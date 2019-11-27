package id.deadlock.smartrepository.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import id.deadlock.smartrepository.fragment.*

class AdapterHome
constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val pages = listOf(
        FragmentHome(),
        FragmentHistory(),
        FragmentFavorite(),
        FragmentAkunUser(),
        FragmentAkunAdmin()
    )

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }
}