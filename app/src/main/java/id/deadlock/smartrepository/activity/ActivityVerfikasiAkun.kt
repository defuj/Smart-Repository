package id.deadlock.smartrepository.activity

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.adapter.adapterContentDashboard.AdapterListVerifikasiAkun
import id.deadlock.smartrepository.model.ModelListVerifikasiAKun

class ActivityVerfikasiAkun : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var refresh : SwipeRefreshLayout? = null
    private var recycler : RecyclerView? = null
    private var akun : ArrayList<ModelListVerifikasiAKun>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verfikasi_akun)

        initial()
    }

    private fun initial() {
        toolbar = findViewById(R.id.toolbarVerifikasiAkun)
        refresh= findViewById(R.id.refreshVerifikasiAkun)
        recycler = findViewById(R.id.recyclerVerifikasiAkun)

        runFunction()
    }

    private fun runFunction() {
        refresh!!.setOnRefreshListener {
            object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    refresh!!.isRefreshing = false
                    loadList()
                }
            }.start()
        }

        loadList()
    }

    private fun loadList(){
        akun = ArrayList()
        akun!!.clear()
        recycler!!.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)

        for(i in 0 until 2){
            val modelAkun = ModelListVerifikasiAKun
            modelAkun.nama = "Nama Pendaftar $i"
            modelAkun.email = "A$i.1700029@mhs.stmik-sumedang.ac.id"
            modelAkun.nim = "$i"
            akun!!.add(modelAkun)
        }
        val adapter =
            AdapterListVerifikasiAkun(
                this,
                akun!!
            )
        recycler!!.adapter = adapter
    }
}
