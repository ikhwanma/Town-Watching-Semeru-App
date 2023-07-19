package com.ikhwan.townwatchingsemeru.presentation.buku_saku_detail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ikhwan.townwatchingsemeru.presentation.buku_saku_detail.deskripsi.DeskripsiFragment
import com.ikhwan.townwatchingsemeru.presentation.buku_saku_detail.pasca_bencana.PascaBencanaFragment
import com.ikhwan.townwatchingsemeru.presentation.buku_saku_detail.pra_bencana.PraBencanaFragment
import com.ikhwan.townwatchingsemeru.presentation.buku_saku_detail.saat_bencana.SaatBencanaFragment

class BukuSakuDetailPagerAdapter(val fragment: Fragment, val id: Int): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> DeskripsiFragment(id)
            1 -> PraBencanaFragment(id)
            2 -> SaatBencanaFragment(id)
            3 -> PascaBencanaFragment(id)
            else -> DeskripsiFragment(id)
        }
    }
}