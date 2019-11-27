package id.deadlock.smartrepository.fragment


import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.activity.ActivityHome
import id.deadlock.smartrepository.adapter.adapterContentHome.AdapterListPopular
import id.deadlock.smartrepository.adapter.adapterContentHome.AdapterListRekomendasi
import id.deadlock.smartrepository.adapter.adapterContentHome.AdapterListTerbaru
import id.deadlock.smartrepository.model.ModelListPopular
import id.deadlock.smartrepository.model.ModelListRekomendasi
import id.deadlock.smartrepository.model.ModelListTerbaru

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

    private var toolbar : Toolbar? = null

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
        toolbar = activity!!.findViewById(R.id.toolbarHome)
        refresh = activity!!.findViewById(R.id.refreshHome)
        recyclerViewPopular = activity!!.findViewById(R.id.recyclerPopular)
        recyclerViewRekomendasi = activity!!.findViewById(R.id.recyclerRekomendasi)
        recyclerViewTerbaru = activity!!.findViewById(R.id.recyclerTerbaru)

        runFunction()
    }

    private fun runFunction() {
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

        loadListPopular()
        loadListRekomendasi()
        loadListTerbaru()
    }

    private fun loadListTerbaru() {
        terbaru = ArrayList()
        terbaru!!.clear()
        recyclerViewTerbaru!!.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

        for(i in 0 until 12){
            val modelTerbaru = ModelListTerbaru
            modelTerbaru.judul = ""
            terbaru!!.add(modelTerbaru)
        }
        val adapterListTerbaru =
            AdapterListTerbaru(
                activity as ActivityHome,
                terbaru!!
            )
        recyclerViewTerbaru!!.adapter = adapterListTerbaru
    }

    private fun loadListRekomendasi() {
        rekomendasi = ArrayList()
        rekomendasi!!.clear()
        recyclerViewRekomendasi!!.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

        for(i in 0 until 12){
            val modelRekomendasi = ModelListRekomendasi
            modelRekomendasi.judul = ""
            rekomendasi!!.add(modelRekomendasi)
        }
        val adapterRekomendasi =
            AdapterListRekomendasi(
                activity as ActivityHome,
                rekomendasi!!
            )
        recyclerViewRekomendasi!!.adapter = adapterRekomendasi
    }

    private fun loadListPopular(){
        popular = ArrayList()
        popular!!.clear()
        recyclerViewPopular!!.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

        for(i in 0 until 12){
            val modelPopular = ModelListPopular
            modelPopular.judul = ""
            popular!!.add(modelPopular)
        }
        val adapterPopular =
            AdapterListPopular(
                activity as ActivityHome, popular!!
            )
        recyclerViewPopular!!.adapter = adapterPopular
    }
}