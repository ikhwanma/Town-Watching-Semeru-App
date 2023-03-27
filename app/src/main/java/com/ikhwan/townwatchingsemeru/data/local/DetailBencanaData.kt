package com.ikhwan.townwatchingsemeru.data.local

data class DetailBencanaData(
    val id: Int,
    val desc: List<String>
)

fun getPraBencanaData(): List<DetailBencanaData> {
    return listOf(
        DetailBencanaData(
            1,
            listOf(
                "Memperhatikan arahan Pusat Vulkanologi dan Mitigasi Bencana Geologi (PVMBG) terkait dengan perkembangan aktivitas gunungapi.",
                "Persiapkan masker dan kacamata pelindung untuk mengantisipasi debu vulkanik.",
                "Mengetahui jalur evakuasi dan shelter yang telah disiapkan oleh pihak berwenang.",
                "Mempersiapkan skenario evakuasi lain apabila dampak letusan meluas di luar prediksi ahli.",
                "Persiapkan dukungan logistik."
            )
        ),
        DetailBencanaData(
            2,
            listOf(
                "Menyiapkan rencana untuk penyelamatan diri apabila gempa bumi terjadi.",
                "Melakukan latihan yang dapat bermanfaat dalam menghadapi reruntuhan saat gempa bumi, seperti merunduk, perlindungan terhadap kepala, berpegangan ataupun dengan bersembunyi di bawah meja.",
                "Menyiapkan alat pemadam kebakaran, alat keselamatan standar dan persediaan obat-obatan.",
                "Membangun konstruksi rumah yang tahan terhadap guncangan gempa bumi dengan fondasi yang kuat. Selain itu, anda bisa merenovasi baguan bangunan yang sudah rentan.",
                "Memperhatikan daerah rawan gempa bumi dan aturan seputar pengguna lahan yang di keluarkan oleh pemerintah."
            )
        ),
        DetailBencanaData(
            3,
            listOf(
                "Waspada terhadap curah hujan yang tinggi.",
                "Persiapkan dukungan logistik.",
                "Simak informasi dari radio mengenai informasi hujan dan kemungkinan tanah longsor.",
                "Apabila pihak berwenang menginstruksikan untuk evakuasi, segera lakukan hal tersebut."
            )
        ),
        DetailBencanaData(
            4,
            listOf(
                "Perhatikan ketinggian rumah Anda dari bangunan yang rawan banjir;",
                "Tinggikan panel listrik",
                "Hubungi pihak berwenang apabila akan dibangun dinding penghalang di sekitar wilayah Anda.",
            )
        ),
    )
}

fun getSaatBencana(): List<DetailBencanaData> {
    return listOf(
        DetailBencanaData(
            1,
            listOf(
                "Pastikan anda sudah berada di shelter atau tempat lain yang aman dari dampak letusan.",
                "Gunakan masker dan kacamata pelindung.",
                "Selalu memperhatikan arahan dari pihak berwenang selama berada di shelter.",
            )
        ),
        DetailBencanaData(
            2,
            listOf(
                "Guncangan akan terasa beberapa saat, selama jangka waktu itu, upayakan keselamatan diri anda dengan cara berlindung di bawah meja untuk menghindari dari benda-benda yang mungkin jatuh dan jendela kaca.",
                "Lindungi kepala dengan bantal atau helm, atau berdirilah di bawah pintu. Bila sudah terasa aman, segera lari ke luar rumah.",
                "Jika anda sedang memasak, segera matikan kompor serta mencabut dan mematikan semua peralatan yang menggunakan listrik untuk mencegah terjadinya kebakaran.",
                "Bila keluar rumah, perhatikan kemungkinan pecahan kaca, genteng atau material lain. Tetap lindungi kepala anda dan segera menuju ke lapangan terbuka.",
                "Jangan berdiri di dekat tiang, pohon atau sumber listrik atai gedung yang mungkin roboh.",
                "Jangan gunakan lift apabila sudah terasa guncangan, gunakalah tangga darurat untuk evakuasi keluar bangunan. Apabila sudah di dalam elevator, tekan semua tombol atau gunakan interphone untuk panggilan kepada pengelola gedung.",
                "Kenali bagian bangunan yang memiliki struktur kuat, seperti pada sudut bangunan.",
                "Apabila anda berada di dalam bangunan yang memiliki petugas keamanan dan ikuti instruksi evakuasi."
            )
        ),
        DetailBencanaData(
            3,
            listOf(
                "Apabila Anda di dalam rumah dan terdengar suara gemuruh, segera ke luar cari tempat lapang dan tanpa penghalang",
                "Apabila Anda di luar, cari tempat yang lapang dan perhatikan sisi tebih atau tanah yang mengalami longsor.",
            )
        ),
        DetailBencanaData(
            4,
            listOf(
                "Simak informasi mengenai informasi banjir.",
                "Waspada terhadap banjir yang akan melanda. Apabila terjadi banjir bandang, beranjak segera ke tempat yang lebih tinggi; jangan menunggu instruksi terkait arahan beranjak.",
                "Waspada terhadap arus bawah, saluran air, kubangan, dan tempat-tempat lain yang tergenang air. Banjir bandang dapat terjadi di tempat ini dengan atau tanpa peringatan pada saat hujan biasa atau deras.",
                "Amankan rumah Anda. Apabila masih tersedia waktu, tempatkan perabot di luar rumah. Barang yang lebih berharga diletakan pada bagian yang lebih tinggi di dalam rumah.",
                "Matikan semua jaringan listrik apabila ada instruksi dari pihak berwenang. Cabut alat-alat yang masih tersambung dengan listrik. Jangan menyentuh peralatan yang bermuatan listrik apabila Anda berdiri di atas air.",
                "Jangan berjalan di arus air. Beberapa langkah berjalan di arus air dapat mengakibatkan Anda jatuh.",
                "Apabila Anda harus berjalan di air, berjalanlah pada pijakan yang tidak bergerak. Gunakan tongkat atau sejenisnya untuk mengecek kepadatan tempat Anda berpijak.",
                "Jangan mengemudikan mobil di wilayah banjir. Apabila air mulai naik, abaikan mobil dan keluarlah ke tempat yang lebih tinggi. Apabila hal ini tidak dilakukan, Anda dan mobil dapat tersapu arus banjir dengan cepat."
            )
        ),
    )
}

fun getPascaBencana(): List<DetailBencanaData> {
    return listOf(
        DetailBencanaData(
            1,
            listOf(
                "Apabila Anda dan keluarga harus tinggal lebih lama di shelter, pastikan kebutuhan dasar terpenuhi dan pendampingan khusus bagi anak-anak dan remaja diberikan. Dukungan orangtua yang bekerjasama dengan organisasi kemanusiaan dalam pendampingan anak-anak dan remaja sangat penting untuk mengurangi stres atau ketertekanan selama di shelter.",
                "Tetap gunakan master dan kacamata pelindung ketika berada di wilayah yang terdampak abu vulkanik.",
                "Memperhatikan perkembangan informasi dari pihak berwenang melalui radio atau pengumuman dari pihak berwenang.",
                "Waspada terhadap kemungkinan bahaya kedua atau secondary hazard berupa banjir lahar dingin. Bencana ini dipicu oleh curah hujan tinggi dan menghanyutkan material vulkanik maupun reruntuhan kayu atau apapun sepanjang sungai dari hilir ke hulu. Perhatikan bentangan kiri dan kanan dari titik sungai mengantisipasi luapan banjir lahar dingin."
            )
        ),
        DetailBencanaData(
            2,
            listOf(
                "Tetap waspada terhadap gempa bumi susulan.",
                "Ketika berada di dalam bangunan, evakuasi diri anda setelah gempa bumi berhenti. Perhatikan reruntuhan maupun benda-benda yang membahayakan pada saat evakuasi.",
                "Jika berada di dalam rumah, tetap berada di bawah meja yang kuat.",
                "Periksa keberadaan api dan potensi terjadinya bencana kebakaran.",
                "Berdirilah di tempat terbuka jauh dari gedung dan instalasi listrik dan air. Apabila di luar bangunan dengan tebing di sekeliling, hindari daerah yang rawan longsor.",
                "Jika di dalam mobil, berhentilah di pinggir jalan, tetapi tetap berada di dalam mobil. Hindari berhenti di bawah atau di atas jembatan atau rambu-rambu lalu lintas.",
            )
        ),
        DetailBencanaData(
            3,
            listOf(
                "Jangan segera kembali ke rumah Anda, perhatikan apakah longsor susulan masih akan terjadi.",
                "Apabila Anda diminta untuk membantu proses evakuasi, gunakan sepatu khusus dan peralatan yang menjamin keselamatan Anda.",
                "Perhatikan kondisi tanah sebagai pijakan yang kokoh bagi langkah Anda.",
                "Apabila harus menghadapi reruntuhan bangunan untuk menyelamatkan korban, pastikan tidak menimbulkan dampak yang lebih buruk atau menunggu pihak berwenang untuk melakukan evakuasi korban."
            )
        ),
        DetailBencanaData(
            4,
            listOf(
                "Secepatnya membersihkan rumah dan halaman dari sisa air banjir, lumpur dan sampah.",
                "Waspada terhadap kemungkinan binatang berbisa seperti ular, lipan, hingga nyamuk yang terbawa aliran banjir.",
                "Segera gunakan persediaan air bersih untuk mengurangi risiko diare.",
                "Gunakan antiseptik untuk membunuh kuman-kuman penyakit",
                "Selalu waspada terhadap potensi banjir susulan.",
                "Waspada terhadap aliran listrik dan gas yang ada di dalam rumah."
            )
        ),
    )
}