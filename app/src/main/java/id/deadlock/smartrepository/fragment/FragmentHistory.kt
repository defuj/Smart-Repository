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
import id.deadlock.smartrepository.adapter.adapterContentHistory.AdapterListHistory
import id.deadlock.smartrepository.model.ModelListHistory

/**
 * A simple [Fragment] subclass.
 */
class FragmentHistory : Fragment() {
    private var refresh : SwipeRefreshLayout? = null
    private var recyclerViewHistory : RecyclerView? = null
    private var history: ArrayList<ModelListHistory>? = null
    private var toolbar : Toolbar? = null

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
        toolbar = activity!!.findViewById(R.id.toolbarHistory)
        refresh = activity!!.findViewById(R.id.refreshHistory)
        recyclerViewHistory = activity!!.findViewById(R.id.recyclerHistory)
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
        recyclerViewHistory!!.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)

        for(i in 0 until 12){
            val modelHistory = ModelListHistory
            modelHistory.judul = ""
            history!!.add(modelHistory)
        }
        val adapterListHistory =
            AdapterListHistory(
                activity as ActivityHome,
                history!!
            )
        recyclerViewHistory!!.adapter = adapterListHistory
    }

}

