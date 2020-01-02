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
import id.deadlock.smartrepository.adapter.adapterContentHistory.AdapterListHistory
import id.deadlock.smartrepository.dataCache
import id.deadlock.smartrepository.model.ModelListHistory
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
class FragmentHistory : Fragment() {
    private var refresh : SwipeRefreshLayout? = null
    private var recyclerViewHistory : RecyclerView? = null
    private var history: ArrayList<ModelListHistory>? = null
    private var toolbar : Toolbar? = null
    private var cache : SharedPreferences? = null
    private var limit : Int = 10 //+10 tiap load
    private var loadMore : TextView? = null
    private var emptyData : RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initial()
    }

    private fun initial() {
        cache = activity!!.getSharedPreferences(dataCache.CACHE,0)
        toolbar = activity!!.findViewById(R.id.toolbarHistory)
        refresh = activity!!.findViewById(R.id.refreshHistory)
        recyclerViewHistory = activity!!.findViewById(R.id.recyclerHistory)
        loadMore = activity!!.findViewById(R.id.textLoadMore)
        loadMore!!.visibility = View.GONE
        emptyData = activity!!.findViewById(R.id.layoutEmptyDataHistory)
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
                    loadHistory()

                }
            }.start()
        }

        loadMore!!.setOnClickListener {
            limit += 10
            loadHistory()
        }

        toolbar!!.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_hapus_history -> Toast.makeText(
                    activity,
                    "Membersihkan History",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }
        loadHistory()
    }

    private fun loadHistory() {
        history = ArrayList()
        history!!.clear()
        recyclerViewHistory!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiServices.URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api = retrofit.create(ApiServices::class.java)
        val call = api.cariKaryaTulis("list_history",cache!!.getString(dataCache.username,"user").toString(),"","","",limit)
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
                                    val modelHistory = ModelListHistory()
                                    modelHistory.id = dataobj.getString("id")
                                    modelHistory.jenis = dataobj.getString("jenis")
                                    modelHistory.judul = dataobj.getString("judul")
                                    modelHistory.tahun = dataobj.getString("tahun")
                                    modelHistory.tgl_upload = dataobj.getString("tgl_upload")
                                    modelHistory.views = dataobj.getString("views")
                                    modelHistory.readers = dataobj.getString("readers")
                                    modelHistory.status_terbit = dataobj.getString("status_terbit")
                                    modelHistory.source = dataobj.getString("source")
                                    modelHistory.nama = dataobj.getJSONArray("penulis")
                                    modelHistory.favorited = dataobj.getBoolean("favorited")
                                    history!!.add(modelHistory)
                                }
                                val adapterListHistory = AdapterListHistory(activity as ActivityHome, history!!)
                                recyclerViewHistory!!.adapter = adapterListHistory
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
        /*for(i in 0 until 12){
            val modelHistory = ModelListHistory()
            modelHistory.judul = ""
            history!!.add(modelHistory)
        }
        val adapterListHistory = AdapterListHistory(activity as ActivityHome, history!!)
        recyclerViewHistory!!.adapter = adapterListHistory */
    }

}

