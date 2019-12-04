package id.deadlock.smartrepository.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import id.deadlock.smartrepository.R
import id.deadlock.smartrepository.adapter.AdapterHome
import id.deadlock.smartrepository.dataCache

class ActivityHome : AppCompatActivity() {
    private var bottomBar : BottomNavigationViewEx? = null
    private var viewPager : ViewPager? = null
    private var adapterHome : AdapterHome? = null
    private var cache : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initial()
    }

    private fun initial() {
        cache = getSharedPreferences(dataCache.CACHE,0)
        bottomBar = findViewById(R.id.bottom_bar)
        viewPager = findViewById(R.id.pagerHome)
        adapterHome = AdapterHome(supportFragmentManager)
        viewPager!!.adapter = adapterHome
        viewPager!!.offscreenPageLimit = 4

        runFunction()
    }

    private fun runFunction() {
        bottomBar!!.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> viewPager!!.setCurrentItem(0,false)
                R.id.menu_history -> viewPager!!.setCurrentItem(1,false)
                R.id.menu_favorite -> {
                    if(cache!!.getBoolean(dataCache.logged,false)){
                        if(cache!!.getBoolean(dataCache.status_akun,false)){
                            viewPager!!.setCurrentItem(2,false)
                        }else{
                            startActivity(Intent(this@ActivityHome,ActivityInputVerificationCode::class.java))
                        }
                    }else{
                        startActivity(Intent(this@ActivityHome,ActivitySign::class.java))
                    }
                }
                else -> {
                    if(cache!!.getBoolean(dataCache.logged,false)){
                        if(cache!!.getBoolean(dataCache.status_akun,false)){
                            if(cache!!.getString(dataCache.akses,"user") == "user"){
                                viewPager!!.setCurrentItem(3,false)
                            }else{
                                viewPager!!.setCurrentItem(4,false)
                            }
                        }else{
                            startActivity(Intent(this@ActivityHome,ActivityInputVerificationCode::class.java))
                        }
                    }else{
                        startActivity(Intent(this@ActivityHome,ActivitySign::class.java))
                    }
                }
            }
            true
        }
        //bottomBar!!.enableItemShiftingMode(false)
        //bottomBar!!.enableAnimation(false)
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
