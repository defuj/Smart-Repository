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
import id.deadlock.smartrepository.model.ModelListPopular
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AdapterListPopular (private val context: Context, private val popular: ArrayList<ModelListPopular>) :
    RecyclerView.Adapter<AdapterListPopular.ViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_list_1, null)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return popular.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cache : SharedPreferences = context.getSharedPreferences(dataCache.CACHE,0)

        holder.judul.text = popular[position].judul
        holder.jenis.text = popular[position].jenis
        holder.reads.text = popular[position].readers
        holder.views.text = "${popular[position].views}x"

        val format : DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("in-ID"))
        val date : Date = format.parse(popular[position].tgl_upload!!)!!

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
            intent.putExtra("id",popular[position].id)
            intent.putExtra("jenis",popular[position].jenis)
            intent.putExtra("judul",popular[position].judul)
            intent.putExtra("tahun",popular[position].tahun)
            intent.putExtra("tgl_upload",popular[position].tgl_upload)
            intent.putExtra("readers",popular[position].readers)
            intent.putExtra("views",popular[position].views)
            intent.putExtra("source",popular[position].source)
            intent.putExtra("favorited",popular[position].favorited)
            context.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        //var favoritkan : TextView = itemView.findViewById(R.id.txtFavorites)

        var judul : TextView = itemView.findViewById(R.id.txtJudulKaryaTulis)
        var jenis : TextView = itemView.findViewById(R.id.txtJenisKaryaTulis)
        var tglRilis : TextView = itemView.findViewById(R.id.txtTanggalRilis)
        var views : TextView = itemView.findViewById(R.id.txtJumlahView)
        var reads : TextView = itemView.findViewById(R.id.txtJumlahReads)
    }
}