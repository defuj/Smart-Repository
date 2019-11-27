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
import id.deadlock.smartrepository.adapter.adapterContentFavorite.AdapterListFavorite
import id.deadlock.smartrepository.model.ModelListFavorite

/**
 * A simple [Fragment] subclass.
 */
class FragmentFavorite : Fragment() {
    private var toolbar:Toolbar? = null
    private var refresh : SwipeRefreshLayout? = null
    private var recyclerViewFavorite : RecyclerView? = null
    private var favorite: ArrayList<ModelListFavorite>? = null

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
        recyclerViewFavorite!!.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)

        for(i in 0 until 5){
            val modelFavorite = ModelListFavorite
            modelFavorite.judul = ""
            favorite!!.add(modelFavorite)
        }
        val adapterListFavorite =
            AdapterListFavorite(
                activity as ActivityHome,
                favorite!!
            )
        recyclerViewFavorite!!.adapter = adapterListFavorite
    }

}
