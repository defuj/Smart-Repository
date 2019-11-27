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

        object : CountDownTimer(2000,1000){
            override fun onFinish() {
                pd!!.dismiss()
                if(username!!.text.toString() == "user" && password!!.text.toString() == "user"){
                    val editor = cache!!.edit()
                    editor.putBoolean(dataCache.show_login,false)
                    editor.putBoolean(dataCache.logged,true)
                    editor.putBoolean(dataCache.status_akun,true)
                    editor.putString(dataCache.username,username!!.text.toString())
                    editor.putString(dataCache.email,"dumy@domain.com")
                    editor.putString(dataCache.nama,"dumy name")
                    editor.putString(dataCache.password,password!!.text.toString())
                    editor.putString(dataCache.akses,"user")
                    editor.apply()
                    startActivity(Intent(this@ActivitySign,ActivityHome::class.java))
                }else if(username!!.text.toString() == "admin" && password!!.text.toString() == "admin"){
                    val editor = cache!!.edit()
                    editor.putBoolean(dataCache.show_login,false)
                    editor.putBoolean(dataCache.logged,true)
                    editor.putBoolean(dataCache.status_akun,true)
                    editor.putString(dataCache.username,username!!.text.toString())
                    editor.putString(dataCache.email,"dumy@domain.com")
                    editor.putString(dataCache.nama,"dumy name")
                    editor.putString(dataCache.password,password!!.text.toString())
                    editor.putString(dataCache.akses,"admin")
                    editor.apply()
                    startActivity(Intent(this@ActivitySign,ActivityHome::class.java))
                }else{
                    showSnackbar("NIM/NIDN Tidak Terdaftar.")
                }
            }

            override fun onTick(millisUntilFinished: Long) {

            }
        }.start()
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
