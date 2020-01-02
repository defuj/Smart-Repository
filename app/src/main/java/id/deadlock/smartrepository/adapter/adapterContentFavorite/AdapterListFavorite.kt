package id.deadlock.smartrepository.adapter.adapterContentFavorite

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.activity.ActivityBacaArtikel
import id.deadlock.smartrepository.activity.ActivityInputVerificationCode
import id.deadlock.smartrepository.dataCache
import id.deadlock.smartrepository.model.ModelListFavorite
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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
        val cache : SharedPreferences = context.getSharedPreferences(dataCache.CACHE,0)

        holder.judul.text = favorite[position].judul
        holder.jenis.text = favorite[position].jenis
        holder.reads.text = favorite[position].readers
        holder.views.text = "${favorite[position].views}x"

        val format : DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("in-ID"))
        val date : Date = format.parse(favorite[position].tgl_upload!!)!!

        val formatter = SimpleDateFormat("MMMM yyyy", Locale("in-ID"))
        holder.tglRilis.text = formatter.format(date)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ActivityBacaArtikel::class.java)
            intent.putExtra("id",favorite[position].id)
            intent.putExtra("jenis",favorite[position].jenis)
            intent.putExtra("judul",favorite[position].judul)
            intent.putExtra("tahun",favorite[position].tahun)
            intent.putExtra("tgl_upload",favorite[position].tgl_upload)
            intent.putExtra("readers",favorite[position].readers)
            intent.putExtra("views",favorite[position].views)
            intent.putExtra("source",favorite[position].source)
            intent.putExtra("favorited",favorite[position].favorited)
            context.startActivity(intent)
        }

        holder.favoritkan.setOnClickListener {
            if(cache.getBoolean(dataCache.logged,false)){
                if(cache.getBoolean(dataCache.status_akun,false)){
                    Toast.makeText(context,"Telah ditambahkan ke daftar Favorit.", Toast.LENGTH_SHORT).show()
                }else{
                    context.startActivity(Intent(context, ActivityInputVerificationCode::class.java))
                }
            }else{
                Toast.makeText(context,"Harap untuk masuk ke Akun telebih dahulu.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class ViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        var favoritkan : TextView = itemView.findViewById(R.id.txtFavorites)
        var btnFavorite : ImageButton = itemView.findViewById(R.id.btnFavorites)

        var judul : TextView = itemView.findViewById(R.id.txtJudulKaryaTulis)
        var jenis : TextView = itemView.findViewById(R.id.txtJenisKaryaTulis)
        var tglRilis : TextView = itemView.findViewById(R.id.txtTanggalRilis)
        var views : TextView = itemView.findViewById(R.id.txtJumlahView)
        var reads : TextView = itemView.findViewById(R.id.txtJumlahReads)
    }
}