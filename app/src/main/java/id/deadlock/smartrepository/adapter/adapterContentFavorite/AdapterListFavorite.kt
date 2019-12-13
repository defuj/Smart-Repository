package id.deadlock.smartrepository.adapter.adapterContentFavorite

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.activity.ActivityBacaArtikel
import id.deadlock.smartrepository.model.ModelListFavorite

class AdapterListFavorite (private val context: Context, private val favorite: ArrayList<ModelListFavorite>) :
    RecyclerView.Adapter<AdapterListFavorite.ViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_list_2, null)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return favorite.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, ActivityBacaArtikel::class.java))
        }
    }

    inner class ViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        //var toolbar: Toolbar = itemView.findViewById(R.id.toolbar3)
    }
}