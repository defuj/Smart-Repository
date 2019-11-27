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

        object : CountDownTimer(2000,1000){
            override fun onFinish() {
                val editor = cache!!.edit()
                editor.putBoolean(dataCache.show_login,false)
                editor.putBoolean(dataCache.logged,true)
                editor.putBoolean(dataCache.status_akun,false)
                editor.putString(dataCache.username,nomorInduk!!.text.toString())
                editor.putString(dataCache.email,email!!.text.toString())
                editor.putString(dataCache.nama,namaLengkap!!.text.toString())
                editor.putString(dataCache.password,sandi!!.text.toString())
                editor.putString(dataCache.akses,"user")
                editor.apply()

                pd!!.dismiss()
                startActivity(
                    Intent(this@ActivitySignUp,
                        ActivityInputVerificationCode::class.java)
                )
            }

            override fun onTick(millisUntilFinished: Long) {

            }
        }.start()
    }
}
