package id.deadlock.smartrepository.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import id.deadlock.smartrepository.R

class ActivityCari : AppCompatActivity() {
    private var toolbar : Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cari)
        toolbar = findViewById(R.id.toolbarCari)

        runFunction()
    }

    private fun runFunction() {
        toolbar!!.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
