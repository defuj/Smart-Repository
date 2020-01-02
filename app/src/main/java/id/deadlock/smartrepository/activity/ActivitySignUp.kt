package id.deadlock.smartrepository.activity

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.dataCache
import id.deadlock.smartrepository.helper.SnackbarHelper
import id.deadlock.smartrepository.network.ApiServices
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class ActivitySignUp : AppCompatActivity() {
    private var btnBack : ImageButton? = null
    private var btnDaftar : Button? = null
    private var nomorInduk : EditText? = null
    private var namaLengkap : EditText? = null
    private var email : EditText? = null
    private var sandi : EditText? = null
    private var konfirSandi : EditText? = null
    private var pd: ProgressDialog? = null
    private var cache : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initial()
    }

    private fun initial() {
        cache = getSharedPreferences(dataCache.CACHE,0)
        btnBack = findViewById(R.id.btnBack)
        btnDaftar = findViewById(R.id.btnDaftar)
        nomorInduk = findViewById(R.id.editNomorInduk)
        namaLengkap = findViewById(R.id.editNamaLengkap)
        email = findViewById(R.id.editEmail)
        sandi = findViewById(R.id.editPassword)
        konfirSandi = findViewById(R.id.editConfirmPassword)

        runFunction()
    }

    private fun runFunction() {
        btnBack!!.setOnClickListener {
            finish()
        }

        btnDaftar!!.setOnClickListener {
            if(nomorInduk!!.text.toString().isNotEmpty() &&
                    namaLengkap!!.text.toString().isNotEmpty() &&
                    email!!.text.toString().isNotEmpty() &&
                    sandi!!.text.toString().isNotEmpty() &&
                    konfirSandi!!.text.toString().isNotEmpty()){
                if(sandi!!.text.toString() == konfirSandi!!.text.toString()){
                    daftar()
                }else{
                    showSnackbar("Oops, konfirmasi kata sandi tidak sesuai.")
                }
            }else{
                showSnackbar("Oops, masih ada form yang kosong.")
            }
        }
    }

    private fun showSnackbar(s: String) {
        val snack = Snackbar.make(findViewById(android.R.id.content),
            s,
            Snackbar.LENGTH_LONG)
        SnackbarHelper.configSnackbar(this,snack)
        snack.show()
    }

    private fun daftar() {
        pd = ProgressDialog(this, R.style.DialogTheme)
        pd!!.setCancelable(false)
        pd!!.show()

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiServices.URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api = retrofit.create(ApiServices::class.java)
        val call = api.daftar("daftar",nomorInduk!!.text.toString(),sandi!!.text.toString(),namaLengkap!!.text.toString(),email!!.text.toString())
        call.enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                pd!!.dismiss()
                showSnackbar("Koneksi Bermasalah.")
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                pd!!.dismiss()
                if(response.isSuccessful){
                    if(response.body() != null){
                        val jsonresponse = response.body()
                        val jsonObject = JSONObject(jsonresponse!!)
                        if(!jsonObject.optBoolean("result")){
                            showSnackbar(jsonObject.optString("msg"))
                        }else{
                            showSnackbar(jsonObject.optString("msg"))

                            val dataArray = jsonObject.getJSONArray("data")
                            for (i in 0 until dataArray.length()) {
                                val dataobj = dataArray.getJSONObject(i)

                                val editor = cache!!.edit()
                                editor.putBoolean(dataCache.show_login,false)
                                editor.putBoolean(dataCache.logged,true)
                                editor.putBoolean(dataCache.status_akun,dataobj.getBoolean("status_akun"))
                                editor.putString(dataCache.username,dataobj.getString("username"))
                                editor.putString(dataCache.email,dataobj.getString("email"))
                                editor.putString(dataCache.nama,dataobj.getString("nama"))
                                editor.putString(dataCache.password,dataobj.getString("password"))
                                editor.putString(dataCache.akses,dataobj.getString("akses"))
                                editor.apply()
                            }

                            object : CountDownTimer(2000,1000){
                                override fun onFinish() {
                                    startActivity(
                                        Intent(this@ActivitySignUp,
                                            ActivityInputVerificationCode::class.java)
                                    )
                                }

                                override fun onTick(millisUntilFinished: Long) {

                                }

                            }.start()
                        }
                    }else{
                        showSnackbar("Terjadi Kesalahan.")
                    }
                }else{
                    showSnackbar("Terjadi Kesalahan.")
                }
            }

        })
    }
}
