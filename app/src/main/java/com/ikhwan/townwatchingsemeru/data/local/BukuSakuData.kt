package com.ikhwan.townwatchingsemeru.data.local

import com.ikhwan.townwatchingsemeru.R

data class BukuSakuData(
    val id: Int,
    val category: String,
    val description: String,
    val icon: Int
)

fun getBukuSakuData(): List<BukuSakuData> {
    return listOf(
        BukuSakuData(
            1,
            "Erupsi Gunung Api",
            "Klik untuk menampilkan informasi siap siaga terhadap bencana erupsi gunung api",
            R.drawable.ic_erupsi
        ),
        BukuSakuData(
            2,
            "Gempa Bumi",
            "Klik untuk menampilkan informasi siap siaga terhadap bencana gempa bumi",
            R.drawable.ic_gempa_bumi
        ),
        BukuSakuData(
            3,
            "Tanah Longsor",
            "Klik untuk menampilkan informasi siap siaga terhadap bencana tanah longsor",
            R.drawable.ic_tanah_longsor
        ),
        BukuSakuData(
            4,
            "Banjir",
            "Klik untuk menampilkan informasi siap siaga terhadap bencana banjir",
            R.drawable.ic_banjir
        ),
        BukuSakuData(
            5,
            "Angin Kencang",
            "Klik untuk menampilkan informasi siap siaga terhadap bencana banjir",
            R.drawable.ic_angin_kencang
        )
    )
}