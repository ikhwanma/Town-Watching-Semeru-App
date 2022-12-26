package com.ikhwan.townwatchingsemeru.presentation.profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ikhwan.townwatchingsemeru.presentation.profile.my_like.MyLikeFragment
import com.ikhwan.townwatchingsemeru.presentation.profile.my_post.MyPostFragment

class ProfilePagerAdapter(val fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MyPostFragment()
            1 -> MyLikeFragment()
            else -> MyPostFragment()
        }
    }
}