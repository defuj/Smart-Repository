package id.deadlock.smartrepository.fragment


import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.activity.ActivityCari
import id.deadlock.smartrepository.activity.ActivityHome
import id.deadlock.smartrepository.activity.ActivityListKaryaTulis
import id.deadlock.smartrepository.adapter.adapterContentHome.AdapterListPopular
import id.deadlock.smartrepository.adapter.adapterContentHome.AdapterListRekomendasi
import id.deadlock.smartrepository.adapter.adapterContentHome.AdapterListTerbaru
import id.deadlock.smartrepository.dataCache
import id.deadlock.smartrepository.model.ModelListPopular
import id.deadlock.smartrepository.model.ModelListRekomendasi
import id.deadlock.smartrepository.model.ModelListTerbaru
import id.deadlock.smartrepository.network.ApiServices
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
class FragmentHome : Fragment() {
    private var refresh : SwipeRefreshLayout? = null
    private var recyclerViewPopular : RecyclerView? = null
    private var popular: ArrayList<ModelListPopular>? = null

    private var recyclerViewRekomendasi : RecyclerView? = null
    private var rekomendasi: ArrayList<ModelListRekomendasi>? = null

    private var recyclerViewTerbaru : RecyclerView? = null
    private var terbaru: ArrayList<ModelListTerbaru>? = null

    private var layoutTerbaru : LinearLayout? = null
    private var layoutPopuler : LinearLayout? = null
    private var layoutRekomendasi : LinearLayout? = null

    private var toolbar : Toolbar? = null

    private var menuCari : TextView? = null
    private var menuNotif : ImageButton? = null
    private var menuJurnal : ImageView? = null
    private var menuSkripsi : ImageView? = null
    private var menuTa : ImageView? = null
    private var menuStmik : ImageView? = null
    private var cache : SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initial()
    }

    private fun initial() {
        cache = activity!!.getSharedPreferences(dataCache.CACHE,0)
        layoutTerbaru= activity!!.findViewById(R.id.layoutTerbaru)
        layoutRekomendasi= activity!!.findViewById(R.id.layoutRekomendasi)
        layoutPopuler= activity!!.findViewById(R.id.layoutPopuler)

        menuCari = activity!!.findViewById(R.id.menu_cari)
        menuNotif = activity!!.findViewById(R.id.menu_notif)
        menuJurnal = activity!!.findViewById(R.id.menu_jurnal)
        menuSkripsi = activity!!.findViewById(R.id.menu_skripsi)
        menuTa = activity!!.findViewById(R.id.menu_ta)
        menuStmik = activity!!.findViewById(R.id.menu_stmik)

        toolbar = activity!!.findViewById(R.id.toolbarHome)
        refresh = activity!!.findViewById(R.id.refreshHome)
        recyclerViewPopular = activity!!.findViewById(R.id.recyclerPopular)
        recyclerViewRekomendasi = activity!!.findViewById(R.id.recyclerRekomendasi)
        recyclerViewTerbaru = activity!!.findViewById(R.id.recyclerTerbaru)

        runFunction()
    }

    private fun runFunction() {
        layoutTerbaru!!.visibility = View.GONE
        layoutRekomendasi!!.visibility = View.GONE
        layoutPopuler!!.visibility = View.GONE

        loadListPopular()
        loadListRekomendasi()
        loadListTerbaru()

        menuCari!!.setOnClickListener {
            //Toast.makeText(activity, "This feature is under development.",Toast.LENGTH_SHORT).show()
            startActivity(Intent(activity,ActivityCari::class.java))
        }
        menuNotif!!.setOnClickListener {
            Toast.makeText(activity, "This feature is under development.",Toast.LENGTH_SHORT).show()
        }
        menuJurnal!!.setOnClickListener {
            //Toast.makeText(activity, "This feature is under development.",Toast.LENGTH_SHORT).show()
            val intent = Intent(activity,ActivityListKaryaTulis::class.java)
            intent.putExtra("kategori","Artikel")
            startActivity(intent)
        }
        menuSkripsi!!.setOnClickListener {
            //Toast.makeText(activity, "This feature is under development.",Toast.LENGTH_SHORT).show()
            val intent = Intent(activity,ActivityListKaryaTulis::class.java)
            intent.putExtra("kategori","Skripsi")
            startActivity(intent)
        }
        menuTa!!.setOnClickListener {
            //Toast.makeText(activity, "This feature is under development.",Toast.LENGTH_SHORT).show()
            val intent = Intent(activity,ActivityListKaryaTulis::class.java)
            intent.putExtra("kategori","Tugas Akhir")
            startActivity(intent)
        }
        menuStmik!!.setOnClickListener {
            //Toast.makeText(activity, "This feature is under development.",Toast.LENGTH_SHORT).show()
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://stmik-sumedang.ac.id")))
        }

        refresh!!.setOnRefreshListener {
            val timer = object: CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    refresh!!.isRefreshing = false
                    loadListPopular()
                    loadListRekomendasi()
                    loadListTerbaru()
                }
            }
            timer.start()
        }

        toolbar!!.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_cari-> Toast.makeText(activity,"Membuka Pencarian",Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    private fun loadListTerbaru() {
        terbaru = ArrayList()
        terbaru!!.clear()
        recyclerViewTerbaru!!.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiServices.URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api = retrofit.create(ApiServices::class.java)
        val call = api.karyaTulis("beranda_karya_tulis_baru",cache!!.getString(dataCache.username,"user").toString())
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
                                layoutTerbaru!!.visibility = View.VISIBLE
                                val dataArray = jsonObject.getJSONArray("artikel")
                                for (i in 0 until dataArray.length()) {
                                    val dataobj = dataArray.getJSONObject(i)
                                    val modelTerbaru = ModelListTerbaru()
                                    modelTerbaru.id = dataobj.getString("id")
                                    modelTerbaru.jenis = dataobj.getString("jenis")
                                    modelTerbaru.judul = dataobj.getString("judul")
                                    modelTerbaru.tahun = dataobj.getString("tahun")
                                    modelTerbaru.tgl_upload = dataobj.getString("tgl_upload")
                                    modelTerbaru.views = dataobj.getString("views")
                                    modelTerbaru.readers = dataobj.getString("readers")
                                    modelTerbaru.status_terbit = dataobj.getString("status_terbit")
                                    modelTerbaru.source = dataobj.getString("source")

                                    modelTerbaru.nama = dataobj.getJSONArray("penulis")

                                    //modelTerbaru.abstrak = dataobj.getString("abstrak")
                                    modelTerbaru.favorited = dataobj.getBoolean("favorited")
                                    terbaru!!.add(modelTerbaru)
                                }
                                val adapterListTerbaru = AdapterListTerbaru(activity as ActivityHome, terbaru!!)
                                recyclerViewTerbaru!!.adapter = adapterListTerbaru
                            }else{
                                layoutTerbaru!!.visibility = View.GONE
                            }
                        }catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

        })
    }

    private fun loadListRekomendasi() {
        rekomendasi = ArrayList()
        rekomendasi!!.clear()
        recyclerViewRekomendasi!!.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiServices.URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api = retrofit.create(ApiServices::class.java)
        val call = api.karyaTulis("beranda_karya_tulis_rekomendasi",cache!!.getString(dataCache.username,"user").toString())
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
                                layoutRekomendasi!!.visibility = View.VISIBLE
                                val dataArray = jsonObject.getJSONArray("artikel")
                                for (i in 0 until dataArray.length()) {
                                    val dataobj = dataArray.getJSONObject(i)
                                    val modelRekomendasi = ModelListRekomendasi()
                                    modelRekomendasi.id = dataobj.getString("id")
                                    modelRekomendasi.jenis = dataobj.getString("jenis")
                                    modelRekomendasi.judul = dataobj.getString("judul")
                                    modelRekomendasi.tahun = dataobj.getString("tahun")
                                    modelRekomendasi.tgl_upload = dataobj.getString("tgl_upload")
                                    modelRekomendasi.views = dataobj.getString("views")
                                    modelRekomendasi.readers = dataobj.getString("readers")
                                    modelRekomendasi.status_terbit = dataobj.getString("status_terbit")
                                    modelRekomendasi.source = dataobj.getString("source")
                                    modelRekomendasi.nama = dataobj.getJSONArray("penulis")

                                    //modelTerbaru.abstrak = dataobj.getString("abstrak")
                                    modelRekomendasi.favorited = dataobj.getBoolean("favorited")
                                    rekomendasi!!.add(modelRekomendasi)
                                }
                                val adapterListRekomendasi = AdapterListRekomendasi(activity as ActivityHome, rekomendasi!!)
                                recyclerViewRekomendasi!!.adapter = adapterListRekomendasi
                            }else{
                                layoutRekomendasi!!.visibility = View.GONE
                            }
                        }catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

        })
    }

    private fun loadListPopular(){
        popular = ArrayList()
        popular!!.clear()
        recyclerViewPopular!!.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiServices.URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api = retrofit.create(ApiServices::class.java)
        val call = api.karyaTulis("beranda_karya_tulis_populer",cache!!.getString(dataCache.username,"user").toString())
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
                                layoutPopuler!!.visibility = View.VISIBLE
                                val dataArray = jsonObject.getJSONArray("artikel")
                                for (i in 0 until dataArray.length()) {
                                    val dataobj = dataArray.getJSONObject(i)
                                    val modelPopular = ModelListPopular()
                                    modelPopular.id = dataobj.getString("id")
                                    modelPopular.jenis = dataobj.getString("jenis")
                                    modelPopular.judul = dataobj.getString("judul")
                                    modelPopular.tahun = dataobj.getString("tahun")
                                    modelPopular.tgl_upload = dataobj.getString("tgl_upload")
                                    modelPopular.views = dataobj.getString("views")
                                    modelPopular.readers = dataobj.getString("readers")
                                    modelPopular.status_terbit = dataobj.getString("status_terbit")
                                    modelPopular.source = dataobj.getString("source")
                                    modelPopular.nama = dataobj.getJSONArray("penulis")

                                    //modelTerbaru.abstrak = dataobj.getString("abstrak")
                                    modelPopular.favorited = dataobj.getBoolean("favorited")
                                    popular!!.add(modelPopular)
                                }
                                val adapterListPopuler = AdapterListPopular(activity as ActivityHome, popular!!)
                                recyclerViewPopular!!.adapter = adapterListPopuler
                            }else{
                                layoutPopuler!!.visibility = View.GONE
                            }
                        }catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

        })
    }
}
