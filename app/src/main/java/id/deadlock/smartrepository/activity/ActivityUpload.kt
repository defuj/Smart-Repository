package id.deadlock.smartrepository.activity

import android.app.Activity
import android.app.ProgressDialog
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
import id.deadlock.smartrepository.network.ApiServices
import kotlinx.android.synthetic.main.activity_upload.*
import net.gotev.uploadservice.MultipartUploadRequest
import net.gotev.uploadservice.UploadNotificationConfig
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

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
    private var pd: ProgressDialog? = null
    private var uriFile : Uri? = null
    private var jenisDokumen = "Artikel"

    private var PdfNameHolder : String? = null
    private var PdfPathHolder : String? = null
    private var PdfID : String? = null

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
            namaFile!!.text = ""
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

        btnUnggah!!.setOnClickListener {
            if(jenisDokumen.isNotEmpty()){
                if(namaFile!!.text.isNotEmpty()){
                    if(jenisDokumen == "Artikel"){
                        if(editAbstrak!!.text.isNotEmpty()){
                            //upload
                            uploadDokumen()
                        }else{
                            Toast.makeText(this,"Abstrak tidak boleh kosong.",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        //upload
                        uploadDokumen()
                    }
                }else{
                    Toast.makeText(this,"Harap pilih dokumen untuk diunggah.",Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(this,"Harap pilih jenis dokumen.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadDokumen() {
        pd = ProgressDialog(this, R.style.DialogTheme)
        pd!!.setCancelable(false)
        pd!!.show()

        PdfID = UUID.randomUUID().toString()
        //PdfPathHolder = FilePa
        val PDF_UPLOAD_HTTP_URL = "${ApiServices.URL}index.php?fun=upload"
        MultipartUploadRequest(this,PdfID,PDF_UPLOAD_HTTP_URL)
            .addFileToUpload("","pdf")
            .addParameter("","")
            .setNotificationConfig(UploadNotificationConfig())
            .setMaxRetries(5)
            .startUpload()
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
            uriFile = data.data!!

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
        if(cache!!.getString(dataCache.akses,"user") == "Admin"){
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
