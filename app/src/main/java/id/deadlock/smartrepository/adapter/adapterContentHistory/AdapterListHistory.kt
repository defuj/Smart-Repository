package id.deadlock.smartrepository.adapter.adapterContentHistory

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.model.ModelListHistory

class AdapterListHistory (private val context: Context, private val history: ArrayList<ModelListHistory>) :
    RecyclerView.Adapter<AdapterListHistory.ViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_list_2, null)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return history.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_hapus -> {
                    Toast.makeText(context,"Menghapus dari daftar",Toast.LENGTH_SHORT).show()
                    notifyItemRemoved(position)
                    notifyItemChanged(position,history.size)
                }
                R.id.menu_favorite -> Toast.makeText(context,"Menambahkan ke Favorite",Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    inner class ViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        var toolbar: Toolbar = itemView.findViewById(R.id.toolbar2)
    }
}