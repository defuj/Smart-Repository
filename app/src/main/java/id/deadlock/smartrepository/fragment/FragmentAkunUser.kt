package id.deadlock.smartrepository.fragment


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.activity.ActivitySign
import id.deadlock.smartrepository.dataCache

/**
 * A simple [Fragment] subclass.
 */
class FragmentAkunUser : Fragment() {
    private var cache : SharedPreferences? = null
    private var toolbar: Toolbar? = null
    private var refresh : SwipeRefreshLayout? = null

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

        runFunction()
    }

    private fun runFunction() {
        refresh!!.setOnRefreshListener {
            object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    refresh!!.isRefreshing = false

                }
            }.start()
        }
        toolbar!!.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_keluar-> keluar()
                R.id.menu_ganti_sandi-> Toast.makeText(activity,"Mengganti", Toast.LENGTH_SHORT).show()
            }
            true
        }
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
