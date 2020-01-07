package id.deadlock.smartrepository.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.adapter.adapterContentDashboard.AdapterListVerifikasiAkun
import id.deadlock.smartrepository.model.ModelListVerifikasiAKun
import id.deadlock.smartrepository.network.ApiServices
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class ActivityVerfikasiAkun : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var refresh : SwipeRefreshLayout? = null
    private var recycler : RecyclerView? = null
    private var akun : ArrayList<ModelListVerifikasiAKun>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verfikasi_akun)

        initial()
    }

    private fun initial() {
        toolbar = findViewById(R.id.toolbarVerifikasiAkun)
        refresh= findViewById(R.id.refreshVerifikasiAkun)
        recycler = findViewById(R.id.recyclerVerifikasiAkun)
        recycler!!.visibility = View.GONE

        runFunction()
    }

    private fun runFunction() {
        toolbar!!.setNavigationOnClickListener {
            finish()
        }

        refresh!!.setOnRefreshListener {
            object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    refresh!!.isRefreshing = false
                    loadList()
                }
            }.start()
        }

        loadList()
    }

    private fun loadList(){
        akun = ArrayList()
        akun!!.clear()
        recycler!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiServices.URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api = retrofit.create(ApiServices::class.java)
        val call = api.listAkun("AkunNonVerif")
        call.enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@ActivityVerfikasiAkun,"Koneksi Bermasalah.",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        val jsonresponse = response.body()
                        val jsonObject = JSONObject(jsonresponse!!)
                        if(jsonObject.optInt("jml") > 0){
                            recycler!!.visibility = View.VISIBLE
                            val dataArray = jsonObject.getJSONArray("akun")
                            for (i in 0 until dataArray.length()) {
                                val dataobj = dataArray.getJSONObject(i)
                                val modelAkun = ModelListVerifikasiAKun()
                                modelAkun.username = dataobj.getString("username")
                                modelAkun.nama = dataobj.getString("nama")
                                modelAkun.email = dataobj.getString("email")
                                modelAkun.tglDaftar = dataobj.getString("tgl_daftar")
                                modelAkun.statusVerif = dataobj.getBoolean("status_verif")
                                akun!!.add(modelAkun)
                            }
                            val adapter = AdapterListVerifikasiAkun(this@ActivityVerfikasiAkun, akun!!)
                            recycler!!.adapter = adapter
                        }else{
                            recycler!!.visibility = View.GONE
                        }
                    }
                }
            }

        })

    }
}
