package id.deadlock.smartrepository.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.adapter.adapterContentHome.AdapterListCari
import id.deadlock.smartrepository.dataCache
import id.deadlock.smartrepository.model.ModelListCari
import id.deadlock.smartrepository.network.ApiServices
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class ActivityCari : AppCompatActivity() {
    private var refresh : SwipeRefreshLayout? = null
    private var recyclerCari : RecyclerView? = null
    private var modelCari: ArrayList<ModelListCari>? = null

    private var toolbar : Toolbar? = null
    private var cari : EditText? = null
    private var sort : String? = "" //ASC atau DESC
    private var filter : String? = ""
    private var limit : Int = 10 //+10 tiap load

    private var cache : SharedPreferences? = null
    private var notFound : RelativeLayout? = null

    private var loadMore : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cari)
        cache = getSharedPreferences(dataCache.CACHE,0)
        toolbar = findViewById(R.id.toolbarCari)
        cari = findViewById(R.id.editTextCari)
        refresh = findViewById(R.id.refreshCari)
        recyclerCari = findViewById(R.id.recyclerCari)
        notFound = findViewById(R.id.layoutSearchNotFound)
        notFound!!.visibility = View.GONE
        recyclerCari!!.visibility = View.GONE
        loadMore = findViewById(R.id.textLoadMore)
        loadMore!!.visibility = View.GONE

        runFunction()
    }

    private fun runFunction() {
        toolbar!!.setNavigationOnClickListener {
            finish()
        }

        loadMore!!.setOnClickListener {
            limit += 10
            showResult()
        }

        refresh!!.setOnRefreshListener {
            val timer = object: CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    refresh!!.isRefreshing = false
                    if(cari!!.text.toString().isEmpty()){
                        notFound!!.visibility = View.GONE
                        Toast.makeText(this@ActivityCari,"Harap masukan kata kunci.",Toast.LENGTH_SHORT).show()
                    }else{
                        showResult()
                    }
                }
            }
            timer.start()
        }

        cari!!.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                if(cari!!.text.toString().isEmpty()){
                    Toast.makeText(this,"Harap masukan kata kunci.",Toast.LENGTH_SHORT).show()
                }else{
                    showResult()
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun showResult(){
        modelCari = ArrayList()
        modelCari!!.clear()
        recyclerCari!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiServices.URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api = retrofit.create(ApiServices::class.java)
        val call = api.cariKaryaTulis("cari",
            cache!!.getString(dataCache.username,"user").toString(),
            cari!!.text.toString(),
            filter!!,
            sort!!,
            limit)

        call.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {

            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        val jsonresponse = response.body()
                        try{
                            val jsonObject = JSONObject(jsonresponse!!)
                            if(jsonObject.optInt("jml") > 0){
                                notFound!!.visibility = View.GONE
                                recyclerCari!!.visibility = View.VISIBLE
                                val dataArray = jsonObject.getJSONArray("artikel")
                                if(jsonObject.optInt("jml_total") > limit){
                                    loadMore!!.visibility = View.VISIBLE
                                }else{
                                    loadMore!!.visibility = View.GONE
                                }

                                for (i in 0 until dataArray.length()) {
                                    val dataobj = dataArray.getJSONObject(i)
                                    val modelResult = ModelListCari()
                                    modelResult.id = dataobj.getString("id")
                                    modelResult.jenis = dataobj.getString("jenis")
                                    modelResult.judul = dataobj.getString("judul")
                                    modelResult.tahun = dataobj.getString("tahun")
                                    modelResult.tgl_upload = dataobj.getString("tgl_upload")
                                    modelResult.views = dataobj.getString("views")
                                    modelResult.readers = dataobj.getString("readers")
                                    modelResult.status_terbit = dataobj.getString("status_terbit")
                                    modelResult.source = dataobj.getString("source")
                                    modelResult.nama = dataobj.getJSONArray("penulis")
                                    modelResult.favorited = dataobj.getBoolean("favorited")
                                    modelCari!!.add(modelResult)
                                }
                                val adapterListCari = AdapterListCari(this@ActivityCari, modelCari!!)
                                recyclerCari!!.adapter = adapterListCari
                            }else{
                                notFound!!.visibility = View.VISIBLE
                                recyclerCari!!.visibility = View.GONE
                            }
                        }catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

        })
    }

    override fun onBackPressed() {
        finish()
    }
}
