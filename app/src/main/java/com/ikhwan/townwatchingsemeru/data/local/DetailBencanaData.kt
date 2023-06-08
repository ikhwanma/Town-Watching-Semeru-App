package com.ikhwan.townwatchingsemeru.data.local

import com.ikhwan.townwatchingsemeru.R

data class DetailBencanaData(
    val id: Int,
    val desc: List<DetailBencanaDescription>,
)

data class DetailBencanaDescription(
    val desc: String,
    val img: Int
)

fun getPraBencanaData(): List<DetailBencanaData> {
    return listOf(
        DetailBencanaData(
            1,
            listOf(
                DetailBencanaDescription("Memperhatikan arahan Pusat Vulkanologi dan Mitigasi Bencana Geologi (PVMBG) terkait dengan perkembangan aktivitas gunungapi.", R.drawable.ic_volcano),
                DetailBencanaDescription("Persiapkan masker dan kacamata pelindung untuk mengantisipasi debu vulkanik.",R.drawable.ic_mask),
                DetailBencanaDescription("Mengetahui jalur evakuasi dan shelter yang telah disiapkan oleh pihak berwenang.",R.drawable.ic_evakuasi),
                DetailBencanaDescription("Mempersiapkan skenario evakuasi lain apabila dampak letusan meluas di luar prediksi ahli.", R.drawable.ic_waypoint),
                DetailBencanaDescription("Persiapkan dukungan logistik.", R.drawable.ic_food),
            )
        ),
        DetailBencanaData(
            2,
            listOf(
                DetailBencanaDescription("Menyiapkan rencana untuk penyelamatan diri apabila gempa bumi terjadi.",R.drawable.ic_evacuation),
                DetailBencanaDescription("Melakukan latihan yang dapat bermanfaat dalam menghadapi reruntuhan saat gempa bumi, seperti merunduk, perlindungan terhadap kepala, berpegangan ataupun dengan bersembunyi di bawah meja.",R.drawable.ic_table_cover),
                DetailBencanaDescription("Menyiapkan alat pemadam kebakaran, alat keselamatan standar dan persediaan obat-obatan.",R.drawable.ic_medkit),
                DetailBencanaDescription("Membangun konstruksi rumah yang tahan terhadap guncangan gempa bumi dengan fondasi yang kuat. Selain itu, anda bisa merenovasi baguan bangunan yang sudah rentan.", R.drawable.ic_construction),
                DetailBencanaDescription("Memperhatikan daerah rawan gempa bumi dan aturan seputar pengguna lahan yang di keluarkan oleh pemerintah.",R.drawable.ic_warning),
            )
        ),
        DetailBencanaData(
            3,
            listOf(
                DetailBencanaDescription("Waspada terhadap curah hujan yang tinggi.",R.drawable.ic_rain),
                DetailBencanaDescription("Persiapkan dukungan logistik.",R.drawable.ic_food),
                DetailBencanaDescription("Simak informasi dari media mengenai informasi hujan dan kemungkinan tanah longsor.",R.drawable.ic_television),
                DetailBencanaDescription("Apabila pihak berwenang menginstruksikan untuk evakuasi, segera lakukan hal tersebut.",R.drawable.ic_exit)
            )
        ),
        DetailBencanaData(
            4,
            listOf(
                DetailBencanaDescription("Perhatikan ketinggian rumah Anda dari bangunan yang rawan banjir.",R.drawable.ic_house),
                DetailBencanaDescription("Tinggikan panel listrik",R.drawable.ic_electrical),
                DetailBencanaDescription("Hubungi pihak berwenang apabila akan dibangun dinding penghalang di sekitar wilayah Anda.",R.drawable.ic_emergency_call),
            )
        ),
        DetailBencanaData(
            5,
            listOf(
                DetailBencanaDescription("Dengar dan simaklah siaran televisi menyangkut prakiraan terkini cuaca setempat", R.drawable.ic_television),
                DetailBencanaDescription("Waspadalah terhadap perubahan cuaca", R.drawable.ic_rain),
                DetailBencanaDescription("Waspadalah terhadap angin yang mendekat.", R.drawable.ic_wind),
                DetailBencanaDescription("Bersiaplah untuk ke tempat perlindungan ( bunker ) bila ada angin topan mendekat.", R.drawable.ic_bunker),
                DetailBencanaDescription("Waspadalah terhadap tanda tanda bahaya sebagai berikut:", R.drawable.ic_warning),
                DetailBencanaDescription("Langit gelap, sering berwarna kehijauan.", R.drawable.ic_dark_cloud),
                DetailBencanaDescription("Hujan es dengan butiran besar", R.drawable.ic_hail),
                DetailBencanaDescription("Awan rendah, hitam, besar, seringkali bergerak berputar", R.drawable.ic_dark_cloud),
                DetailBencanaDescription("Suara keras seperti bunyi kereta api cepat", R.drawable.ic_volume),
            )
        )
    )
}

fun getSaatBencana(): List<DetailBencanaData> {
    return listOf(
        DetailBencanaData(
            1,
            listOf(
                DetailBencanaDescription("Pastikan anda sudah berada di shelter atau tempat lain yang aman dari dampak letusan.",R.drawable.ic_evakuasi),
                DetailBencanaDescription("Gunakan masker dan kacamata pelindung.",R.drawable.ic_mask),
                DetailBencanaDescription("Selalu memperhatikan arahan dari pihak berwenang selama berada di shelter.",R.drawable.ic_planning),
            )
        ),
        DetailBencanaData(
            2,
            listOf(
                DetailBencanaDescription("Guncangan akan terasa beberapa saat, selama jangka waktu itu, upayakan keselamatan diri anda dengan cara berlindung di bawah meja untuk menghindari dari benda-benda yang mungkin jatuh dan jendela kaca.",R.drawable.ic_table_cover),
                DetailBencanaDescription("Lindungi kepala dengan bantal atau helm, atau berdirilah di bawah pintu. Bila sudah terasa aman, segera lari ke luar rumah.",R.drawable.ic_helm),
                DetailBencanaDescription("Jika anda sedang memasak, segera matikan kompor serta mencabut dan mematikan semua peralatan yang menggunakan listrik untuk mencegah terjadinya kebakaran.",R.drawable.ic_electrical),
                DetailBencanaDescription("Bila keluar rumah, perhatikan kemungkinan pecahan kaca, genteng atau material lain. Tetap lindungi kepala anda dan segera menuju ke lapangan terbuka.",R.drawable.ic_building),
                DetailBencanaDescription("Jangan berdiri di dekat tiang, pohon atau sumber listrik atai gedung yang mungkin roboh.",R.drawable.ic_tree),
                DetailBencanaDescription("Jangan gunakan lift apabila sudah terasa guncangan, gunakalah tangga darurat untuk evakuasi keluar bangunan. Apabila sudah di dalam elevator, tekan semua tombol atau gunakan interphone untuk panggilan kepada pengelola gedung.",R.drawable.ic_emergency_stair),
                DetailBencanaDescription("Kenali bagian bangunan yang memiliki struktur kuat, seperti pada sudut bangunan.",R.drawable.ic_corner),
                DetailBencanaDescription("Apabila anda berada di dalam bangunan yang memiliki petugas keamanan dan ikuti instruksi evakuasi.",R.drawable.ic_planning),
            )
        ),
        DetailBencanaData(
            3,
            listOf(
                DetailBencanaDescription("Apabila Anda di dalam rumah dan terdengar suara gemuruh, segera ke luar cari tempat lapang dan tanpa penghalang",R.drawable.ic_field),
                DetailBencanaDescription("Apabila Anda di luar, cari tempat yang lapang dan perhatikan sisi tebih atau tanah yang mengalami longsor.",R.drawable.ic_landslide)
            )
        ),
        DetailBencanaData(
            4,
            listOf(
                DetailBencanaDescription("Simak informasi mengenai informasi banjir.",R.drawable.ic_television),
                DetailBencanaDescription("Waspada terhadap banjir yang akan melanda. Apabila terjadi banjir bandang, beranjak segera ke tempat yang lebih tinggi; jangan menunggu instruksi terkait arahan beranjak.",R.drawable.ic_roof),
                DetailBencanaDescription("Waspada terhadap arus bawah, saluran air, kubangan, dan tempat-tempat lain yang tergenang air. Banjir bandang dapat terjadi di tempat ini dengan atau tanpa peringatan pada saat hujan biasa atau deras.",R.drawable.ic_puddle),
                DetailBencanaDescription("Amankan rumah Anda. Apabila masih tersedia waktu, tempatkan perabot di luar rumah. Barang yang lebih berharga diletakan pada bagian yang lebih tinggi di dalam rumah.",R.drawable.ic_diamond),
                DetailBencanaDescription("Matikan semua jaringan listrik apabila ada instruksi dari pihak berwenang. Cabut alat-alat yang masih tersambung dengan listrik. Jangan menyentuh peralatan yang bermuatan listrik apabila Anda berdiri di atas air.",R.drawable.ic_electrical),
                DetailBencanaDescription("Jangan berjalan di arus air. Beberapa langkah berjalan di arus air dapat mengakibatkan Anda jatuh.",R.drawable.ic_boot),
                DetailBencanaDescription("Apabila Anda harus berjalan di air, berjalanlah pada pijakan yang tidak bergerak. Gunakan tongkat atau sejenisnya untuk mengecek kepadatan tempat Anda berpijak.",R.drawable.ic_stick),
                DetailBencanaDescription("Jangan mengemudikan mobil di wilayah banjir. Apabila air mulai naik, abaikan mobil dan keluarlah ke tempat yang lebih tinggi. Apabila hal ini tidak dilakukan, Anda dan mobil dapat tersapu arus banjir dengan cepat.",R.drawable.ic_forbidden),
            )
        ),
        DetailBencanaData(
            5,
            listOf(
                DetailBencanaDescription("Bila dalam keadaan bahaya segeralah ke tempat perlindungan (bunker)",R.drawable.ic_bunker),
                DetailBencanaDescription("Jika anda berada di dalam bangunan seperti rumah, gedung perkantoran, sekolah, rumah sakit, pabrik, pusat perbelanjaan, gedung pencakar langit, maka yang anda harus lakukan adalah segera menuju ke ruangan yang telah dipersiapkan untuk menghadapi keadaan tersebut seperti sebuah ruangan yang dianggap paling aman, basement, ruangan anti badai, atau di tingkat lantai yang paling bawah. Bila tidak terdapat basement, segeralah ke tengah tengah ruangan pada lantai terbawah, jauhilah sudut sudut ruangan, jendela, pintu, dan dinding terluar bangunan. Semakin banyak sekat dinding antara diri anda dengan dinding terluar gedung semakin aman. Berlindunglah di bawah meja gunakan lengan anda untuk melindungi kepala dan leher anda. Jangan pernah membuka jendela.",R.drawable.ic_basement),
                DetailBencanaDescription("Jika anda berada di dalam kendaraan bermobil, segeralah hentikan dan tinggalkan kendaraan anda serta carilah tempat perlindungan yang terdekat seperti yang telah disebutkan di atas.",R.drawable.ic_forbidden),
            )
        )
    )
}

fun getPascaBencana(): List<DetailBencanaData> {
    return listOf(
        DetailBencanaData(
            1,
            listOf(
                DetailBencanaDescription("Tetap gunakan master dan kacamata pelindung ketika berada di wilayah yang terdampak abu vulkanik.",R.drawable.ic_mask),
                DetailBencanaDescription("Memperhatikan perkembangan informasi dari pihak berwenang melalui televisi atau pengumuman dari pihak berwenang.",R.drawable.ic_television),
                DetailBencanaDescription("Waspada terhadap kemungkinan bahaya kedua atau secondary hazard berupa banjir lahar dingin. Bencana ini dipicu oleh curah hujan tinggi dan menghanyutkan material vulkanik maupun reruntuhan kayu atau apapun sepanjang sungai dari hilir ke hulu. Perhatikan bentangan kiri dan kanan dari titik sungai mengantisipasi luapan banjir lahar dingin.",R.drawable.ic_warning),
            )
        ),
        DetailBencanaData(
            2,
            listOf(
                DetailBencanaDescription("Tetap waspada terhadap gempa bumi susulan.",R.drawable.ic_warning),
                DetailBencanaDescription("Ketika berada di dalam bangunan, evakuasi diri anda setelah gempa bumi berhenti. Perhatikan reruntuhan maupun benda-benda yang membahayakan pada saat evakuasi.",R.drawable.ic_landslide),
                DetailBencanaDescription("Jika berada di dalam rumah, tetap berada di bawah meja yang kuat.",R.drawable.ic_table_cover),
                DetailBencanaDescription("Periksa keberadaan api dan potensi terjadinya bencana kebakaran.",R.drawable.ic_fire),
                DetailBencanaDescription("Berdirilah di tempat terbuka jauh dari gedung dan instalasi listrik dan air. Apabila di luar bangunan dengan tebing di sekeliling, hindari daerah yang rawan longsor.",R.drawable.ic_field),
                DetailBencanaDescription("Jika di dalam mobil, berhentilah di pinggir jalan, tetapi tetap berada di dalam mobil. Hindari berhenti di bawah atau di atas jembatan atau rambu-rambu lalu lintas.",R.drawable.ic_driver),
            )
        ),
        DetailBencanaData(
            3,
            listOf(
                DetailBencanaDescription("Jangan segera kembali ke rumah Anda, perhatikan apakah longsor susulan masih akan terjadi.",R.drawable.ic_warning),
                DetailBencanaDescription("Apabila Anda diminta untuk membantu proses evakuasi, gunakan sepatu khusus dan peralatan yang menjamin keselamatan Anda.",R.drawable.ic_boots),
                DetailBencanaDescription("Perhatikan kondisi tanah sebagai pijakan yang kokoh bagi langkah Anda.",R.drawable.ic_ground),
            )
        ),
        DetailBencanaData(
            4,
            listOf(
                DetailBencanaDescription("Secepatnya membersihkan rumah dan halaman dari sisa air banjir, lumpur dan sampah.",R.drawable.ic_mop),
                DetailBencanaDescription("Waspada terhadap kemungkinan binatang berbisa seperti ular, lipan, hingga nyamuk yang terbawa aliran banjir.",R.drawable.ic_snake),
                DetailBencanaDescription("Segera gunakan persediaan air bersih untuk mengurangi risiko diare.",R.drawable.ic_quality),
                DetailBencanaDescription("Gunakan antiseptik untuk membunuh kuman-kuman penyakit",R.drawable.ic_cleaning_spray),
                DetailBencanaDescription("Selalu waspada terhadap potensi banjir susulan.",R.drawable.ic_warning),
                DetailBencanaDescription("Waspada terhadap aliran listrik dan gas yang ada di dalam rumah.", R.drawable.ic_electrical),
            )
        ),
        DetailBencanaData(
            5,
            listOf(
                DetailBencanaDescription("Segera periksa keselamatan orang-orang terdekat.", R.drawable.ic_helping),
                DetailBencanaDescription("Apabila ada dari orang sekitar yang ditemukan dalam kondisi terluka atau membutuhkan pertolongan, segera berikan bantuan darurat", R.drawable.ic_medicine),
                DetailBencanaDescription("Periksa segala sambungan baik listrik, gas, dan sebagainya, jika ada kerusakan segera lapor ke pihak yang berwenang.", R.drawable.ic_electrical),
            )
        )
    )
}