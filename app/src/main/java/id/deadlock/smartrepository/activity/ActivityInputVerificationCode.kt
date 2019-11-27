package id.deadlock.smartrepository.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import id.deadlock.smartrepository.R

class ActivityInputVerificationCode : AppCompatActivity() {
    private var close : ImageButton? = null
    private var sign : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_verification_code)

        initial()
    }

    private fun initial() {
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
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityInputVerificationCode,ActivityHome::class.java))
    }
}
