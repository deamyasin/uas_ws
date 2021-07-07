package com.foodorder.ui.home.adapter

import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.foodorder.models.BannerModel
import com.foodorder.models.CategoryModel
import com.foodorder.ui.home.fragment.HomeMenuFragment

internal class HomeMenuFragmentAdapter(fm: FragmentManager,
                                       val categoryModelList: ArrayList<BannerModel>,
                                       val isRtl: Boolean) : FragmentStatePagerAdapter(fm) {

    companion object {
        val LOOPS_COUNT = 4000
    }

    init {
        Log.e("pagers:", "" + categoryModelList.size)
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        val index = position % categoryModelList.size
        return HomeMenuFragment().newInstance(index, categoryModelList)
    }

    override fun getCount(): Int {
        return Integer.MAX_VALUE//LOOPS_COUNT //Integer.MAX_VALUE//sliderModelList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        try {
            super.restoreState(state, loader)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun saveState(): Parcelable? {
        return null
    }

}