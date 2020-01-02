package id.deadlock.smartrepository.fragment


import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.activity.ActivityHome
import id.deadlock.smartrepository.adapter.adapterContentFavorite.AdapterListFavorite
import id.deadlock.smartrepository.dataCache
import id.deadlock.smartrepository.model.ModelListFavorite
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
class FragmentFavorite : Fragment() {
    private var toolbar:Toolbar? = null
    private var refresh : SwipeRefreshLayout? = null
    private var recyclerViewFavorite : RecyclerView? = null
    private var favorite: ArrayList<ModelListFavorite>? = null
    private var cache : SharedPreferences? = null
    private var limit : Int = 10 //+10 tiap load
    private var loadMore : TextView? = null
    private var emptyData : RelativeLayout? = null

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
        toolbar = activity!!.findViewById(R.id.toolbarFavorite)
        refresh = activity!!.findViewById(R.id.refreshFavorite)
        recyclerViewFavorite = activity!!.findViewById(R.id.recyclerFavorite)
        cache = activity!!.getSharedPreferences(dataCache.CACHE,0)
        loadMore = activity!!.findViewById(R.id.textLoadMore)
        loadMore!!.visibility = View.GONE
        emptyData = activity!!.findViewById(R.id.layoutEmptyDataFavorite)
        emptyData!!.visibility = View.GONE
        runFunction()
    }

    private fun runFunction() {
        refresh!!.setOnRefreshListener {
            object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    refresh!!.isRefreshing = false
                    loadFavorite()

                }
            }.start()
        }

        loadMore!!.setOnClickListener {
            limit += 10
            loadFavorite()
        }

        toolbar!!.setOnMenuItemClickListener {
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
        recyclerViewFavorite!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)

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
                                emptyData!!.visibility = View.GONE
                                val dataArray = jsonObject.getJSONArray("artikel")
                                if(jsonObject.optInt("jml_total") > limit){
                                    loadMore!!.visibility = View.VISIBLE
                                }else{
                                    loadMore!!.visibility = View.GONE
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
                                recyclerViewFavorite!!.adapter = adapterListFavorite
                            }else{
                                emptyData!!.visibility = View.VISIBLE
                                loadMore!!.visibility = View.GONE
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
