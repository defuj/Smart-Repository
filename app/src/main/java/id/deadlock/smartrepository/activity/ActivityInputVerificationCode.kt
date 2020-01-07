package id.deadlock.smartrepository.activity

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.dataCache
import id.deadlock.smartrepository.network.ApiServices
import kotlinx.android.synthetic.main.activity_input_verification_code.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class ActivityInputVerificationCode : AppCompatActivity() {
    private var close : ImageButton? = null
    private var sign : TextView? = null
    private var pd: ProgressDialog? = null
    private var cache : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_verification_code)

        initial()
    }

    private fun initial() {
        cache = getSharedPreferences(dataCache.CACHE,0)
        sign = findViewById(R.id.txtBackToSign)
        close = findViewById(R.id.btnClose)

        runFunction()
    }

    private fun runFunction() {
        sign!!.setOnClickListener {
            startActivity(Intent(this@ActivityInputVerificationCode,ActivitySign::class.java))
        }

        close!!.setOnClickListener {
            startActivity(Intent(this@ActivityInputVerificationCode,ActivityHome::class.java))
        }

        btnVerifikasiAkun!!.setOnClickListener {
            if(editKodeVerifikasi!!.text.isNotEmpty()){
                pd = ProgressDialog(this, R.style.DialogTheme)
                pd!!.setCancelable(false)
                pd!!.show()

                verifikasi()
            }else{
                Toast.makeText(this,"Harap isi kode verifikasi.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifikasi() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiServices.URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api = retrofit.create(ApiServices::class.java)
        val call = api.konfirmasiKodeAkun("verifikasiKode",cache!!.getString(dataCache.username,"user").toString(),editKodeVerifikasi!!.text.toString())
        call.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                pd!!.dismiss()
                Toast.makeText(this@ActivityInputVerificationCode,"Koneksi Bermasalah",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                pd!!.dismiss()
                if(response.isSuccessful){
                    if(response.body() != null){
                        val jsonresponse = response.body()
                        val jsonObject = JSONObject(jsonresponse!!)
                        if(jsonObject.optBoolean("result")){
                            Toast.makeText(this@ActivityInputVerificationCode,"Verifikasi Berhasil.",Toast.LENGTH_SHORT).show()

                            val editor = cache!!.edit()
                            editor.putBoolean(dataCache.status_akun,true)
                            editor.apply()

                            object : CountDownTimer(2000,1000){
                                override fun onFinish() {
                                    startActivity(Intent(this@ActivityInputVerificationCode,ActivityHome::class.java))
                                }

                                override fun onTick(millisUntilFinished: Long) {

                                }

                            }.start()

                        }else{
                            Toast.makeText(this@ActivityInputVerificationCode,"Verifikasi Gagal, silahkan coba lagi!",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityInputVerificationCode,ActivityHome::class.java))
    }
}
