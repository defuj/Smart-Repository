package id.deadlock.smartrepository.fragment


import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.activity.ActivityGantiSandi
import id.deadlock.smartrepository.activity.ActivitySign
import id.deadlock.smartrepository.activity.ActivityUpload
import id.deadlock.smartrepository.adapter.adapterContentHome.AdapterListCari
import id.deadlock.smartrepository.dataCache
import id.deadlock.smartrepository.model.ModelListCari
import id.deadlock.smartrepository.network.ApiServices
import kotlinx.android.synthetic.main.fragment_akun_user.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * A simple [Fragment] subclass.
 */
class FragmentAkunUser : Fragment() {
    private var cache : SharedPreferences? = null
    private var toolbar: Toolbar? = null
    private var refresh : SwipeRefreshLayout? = null

    private var nama : TextView? = null
    private var email : TextView? = null
    private var status : TextView? = null
    private var nomorInduk : TextView? = null
    private var upload : FloatingActionButton? = null
    private var limit : Int = 10 //+10 tiap load
    private var artikel: ArrayList<ModelListCari>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_akun_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cache = activity!!.getSharedPreferences(dataCache.CACHE,0)
        toolbar = activity!!.findViewById(R.id.toolbarAkun)
        refresh = activity!!.findViewById(R.id.refreshAkun)
        upload = activity!!.findViewById(R.id.floatingActionButtonUpload)

        nama = activity!!.findViewById(R.id.txtNamaLengkap)
        email = activity!!.findViewById(R.id.txtEmail)
        status = activity!!.findViewById(R.id.txtStatus)
        nomorInduk = activity!!.findViewById(R.id.txtNomorInduk)

        runFunction()
    }

    @SuppressLint("SetTextI18n")
    private fun runFunction() {
        upload!!.setOnClickListener {
            startActivity(Intent(activity,ActivityUpload::class.java))
        }
        nama!!.text = cache!!.getString(dataCache.nama,"")
        email!!.text = cache!!.getString(dataCache.email,"")
        //status!!.text = "Status : ${cache!!.getString(dataCache.status_akun,"")}"
        nomorInduk!!.text = "Nomor Induk : ${cache!!.getString(dataCache.username,"")}"

        refresh!!.setOnRefreshListener {
            object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    refresh!!.isRefreshing = false
                    loadInformasi()
                    showArtikel()

                }
            }.start()
        }
        toolbar!!.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_keluar-> keluar()
                R.id.menu_ganti_sandi-> startActivity(Intent(activity!!,ActivityGantiSandi::class.java))
            }
            true
        }

        textLoadMoreListArtikelAkun!!.setOnClickListener {
            limit += 10
            showArtikel()
        }

        recyclerAkunUser!!.visibility = View.GONE
        layoutArtikelAkunEmpty!!.visibility = View.GONE
        textLoadMoreListArtikelAkun!!.visibility = View.GONE

        loadInformasi()
        showArtikel()
    }

    private fun showArtikel() {
        artikel = ArrayList()
        artikel!!.clear()
        recyclerAkunUser!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiServices.URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api = retrofit.create(ApiServices::class.java)
        val call = api.listKaryaTulisAkun("listKaryaTulisAkun",
            cache!!.getString(dataCache.username,"user").toString(),
            limit)

        call.enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {

            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        val jsonresponse = response.body()
                        try{
                            val jsonObject = JSONObject(jsonresponse!!)
                            if(jsonObject.optInt("jml") > 0){
                                layoutArtikelAkunEmpty!!.visibility = View.GONE
                                recyclerAkunUser!!.visibility = View.VISIBLE
                                val dataArray = jsonObject.getJSONArray("artikel")
                                if(jsonObject.optInt("jml_total") > limit){
                                    textLoadMoreListArtikelAkun!!.visibility = View.VISIBLE
                                }else{
                                    textLoadMoreListArtikelAkun!!.visibility = View.GONE
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
                                    artikel!!.add(modelResult)
                                }
                                val adapterListCari = AdapterListCari(activity!!, artikel!!)
                                recyclerAkunUser!!.adapter = adapterListCari
                            }else{
                                layoutArtikelAkunEmpty!!.visibility = View.VISIBLE
                                recyclerAkunUser!!.visibility = View.GONE
                            }
                        }catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

        })
    }

    private fun loadInformasi() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiServices.URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api = retrofit.create(ApiServices::class.java)
        val call = api.InformasiKaryaTulisAkun(
            "InformasiKaryaTulisAkun",
            cache!!.getString(dataCache.username,"user")!!)
        call.enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(activity!!,"Koneksi Bermasalah.",Toast.LENGTH_SHORT).show()
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        val jsonresponse = response.body()
                        val jsonObject = JSONObject(jsonresponse!!)
                        if(jsonObject.optBoolean("result")){
                            jmlPembaca!!.text = jsonObject.optString("jml_pembaca")
                            jmlKaryaTulis!!.text = jsonObject.optString("jml_karya_tulis")
                            jml_viewer!!.text = "${jsonObject.optString("jml_view")}x"
                        }
                    }
                }
            }

        })
    }


    private fun keluar(){
        val dialogBuilder = AlertDialog.Builder(activity!!)
        dialogBuilder.setMessage("Apakah Anda ingin keluar dari akun?")
        dialogBuilder.setPositiveButton("Ya") { _, _ ->
            val editor = cache!!.edit()
            editor.clear()
            editor.apply()

            startActivity(Intent(activity, ActivitySign::class.java))
        }
        dialogBuilder.setNegativeButton("Tidak") { _, _ ->

        }
        val alert = dialogBuilder.create()
        alert.show()
    }


}
