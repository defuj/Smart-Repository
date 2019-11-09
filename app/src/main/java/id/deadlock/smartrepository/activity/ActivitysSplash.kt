package id.deadlock.smartrepository.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.dataCache

class ActivitysSplash : AppCompatActivity() {
    private var cache : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        cache = getSharedPreferences(dataCache.CACHE,0)

        object : CountDownTimer(3000,1000){
            override fun onFinish() {
                if(cache!!.getBoolean(dataCache.logged,false)){
                    startActivity(Intent(this@ActivitysSplash,ActivityHome::class.java))
                }else{
                    startActivity(Intent(this@ActivitysSplash,ActivitySign::class.java))
                }
            }

            override fun onTick(millisUntilFinished: Long) {

            }
        }.start()
    }
}
