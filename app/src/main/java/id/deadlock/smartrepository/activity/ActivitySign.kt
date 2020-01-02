package id.deadlock.smartrepository.activity

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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

class ActivitySign : AppCompatActivity() {
    private var signin : Button? = null
    private var signup : Button? = null
    private var close : ImageButton? = null
    private var cache : SharedPreferences? = null
    private var username : EditText? = null
    private var password : EditText? = null
    private var pd: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        initial()
    }

    private fun initial() {
        cache = getSharedPreferences(dataCache.CACHE,0)
        close = findViewById(R.id.btnClose)
        signin = findViewById(R.id.btnMasuk)
        signup = findViewById(R.id.btnDaftar)
        username = findViewById(R.id.editUsername)
        password = findViewById(R.id.editPassword)

        runFunction()
    }

    private fun runFunction() {
        signin!!.setOnClickListener {
            validasiUserPassword()
        }
        signup!!.setOnClickListener {
            startActivity(Intent(this@ActivitySign,
                ActivitySignUp::class.java))
        }
        close!!.setOnClickListener {
            val editor = cache!!.edit()
            editor.putBoolean(dataCache.show_login,false)
            editor.apply()
            startActivity(Intent(this@ActivitySign,ActivityHome::class.java))
        }
    }

    private fun validasiUserPassword() {
        if(username!!.text.toString().isNotEmpty()){
            if(password!!.text.toString().isNotEmpty()){
                login()
            }else{
                showSnackbar("Kata sandi harus diisi.")
            }
        }else{
            showSnackbar("NIM/NIDN harus diisi.")
        }
    }

    private fun login() {
        pd = ProgressDialog(this, R.style.DialogTheme)
        pd!!.setCancelable(false)
        pd!!.show()

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiServices.URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api = retrofit.create(ApiServices::class.java)
        val call = api.masuk("masuk",username!!.text.toString(),password!!.text.toString())
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

                                if(!dataobj.getBoolean("status_akun")){
                                    startActivity(Intent(this@ActivitySign,ActivityInputVerificationCode::class.java))
                                }else{
                                    startActivity(Intent(this@ActivitySign,ActivityHome::class.java))
                                }
                            }
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

    private fun showSnackbar(s: String) {
        val snack = Snackbar.make(findViewById(android.R.id.content),
            s,
            Snackbar.LENGTH_LONG)
        SnackbarHelper.configSnackbar(this,snack)
        snack.show()
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
