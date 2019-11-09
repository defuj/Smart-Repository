package id.deadlock.smartrepository.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.activity.sign.ActivitySignIn
import id.deadlock.smartrepository.activity.sign.ActivitySignUp

class ActivitySign : AppCompatActivity() {
    private var signin : Button? = null
    private var signup : Button? = null
    private var close : ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        initial()
    }

    private fun initial() {
        close = findViewById(R.id.btnClose)
        signin = findViewById(R.id.btnMasuk)
        signup = findViewById(R.id.btnDaftar)

        runFunction()
    }

    private fun runFunction() {
        signin!!.setOnClickListener {
            startActivity(Intent(this,ActivitySignIn::class.java))
        }
        signup!!.setOnClickListener {
            startActivity(Intent(this,ActivitySignUp::class.java))
        }
        close!!.setOnClickListener {
            startActivity(Intent(this,ActivityHome::class.java))
        }
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
