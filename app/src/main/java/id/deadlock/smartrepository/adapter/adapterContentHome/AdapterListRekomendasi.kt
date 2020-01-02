package id.deadlock.smartrepository.adapter.adapterContentHome

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.activity.ActivityBacaArtikel
import id.deadlock.smartrepository.dataCache
import id.deadlock.smartrepository.model.ModelListRekomendasi
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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
        val cache : SharedPreferences = context.getSharedPreferences(dataCache.CACHE,0)

        holder.judul.text = rekomendasi[position].judul
        holder.jenis.text = rekomendasi[position].jenis
        holder.reads.text = rekomendasi[position].readers
        holder.views.text = "${rekomendasi[position].views}x"

        val format : DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("in-ID"))
        val date : Date = format.parse(rekomendasi[position].tgl_upload!!)!!

        val formatter = SimpleDateFormat("MMMM yyyy", Locale("in-ID"))
        holder.tglRilis.text = formatter.format(date)

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, ActivityBacaArtikel::class.java))
        }

        /*holder.favoritkan.setOnClickListener {
            if(cache.getBoolean(dataCache.logged,false)){
                if(cache.getBoolean(dataCache.status_akun,false)){
                    Toast.makeText(context,"Telah ditambahkan ke daftar Favorit.", Toast.LENGTH_SHORT).show()
                }else{
                    context.startActivity(Intent(context, ActivityInputVerificationCode::class.java))
                }
            }else{
                Toast.makeText(context,"Harap untuk masuk ke Akun telebih dahulu.", Toast.LENGTH_SHORT).show()
            }
        }*/

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ActivityBacaArtikel::class.java)
            intent.putExtra("id",rekomendasi[position].id)
            intent.putExtra("jenis",rekomendasi[position].jenis)
            intent.putExtra("judul",rekomendasi[position].judul)
            intent.putExtra("tahun",rekomendasi[position].tahun)
            intent.putExtra("tgl_upload",rekomendasi[position].tgl_upload)
            intent.putExtra("readers",rekomendasi[position].readers)
            intent.putExtra("views",rekomendasi[position].views)
            intent.putExtra("source",rekomendasi[position].source)
            intent.putExtra("favorited",rekomendasi[position].favorited)
            context.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        var judul : TextView = itemView.findViewById(R.id.txtJudulKaryaTulis)
        var jenis : TextView = itemView.findViewById(R.id.txtJenisKaryaTulis)
        var tglRilis : TextView = itemView.findViewById(R.id.txtTanggalRilis)
        var views : TextView = itemView.findViewById(R.id.txtJumlahView)
        var reads : TextView = itemView.findViewById(R.id.txtJumlahReads)
    }
}