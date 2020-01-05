package id.deadlock.smartrepository.fragment


import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.activity.ActivityHome
import id.deadlock.smartrepository.adapter.adapterContentFavorite.AdapterListFavorite
import id.deadlock.smartrepository.dataCache
import id.deadlock.smartrepository.model.ModelListFavorite
import id.deadlock.smartrepository.network.ApiServices
import kotlinx.android.synthetic.main.fragment_favorite.*
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
class FragmentFavorite : Fragment() {
    private var favorite: ArrayList<ModelListFavorite>? = null
    private var cache : SharedPreferences? = null
    private var limit : Int = 10 //+10 tiap load

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initial()
    }

    private fun initial() {
        cache = activity!!.getSharedPreferences(dataCache.CACHE,0)
        textLoadMoreFavorite!!.visibility = View.GONE
        layoutEmptyDataFavorite!!.visibility = View.GONE
        runFunction()
    }

    private fun runFunction() {
        refreshFavorite!!.setOnRefreshListener {
            object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    refreshFavorite!!.isRefreshing = false
                    loadFavorite()

                }
            }.start()
        }

        textLoadMoreFavorite!!.setOnClickListener {
            limit += 10
            loadFavorite()
        }

        toolbarFavorite!!.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_hapus_favorite-> Toast.makeText(activity,"Menghapus", Toast.LENGTH_SHORT).show()
                R.id.menu_tambah_favorite-> Toast.makeText(activity,"Menambah", Toast.LENGTH_SHORT).show()
            }
            true
        }
        loadFavorite()
    }

    private fun loadFavorite() {
        favorite = ArrayList()
        favorite!!.clear()
        recyclerFavorite!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiServices.URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api = retrofit.create(ApiServices::class.java)
        val call = api.cariKaryaTulis("list_favorite",cache!!.getString(dataCache.username,"user").toString(),"","","",limit)
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
                                layoutEmptyDataFavorite!!.visibility = View.GONE
                                val dataArray = jsonObject.getJSONArray("artikel")
                                if(jsonObject.optInt("jml_total") > limit){
                                    textLoadMoreFavorite!!.visibility = View.VISIBLE
                                }else{
                                    textLoadMoreFavorite!!.visibility = View.GONE
                                }

                                for (i in 0 until dataArray.length()) {
                                    val dataobj = dataArray.getJSONObject(i)
                                    val modelFavorite = ModelListFavorite()
                                    modelFavorite.id = dataobj.getString("id")
                                    modelFavorite.jenis = dataobj.getString("jenis")
                                    modelFavorite.judul = dataobj.getString("judul")
                                    modelFavorite.tahun = dataobj.getString("tahun")
                                    modelFavorite.tgl_upload = dataobj.getString("tgl_upload")
                                    modelFavorite.views = dataobj.getString("views")
                                    modelFavorite.readers = dataobj.getString("readers")
                                    modelFavorite.status_terbit = dataobj.getString("status_terbit")
                                    modelFavorite.source = dataobj.getString("source")
                                    modelFavorite.nama = dataobj.getJSONArray("penulis")
                                    modelFavorite.favorited = dataobj.getBoolean("favorited")
                                    favorite!!.add(modelFavorite)
                                }
                                val adapterListFavorite = AdapterListFavorite(activity as ActivityHome, favorite!!)
                                recyclerFavorite!!.adapter = adapterListFavorite
                            }else{
                                layoutEmptyDataFavorite!!.visibility = View.VISIBLE
                                textLoadMoreFavorite!!.visibility = View.GONE
                            }
                        }catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

        })

        /*for(i in 0 until 5){
            val modelFavorite = ModelListFavorite()
            modelFavorite.judul = ""
            favorite!!.add(modelFavorite)
        }
        val adapterListFavorite = AdapterListFavorite(activity as ActivityHome, favorite!!)
        recyclerViewFavorite!!.adapter = adapterListFavorite*/
    }

}
