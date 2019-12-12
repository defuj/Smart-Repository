package id.deadlock.smartrepository.adapter.adapterContentDashboard

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.model.ModelListVerifikasiAKun

class AdapterListVerifikasiAkun (private val context: Context, private val akun: ArrayList<ModelListVerifikasiAKun>) :
    RecyclerView.Adapter<AdapterListVerifikasiAkun.ViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_list_5, null)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return akun.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.listAkun!!.setOnClickListener {
            if(akun[position].nim == "2"){
                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder.setTitle("Verifikasi Akun")
                dialogBuilder.setMessage("Apakah akun ini sudah memenuhi kriteria dan persyaratan untuk diverifikasi?")
                dialogBuilder.setPositiveButton("Ya") { _, _ ->

                }
                dialogBuilder.setNegativeButton("Tidak") { _, _ ->

                }

                val alert = dialogBuilder.create()
                alert.show()
            }else{
                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder.setTitle("Kode Verifikasi")
                dialogBuilder.setMessage("823387")
                dialogBuilder.setPositiveButton("Tutup") { _, _ ->

                }

                val alert = dialogBuilder.create()
                alert.show()
            }
        }
    }

    inner class ViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        var listAkun = itemView.findViewById<LinearLayout>(R.id.layoutContentAkun)
    }
}