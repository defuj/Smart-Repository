package id.deadlock.smartrepository.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.dataCache

class ActivitySplash : AppCompatActivity() {
    private var cache : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        cache = getSharedPreferences(dataCache.CACHE,0)

        object : CountDownTimer(2000,1000){
            override fun onFinish() {
                if(cache!!.getBoolean(dataCache.show_login,true)){
                    startActivity(Intent(this@ActivitySplash,ActivitySign::class.java))
                }else{
                    startActivity(Intent(this@ActivitySplash,ActivityHome::class.java))
                }
            }

            override fun onTick(millisUntilFinished: Long) {

            }
        }.start()
    }
}
