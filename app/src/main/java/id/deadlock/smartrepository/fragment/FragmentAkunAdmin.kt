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
import id.deadlock.smartrepository.adapter.adapterContentDashboard.AdapterListDashboard
import id.deadlock.smartrepository.model.ModelListDashboard

/**
 * A simple [Fragment] subclass.
 */
class FragmentAkunAdmin : Fragment() {
    private var toolbar: Toolbar? = null
    private var refresh : SwipeRefreshLayout? = null
    private var recyclerViewDashboard : RecyclerView? = null
    private var artikel: ArrayList<ModelListDashboard>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_akun_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar = activity!!.findViewById(R.id.toolbarAkunAdmin)
        refresh = activity!!.findViewById(R.id.refreshAkunAdmin)
        recyclerViewDashboard = activity!!.findViewById(R.id.recyclerDasboard)
        runFunction()
    }

    private fun runFunction() {
        refresh!!.setOnRefreshListener {
            object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    refresh!!.isRefreshing = false
                    loadArtikel()
                }
            }.start()
        }
        toolbar!!.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_keluar-> Toast.makeText(activity,"Keluar", Toast.LENGTH_SHORT).show()
                R.id.menu_ganti_sandi-> Toast.makeText(activity,"Mengganti", Toast.LENGTH_SHORT).show()
            }
            true
        }
        loadArtikel()
    }

    private fun loadArtikel(){
        artikel = ArrayList()
        artikel!!.clear()
        recyclerViewDashboard!!.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)

        for(i in 0 until 5){
            val modelDashboard = ModelListDashboard
            modelDashboard.judul = ""
            artikel!!.add(modelDashboard)
        }
        val adapterListDashboard =
            AdapterListDashboard(
                activity as ActivityHome,
                artikel!!
            )
        recyclerViewDashboard!!.adapter = adapterListDashboard
    }
}
