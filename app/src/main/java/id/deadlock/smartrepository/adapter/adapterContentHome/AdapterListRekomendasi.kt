package id.deadlock.smartrepository.adapter.adapterContentHome

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.model.ModelListRekomendasi

class AdapterListRekomendasi (private val context: Context, private val rekomendasi: ArrayList<ModelListRekomendasi>) :
    RecyclerView.Adapter<AdapterListRekomendasi.ViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_list_1, null)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return rekomendasi.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    inner class ViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        //var number: TextView = itemView.findViewById(R.id.txtNumber)
    }
}