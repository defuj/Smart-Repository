package id.deadlock.smartrepository.activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.dataCache
import java.io.File

class ActivityUpload : AppCompatActivity() {
    private var cache : SharedPreferences? = null
    private var toolbar: Toolbar? = null
    private var uploadBefore : LinearLayout? = null
    private var uploadAfter : LinearLayout? = null
    private var hapusUpload : TextView? = null
    private var layoutAbstrak : LinearLayout? = null
    private var btnJenisArtikel : TextView? = null
    private var spinnerJenisKaryaTulis : Spinner? = null
    private var namaFile : TextView? = null
    private var ukuranFile : TextView? = null

    private var jenisDokumen = "Artikel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        cache = getSharedPreferences(dataCache.CACHE,0)
        toolbar = findViewById(R.id.toolbarUpload)
        uploadBefore = findViewById(R.id.layoutUploadBefore)
        uploadAfter = findViewById(R.id.layoutUploadAfter)
        hapusUpload = findViewById(R.id.txtHapusFile)
        layoutAbstrak = findViewById(R.id.layoutAbstrak)
        btnJenisArtikel = findViewById(R.id.jenisArtikel)
        spinnerJenisKaryaTulis = findViewById(R.id.spinner_jenisArtikel)
        namaFile = findViewById(R.id.txtNamaFile)
        ukuranFile = findViewById(R.id.txtFileSize)

        runFunction()
    }

    private fun runFunction() {
        showJenisDokumen()

        toolbar!!.setNavigationOnClickListener {
            finish()
        }

        uploadBefore!!.setOnClickListener {
            //uploadBefore!!.visibility = View.GONE
            //uploadAfter!!.visibility = View.VISIBLE
            pilihFile()
        }

        hapusUpload!!.setOnClickListener {
            uploadBefore!!.visibility = View.VISIBLE
            uploadAfter!!.visibility = View.GONE
        }

        spinnerJenisKaryaTulis!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                jenisDokumen = spinnerJenisKaryaTulis!!.selectedItem.toString()
                if(spinnerJenisKaryaTulis!!.selectedItem.toString() == "Artikel"){
                    layoutAbstrak!!.visibility = View.VISIBLE
                }else{
                    layoutAbstrak!!.visibility = View.GONE
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }
    }

    private fun pilihFile() {
        val pilihDokumen = Intent()
            .setType("application/pdf")
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setAction(Intent.ACTION_OPEN_DOCUMENT)
        startActivityForResult(Intent.createChooser(pilihDokumen, "Pilih File"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && data != null){
            val uri : Uri = data.data!!
            val file = File(uri.path.toString())
            val fileInputStream = applicationContext.contentResolver.openInputStream(uri)
            val fileSize = "${fileInputStream!!.available()} byte" //file.length()/1024

            namaFile!!.text = file.name
            ukuranFile!!.text = fileSize

            uploadBefore!!.visibility = View.GONE
            uploadAfter!!.visibility = View.VISIBLE
        }
    }

    private fun showJenisDokumen() {
        val spinnerArray = ArrayList<String>()
        spinnerArray.clear()
        if(cache!!.getString(dataCache.akses,"user") == "admin"){
            spinnerArray.add("Artikel")
            spinnerArray.add("Skripsi")
            spinnerArray.add("Tugas Akhir")
        }else{
            spinnerArray.add("Artikel")
        }

        val adapter = ArrayAdapter(this@ActivityUpload,
            android.R.layout.simple_spinner_item, spinnerArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerJenisKaryaTulis!!.adapter = adapter
    }

    override fun onBackPressed() {
        finish()
    }
}
