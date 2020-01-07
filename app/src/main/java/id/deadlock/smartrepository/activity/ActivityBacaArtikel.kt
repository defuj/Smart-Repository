package id.deadlock.smartrepository.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.dataCache
import id.deadlock.smartrepository.network.ApiServices
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ActivityBacaArtikel : AppCompatActivity() {
    private var toolbar : Toolbar? = null
    private var txtJudul : TextView? = null
    private var txtView : TextView? = null
    private var txtBaca : TextView? = null
    private var txtAbstrak : TextView? = null
    private var txtTgl : TextView? = null
    private var layoutAbstrak : LinearLayout? = null
    private var addToFavorite : FloatingActionButton? = null
    private var cache : SharedPreferences? = null
    private var baca : Button? = null
    private var pdfView : PDFView? = null
    private var btnAddFavorite : ImageButton? = null
    private var DOCUMENTREADY: Boolean = false
    private var DOCUMENTFILE : String? = null
    private var DOCUMENTNAME : String? = null

    //private var penulisList : List<Chip>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baca_artikel)
        val config : PRDownloaderConfig = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .build()
        PRDownloader.initialize(applicationContext,config)

        cache = getSharedPreferences(dataCache.CACHE,0)
        pdfView = findViewById(R.id.pdfView)

        addToFavorite = findViewById(R.id.floatingActionButtonFavorite)
        toolbar = findViewById(R.id.toolbarBaca)
        txtJudul = findViewById(R.id.txtJudulKaryaTulis)
        txtView = findViewById(R.id.txtJumlahView)
        txtBaca = findViewById(R.id.txtJumlahBaca)
        txtAbstrak = findViewById(R.id.txtAbstrak)
        layoutAbstrak = findViewById(R.id.layoutAbstrakArtikel)
        txtTgl = findViewById(R.id.txtTanggalRilis)
        baca = findViewById(R.id.btnBacaLengkap)
        layoutAbstrak!!.visibility = View.GONE
        btnAddFavorite = findViewById(R.id.imgButtonFavorit)

        runFunction()
    }

    @SuppressLint("SetTextI18n")
    private fun runFunction() {
        txtJudul!!.text = intent.getStringExtra("judul")
        txtView!!.text = "${intent.getStringExtra("views")}x"
        txtBaca!!.text = intent.getStringExtra("readers")

        if(intent.getStringExtra("jenis") == "Artikel"){
            getAbstrakAndWriter()
        }else{
            layoutAbstrak!!.visibility = View.GONE
        }

        val format : DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("in-ID"))
        val date : Date = format.parse(intent.getStringExtra("tgl_upload")!!)!!
        val formatter = SimpleDateFormat("MMMM yyyy", Locale("in-ID"))
        txtTgl!!.text = formatter.format(date)

        toolbar!!.setNavigationOnClickListener {
            finish()
        }

        toolbar!!.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_favorite-> {
                    if(cache!!.getBoolean(dataCache.logged,false)){
                        if(cache!!.getBoolean(dataCache.status_akun,false)){
                            addFavorite()
                        }else{
                            startActivity(Intent(this, ActivityInputVerificationCode::class.java))
                        }
                    }else{
                        Toast.makeText(this,"Harap untuk masuk ke Akun telebih dahulu.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            true
        }

        btnAddFavorite!!.setOnClickListener {
            if(cache!!.getBoolean(dataCache.logged,false)){
                if(cache!!.getBoolean(dataCache.status_akun,false)){
                    Toast.makeText(this,"Telah ditambahkan ke daftar Favorit.", Toast.LENGTH_SHORT).show()
                }else{
                    startActivity(Intent(this, ActivityInputVerificationCode::class.java))
                }
            }else{
                Toast.makeText(this,"Harap untuk masuk ke Akun telebih dahulu.", Toast.LENGTH_SHORT).show()
            }
        }

        baca!!.setOnClickListener {
            if(cache!!.getBoolean(dataCache.logged,false)){
                if(cache!!.getBoolean(dataCache.status_akun,false)){
                    if(DOCUMENTREADY){
                        if(!DOCUMENTFILE.isNullOrEmpty()){
                            bacaLengkap(DOCUMENTFILE!!,DOCUMENTNAME!!)
                        }else{
                            Toast.makeText(this,"Dokumen Tidak Tersedia.", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this,"Menyiapkan Dokumen...", Toast.LENGTH_SHORT).show()
                        unduhDokumen()
                    }
                }else{
                    startActivity(Intent(this, ActivityInputVerificationCode::class.java))
                }
            }else{
                Toast.makeText(this,"Harap untuk masuk ke Akun telebih dahulu.", Toast.LENGTH_SHORT).show()
            }
        }
        unduhDokumen()
        showFavorited()
    }

    private fun showFavorited() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiServices.URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api = retrofit.create(ApiServices::class.java)
        val call = api.addFavorite(
            "showFavorite",
            cache!!.getString(dataCache.username,"user")!!,
            intent.getStringExtra("id")!!)
        call.enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {

            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        val jsonresponse = response.body()
                        val jsonObject = JSONObject(jsonresponse!!)
                        if(jsonObject.optBoolean("result")){
                            toolbar!!.menu.getItem(0).icon.setColorFilter(resources.getColor(R.color.gradient1ColorCenter), PorterDuff.Mode.SRC_IN)
                        }
                    }
                }
            }

        })
    }

    private fun addFavorite() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiServices.URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api = retrofit.create(ApiServices::class.java)
        val call = api.addFavorite(
            "addFavorite",
            cache!!.getString(dataCache.username,"user")!!,
            intent.getStringExtra("id")!!)
        call.enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {

            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        val jsonresponse = response.body()
                        val jsonObject = JSONObject(jsonresponse!!)
                        if(jsonObject.optBoolean("result")){
                            toolbar!!.menu.getItem(0).icon.setColorFilter(resources.getColor(R.color.gradient1ColorCenter), PorterDuff.Mode.SRC_IN)
                            Toast.makeText(this@ActivityBacaArtikel,"Telah ditambahkan ke favorite",Toast.LENGTH_SHORT).show()
                        }else{
                            toolbar!!.menu.getItem(0).icon.setColorFilter(resources.getColor(R.color.black_overlay), PorterDuff.Mode.SRC_IN)
                            Toast.makeText(this@ActivityBacaArtikel,"Telah dihapus dari favorite",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        })
    }

    private fun unduhDokumen(){
        val uri = Uri.parse(intent.getStringExtra("source"))
        val fileName = uri.pathSegments.last()
        //val dirPath = filesDir.absolutePath+File.pathSeparator+"mnt/sdcard/SmartRepository"
        val dirPath = "$filesDir"

        if(!File(dirPath).exists()){
            File(dirPath).mkdirs()
        }

        if(File("$dirPath/$fileName").exists()){
            //Toast.makeText(this@ActivityBacaArtikel,"Document is Ready",Toast.LENGTH_SHORT).show()
            DOCUMENTREADY = true
            DOCUMENTFILE = "$dirPath/$fileName"
            DOCUMENTNAME = fileName
        }else{
            PRDownloader.download(intent.getStringExtra("source"), dirPath, fileName).build().start(object : OnDownloadListener{
                override fun onDownloadComplete() {
                    //Toast.makeText(this@ActivityBacaArtikel,"Document is Ready",Toast.LENGTH_SHORT).show()
                    DOCUMENTREADY = true
                    DOCUMENTFILE = "$dirPath/$fileName"
                    DOCUMENTNAME = fileName
                    //loadDokumen("$dirPath/$fileName")
                }

                override fun onError(error: Error?) {
                    DOCUMENTREADY = false
                    Toast.makeText(this@ActivityBacaArtikel,"Document error, sorry you can't read the document.",Toast.LENGTH_SHORT).show()
                    //Toast.makeText(this@ActivityBacaArtikel,"Failed load document,$error",Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun getAbstrakAndWriter(){
        //penulisList = ArrayList()

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiServices.URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api = retrofit.create(ApiServices::class.java)
        val call = api.detailKaryaTulis("abstrak_penulis",cache!!.getString(dataCache.username,"user").toString(),intent.getStringExtra("id")!!)
        call.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@ActivityBacaArtikel,"Jaringan internet bermasalah.",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        val jsonresponse = response.body()
                        try{
                            val jsonObject = JSONObject(jsonresponse!!)
                            txtAbstrak!!.text = jsonObject.optString("abstrak")
                            layoutAbstrak!!.visibility = View.VISIBLE
                            val dataArray = jsonObject.getJSONArray("penulis")
                            for (i in 0 until dataArray.length()) {
                                val dataobj = dataArray.getJSONObject(i)
                                //val modelPenulis = ModelListPenulis()
                                //modelPenulis.nama = dataobj.getString("nama")
                                //modelPenulis.nomor_induk = dataobj.getString("nomor_induk")
                                //penulisList

                                //list_nama_penulis.chipList = penulisList
                            }
                        }catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }else{
                        Toast.makeText(this@ActivityBacaArtikel,"Informasi tidak tersedia.",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@ActivityBacaArtikel,"Gagal mengambil data.",Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun bacaLengkap(path : String, filename : String){
        val idDoc : String = intent.getStringExtra("id")!!
        val intent = Intent(this,ActivityDetailArtikel::class.java)
        intent.putExtra("file",path)
        intent.putExtra("filename",filename)
        intent.putExtra("user",cache!!.getString(dataCache.username,"user"))
        intent.putExtra("id",idDoc)
        startActivity(intent)
    }

    override fun onBackPressed() {
        finish()
    }


}
