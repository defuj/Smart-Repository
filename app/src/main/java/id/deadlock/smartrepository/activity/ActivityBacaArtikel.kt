package id.deadlock.smartrepository.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import id.deadlock.smartrepository.R

class ActivityBacaArtikel : AppCompatActivity() {
    private var toolbar : Toolbar? = null
    private var txtJudul : TextView? = null
    private var txtView : TextView? = null
    private var txtBaca : TextView? = null
    private var txtAbstrak : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baca_artikel)

        toolbar = findViewById(R.id.toolbarBaca)
        txtJudul = findViewById(R.id.txtJudulKaryaTulis)
        txtView = findViewById(R.id.txtJumlahView)
        txtBaca = findViewById(R.id.txtJumlahBaca)
        txtAbstrak = findViewById(R.id.txtAbstrak)

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
