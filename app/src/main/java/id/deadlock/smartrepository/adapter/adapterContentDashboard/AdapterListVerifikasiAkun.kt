package id.deadlock.smartrepository.adapter.adapterContentDashboard

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.model.ModelListVerifikasiAKun
import id.deadlock.smartrepository.network.ApiServices
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class AdapterListVerifikasiAkun (private val context: Context, private val akun: ArrayList<ModelListVerifikasiAKun>) :
    RecyclerView.Adapter<AdapterListVerifikasiAkun.ViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_list_4, null)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return akun.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nama!!.text = akun[position].nama
        holder.email!!.text = akun[position].email

        holder.listAkun!!.setOnClickListener {
            if(akun[position].statusVerif == false){
                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder.setTitle("Verifikasi Akun")
                dialogBuilder.setMessage("Apakah akun ini sudah memenuhi kriteria dan persyaratan untuk diverifikasi?")
                dialogBuilder.setPositiveButton("Ya") { _, _ ->
                    val retrofit = Retrofit.Builder()
                        .baseUrl(ApiServices.URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build()
                    val api = retrofit.create(ApiServices::class.java)
                    val call = api.verifikasiAkun("verifikasiAkun",akun[position].username.toString())
                    call.enqueue(object : Callback<String>{
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(context,"Koneksi Bermasalah",Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if(response.isSuccessful){
                                if(response.body() != null){
                                    val jsonresponse = response.body()
                                    val jsonObject = JSONObject(jsonresponse!!)
                                    if(jsonObject.optBoolean("result")){
                                        val dialogBuilder2 = AlertDialog.Builder(context)
                                        dialogBuilder2.setTitle("Kode Verifikasi")
                                        dialogBuilder2.setMessage(jsonObject.optString("kode"))
                                        dialogBuilder2.setPositiveButton("Tutup") { _, _ ->

                                        }

                                        val alert = dialogBuilder2.create()
                                        alert.show()
                                    }else{
                                        Toast.makeText(context,"Koneksi Bermasalah",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }

                    })
                }
                dialogBuilder.setNegativeButton("Tidak") { _, _ ->

                }

                val alert = dialogBuilder.create()
                alert.show()
            }else{
                val retrofit = Retrofit.Builder()
                    .baseUrl(ApiServices.URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
                val api = retrofit.create(ApiServices::class.java)
                val call = api.verifikasiAkun("getKodeAkun",akun[position].username.toString())
                call.enqueue(object : Callback<String>{
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(context,"Koneksi Bermasalah",Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if(response.isSuccessful){
                            if(response.body() != null){
                                val jsonresponse = response.body()
                                val jsonObject = JSONObject(jsonresponse!!)
                                if(jsonObject.optBoolean("result")){
                                    val dialogBuilder2 = AlertDialog.Builder(context)
                                    dialogBuilder2.setTitle("Kode Verifikasi")
                                    dialogBuilder2.setMessage(jsonObject.optString("kode"))
                                    dialogBuilder2.setPositiveButton("Tutup") { _, _ ->

                                    }

                                    val alert = dialogBuilder2.create()
                                    alert.show()
                                }else{
                                    Toast.makeText(context,"Koneksi Bermasalah",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                })
            }
        }
    }

    inner class ViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        var listAkun = itemView.findViewById<LinearLayout>(R.id.layoutContentAkun)
        var nama = itemView.findViewById<TextView>(R.id.txtNamaPendaftar)
        var email = itemView.findViewById<TextView>(R.id.txtEmailPendaftar)
    }
}