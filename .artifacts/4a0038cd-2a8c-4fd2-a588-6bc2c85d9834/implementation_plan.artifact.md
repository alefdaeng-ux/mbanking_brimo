# Implementation Plan: Penyesuaian Halaman Login (Area di Atas Fast Menu)

Meremas dan menyesuaikan area di atas *Fast Menu* pada `activity_login.xml` agar 100% presisi dengan foto referensi aplikasi BRImo.

## User Review Required

> [!NOTE]
> Perencanaan ini berfokus pada area **di atas Fast Menu**: transisi background gelombang biru ke putih, serta ilustrasi lingkaran event di tengah layar.

## Proposed Changes

### [Layouts & Drawables]
#### [NEW] [bg_login_wave.xml](file:///C:/Users/ASUS%20Vivobook/AndroidStudioProjects/brimoku_backup/app/src/main/res/drawable/bg_login_wave.xml)
- Dibuat vektor/shape khusus untuk transisi latar belakang biru bergelombang (*blue curved wave transition*) ke area putih di bawahnya.

#### [MODIFY] [bg_pebble_card.xml](file:///C:/Users/ASUS%20Vivobook/AndroidStudioProjects/brimoku_backup/app/src/main/res/drawable/bg_pebble_card.xml)
- Menyesuaikan kontainer ilustrasi berbentuk **lingkaran/oval presisi** dengan latar belakang putih murni di mana gambar event berada.

#### [MODIFY] [activity_login.xml](file:///C:/Users/ASUS%20Vivobook/AndroidStudioProjects/brimoku_backup/app/src/main/res/layout/activity_login.xml)
- **Top Bar**: "ID", Logo BRImo, dan "Kontak Kami" diatur dengan padding atas yang rapi.
- **Slogan**: Teks *"Selamat Memperingati Hari Statistik Nasional 2026"* berada di atas kontainer ilustrasi.
- **Background Wave**: Menempatkan transisi gelombang biru melengkung di latar belakang di mana kontainer ilustrasi menumpang tepat di garis melengkung tersebut.
- **Center Illustration**: Menampilkan ilustrasi grafik event dalam kontainer lingkaran/oval putih murni tanpa ada potongan kaku.

## Verification Plan

### Automated Tests
- Menjalankan build Gradle `app:assembleDebug` untuk memastikan kompilasi sukses.

### Manual Verification
- Menjalankan aplikasi untuk memastikan tampilan layar login bagian atas presisi dengan gambar referensi.
