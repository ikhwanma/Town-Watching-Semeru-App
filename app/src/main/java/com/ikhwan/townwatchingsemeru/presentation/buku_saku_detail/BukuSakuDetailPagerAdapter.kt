package com.ikhwan.townwatchingsemeru.presentation.buku_saku_detail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ikhwan.townwatchingsemeru.presentation.buku_saku_detail.pasca_bencana.PascaBencanaFragment
import com.ikhwan.townwatchingsemeru.presentation.buku_saku_detail.pra_bencana.PraBencanaFragment
import com.ikhwan.townwatchingsemeru.presentation.buku_saku_detail.saat_bencana.SaatBencanaFragment

class BukuSakuDetailPagerAdapter(val fragment: Fragment, val id: Int): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> PraBencanaFragment(id)
            1 -> SaatBencanaFragment(id)
            2 -> PascaBencanaFragment(id)
            else -> PraBencanaFragment(id)
        }
    }
}