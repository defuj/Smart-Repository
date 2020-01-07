package id.deadlock.smartrepository.fragment


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.activity.*
import id.deadlock.smartrepository.adapter.adapterContentDashboard.AdapterListDashboard
import id.deadlock.smartrepository.dataCache
import id.deadlock.smartrepository.model.ModelListDashboard

/**
 * A simple [Fragment] subclass.
 */
class FragmentAkunAdmin : Fragment() {
    private var cache : SharedPreferences? = null
    private var toolbar: Toolbar? = null
    private var refresh : SwipeRefreshLayout? = null
    private var recyclerViewDashboard : RecyclerView? = null
    private var artikel: ArrayList<ModelListDashboard>? = null
    private var upload : LinearLayout?= null
    private var verifikasiAkun : LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_akun_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cache = activity!!.getSharedPreferences(dataCache.CACHE,0)
        toolbar = activity!!.findViewById(R.id.toolbarAkunAdmin)
        refresh = activity!!.findViewById(R.id.refreshAkunAdmin)
        recyclerViewDashboard = activity!!.findViewById(R.id.recyclerDasboard)
        upload = activity!!.findViewById(R.id.layoutBtnUpload)
        verifikasiAkun = activity!!.findViewById(R.id.layoutBtnVerifikasiAkun)
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
                R.id.menu_keluar-> keluar()
                R.id.menu_ganti_sandi-> startActivity(Intent(activity!!, ActivityGantiSandi::class.java))
            }
            true
        }

        upload!!.setOnClickListener {
            startActivity(Intent(activity,ActivityUpload::class.java))
        }

        verifikasiAkun!!.setOnClickListener {
            startActivity(Intent(activity,ActivityVerfikasiAkun::class.java))
        }

        loadArtikel()
    }

    private fun keluar(){
        val dialogBuilder = AlertDialog.Builder(activity!!)
        dialogBuilder.setMessage("Apakah Anda ingin keluar dari akun?")
        dialogBuilder.setPositiveButton("Ya") { _, _ ->
            val editor = cache!!.edit()
            editor.clear()
            editor.apply()

            startActivity(Intent(activity,ActivitySign::class.java))
        }
        dialogBuilder.setNegativeButton("Tidak") { _, _ ->

        }
        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun loadArtikel(){
        artikel = ArrayList()
        artikel!!.clear()
        recyclerViewDashboard!!.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)

        for(i in 0 until 5){
            val modelDashboard = ModelListDashboard()
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
