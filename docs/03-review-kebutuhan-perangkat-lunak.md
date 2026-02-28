# 🔍 Tinjauan Ulang (Review) Kebutuhan Perangkat Lunak

## Unit Kompetensi: J.62SAD00.009.1 — Meninjau Ulang (Review) Kebutuhan Perangkat Lunak

**Nama Proyek:** Absensi Karyawan API  
**Versi Dokumen:** 1.0  
**Tanggal:** 27 Februari 2026  
**Penyusun:** Dika Fahrozy  

---

## 1. Pendahuluan

### 1.1 Tujuan Dokumen

Dokumen ini bertujuan untuk memvalidasi seluruh kebutuhan perangkat lunak yang telah didefinisikan pada dokumen SRS (01-spesifikasi-kebutuhan-perangkat-lunak.md), memastikan:

1. Kebutuhan **sesuai dengan tujuan bisnis** sistem pencatatan kehadiran karyawan
2. Kebutuhan **bebas dari konflik** satu sama lain
3. Kebutuhan **lengkap, konsisten, dan dapat diimplementasikan**
4. Kebutuhan **dapat diuji (testable)**

### 1.2 Ruang Lingkup Review

Review dilakukan terhadap:

- Seluruh kebutuhan fungsional (FR-01 s/d FR-08)
- Seluruh kebutuhan non-fungsional (NFR-01 s/d NFR-04)
- Konsistensi antara kebutuhan dan implementasi aktual (Controller, Service, Entity)
- Kelengkapan cakupan use case

### 1.3 Tim Reviewer

| Peran              | Tanggung Jawab                              |
| ------------------ | ------------------------------------------- |
| **System Analyst** | Memvalidasi kesesuaian dengan tujuan HR/Absensi |
| **Lead Developer** | Memverifikasi kelayakan teknis implementasi |
| **QA Engineer**    | Memastikan kebutuhan dapat diuji            |

---

## 2. Kriteria Review

Setiap kebutuhan dievaluasi berdasarkan kriteria berikut:

| Kriteria                        | Deskripsi                                    | Bobot |
| ------------------------------- | -------------------------------------------- | ----- |
| **Kelengkapan (Completeness)**  | Apakah kebutuhan sudah cukup detail?         | 20%   |
| **Konsistensi (Consistency)**   | Apakah ada konflik antar kebutuhan?          | 20%   |
| **Keterlacakan (Traceability)** | Apakah bisa dipetakan ke komponen kode?      | 15%   |
| **Keterujian (Testability)**    | Apakah bisa dibuat test case?                | 15%   |
| **Kelayakan (Feasibility)**     | Apakah bisa diimplementasikan secara teknis? | 15%   |
| **Kejelasan (Clarity)**         | Apakah tidak ambigu?                         | 15%   |

**Skala Penilaian:**
- ✅ **Memenuhi** — Kriteria terpenuhi dengan baik
- ⚠️ **Perlu Perbaikan** — Ada catatan minor
- ❌ **Tidak Memenuhi** — Harus diperbaiki sebelum implementasi

---

## 3. Review Kebutuhan Fungsional

### 3.1 Manajemen Data Master Karyawan (FR-01, FR-02, FR-03)

| Kriteria     | Status | Catatan                                                   |
| ------------ | ------ | --------------------------------------------------------- |
| Kelengkapan  | ✅     | Atribut esensial API lengkap (Nama, NIP, Email, Dept)   |
| Konsistensi  | ✅     | Sinkron antara Controller, Service, dan DTO               |
| Keterlacakan | ✅     | Terpetakan pada `KaryawanController` dan `KaryawanService`|
| Keterujian   | ✅     | Data dummy Karyawan dapat difeed untuk testing easily     |
| Kelayakan    | ✅     | Sederhana dan langsung tersimpan (CRUD)                   |
| Kejelasan    | ✅     | Jelas peruntukannya                                       |

**Rekomendasi:**
- Pertimbangkan penambahan field penugasan lokasi karyawan di masa depan.
- Tambahkan logic pagination untuk endpoint GET `daftar karyawan` (`FR-02`).

---

### 3.2 Transaksi Absensi Check-In (FR-06)

| Kriteria     | Status | Catatan                                                              |
| ------------ | ------ | -------------------------------------------------------------------- |
| Kelengkapan  | ✅     | Business rule 1 kali sehari, validasi terlambat lewat 08:00 jelas    |
| Konsistensi  | ✅     | Model enum `StatusAbsensi` sejalan dengan implementasi di service    |
| Keterlacakan | ✅     | `AbsensiServiceImpl.checkIn(String nip)`                             |
| Keterujian   | ✅     | Testable menggunakan unit test via Mock time/LocalDate               |
| Kelayakan    | ✅     | Diimplementasikan dengan return object `AbsensiResponseDTO`          |
| Kejelasan    | ✅     | Logic validasi "Anda sudah check-in hari ini" tidak ambigu.          |

**Status: APPROVED** ✅

---

### 3.3 Transaksi Absensi Check-Out (FR-07)

| Kriteria     | Status | Catatan                                              |
| ------------ | ------ | ---------------------------------------------------- |
| Kelengkapan  | ✅     | Update kolom `jam_keluar` dari record terkait absensi hari ini. |
| Konsistensi  | ✅     | Konsisten dengan FR-06 |
| Keterlacakan | ✅     | `AbsensiServiceImpl.checkOut(String nip)` |
| Keterujian   | ✅     | Mudah dites menggunakan postman dan Junit |
| Kelayakan    | ✅     | Diimplementasikan |
| Kejelasan    | ✅     | Tidak ambigu |

**Rekomendasi:**
- Pertimbangkan validasi rentang minimum jam kerja agar Check-out tidak terlalu cepat setelah Check-in.

---

### 3.4 Rekap Histori Absensi (FR-08)

| Kriteria     | Status | Catatan                                                |
| ------------ | ------ | ------------------------------------------------------ |
| Kelengkapan  | ⚠️     | Tidak ada filter berdasarkan karyawan tertentu/departmen|
| Konsistensi  | ✅     | Format Date Range pada query param sudah tepat ISO      |
| Keterlacakan | ✅     | `AbsensiController.getAbsensiByRange`                  |
| Keterujian   | ✅     | Sangat testable                                        |
| Kelayakan    | ✅     | Diimplementasikan |
| Kejelasan    | ✅     | Filter awal (`start`, `end`) mandatory. |

---

## 4. Review Kebutuhan Non-Fungsional

### 4.1 Arsitektur & Performa (NFR-01, NFR-02)

| Sub-ID  | Kebutuhan                | Status | Catatan                                     |
| ------- | ------------------------ | ------ | ------------------------------------------- |
| NFR-01a | Response Time < 1s       | ✅     | Sistem Spring Boot cepat untuk single read/write JPA |
| NFR-02a | Layered Architecture     | ✅     | Controller - Service - Repo - DB pattern  |
| NFR-02b | DTO Mapping              | ✅     | Return Object di wrap di Model API Response |

**Temuan Arsitektur & Keamanan:**

| No   | Temuan                                 | Severity  | Rekomendasi                                                      |
| ---- | -------------------------------------- | --------- | ---------------------------------------------------------------- |
| A-01 | Belum ada mekanisme Log/Audit Action   | ⚠️ Medium | Tracking history hapus data `karyawan` (FR-05) sangat penting. Sebaiknya menggunakan _Soft Delete_ (`is_deleted/status_aktif`). |
| A-02 | Hardcode Limit Waktu (08:00)           | ⚠️ Medium | Waktu kedatangan hardcode rentan patah bila beda departemen beda masuk. Baiknya simpan rule waktu ini di DB Master Schedule. |
| A-03 | Missing Authentication Wrapper         | 🔴 High   | Endpoint REST terbuka bebas. Butuh JWT Token Auth (Bisa phase 2).|

---

## 5. Ringkasan Hasil Review

### 5.1 Statistik Review

| Kategori       | Jumlah Sub Kriteria| Memenuhi ✅ | Perlu Perbaikan ⚠️ | Tidak Memenuhi ❌ |
| -------------- | ------ | ----------- | ------------------ | ----------------- |
| Fungsional     | 8      | 6           | 2                  | 0                 |
| Non-Fungsional | 4      | 2           | 2                  | 0                 |
| **Total**      | **12** | **8**       | **4**              | **0**             |

### 5.2 Daftar Aksi (Action Items)

| No  | Prioritas | Item                                                             | Target            |
| --- | --------- | ---------------------------------------------------------------- | ----------------- |
| 1   | 🔴 Tinggi | Implementasi role/login security wrapper via Spring Security     | Phase 2 Deployment|
| 2   | 🟡 Sedang | Konversi *Hard Deletion* Data Karyawan menjadi *Soft Delete*     | Sprint berikutnya |
| 3   | 🟡 Sedang | Buat pengaturan limit kedatangan (Terlambat) di level DB/Config  | Sprint berikutnya |
| 4   | 🔵 Rendah | API filter By Karyawan ID di laporan histori                     | Sprint berikutnya |
| 5   | 🔵 Rendah | Pagination dan sorting untuk Master data Karyawan                | Backlog           |

### 5.3 Kesimpulan

Secara keseluruhan desain Sistem Informasi API Absensi Karyawan sudah berfungsi layaknya Minimum Viable Product (MVP). Proses Check In dan Check Out berhasil mencatat Logika bisnis dasar. Perbaikan keamanan dan struktur hardcode konfigurasi waktu disarankan agar segera direfleksikan di fase lanjutannya. Semuanya bisa dites sesuai perancanaan FR.

**Status Dokumen SRS:** ✅ **APPROVED WITH MINOR NOTES**

---

## 6. Tanda Tangan Review

| Peran           | Nama               | Tanggal      | Tanda Tangan       |
| --------------- | ------------------ | ------------ | ------------------ |
| System Analyst  | ********\_******** | 28/02/2026   | ********\_******** |
| Lead Developer  | ********\_******** | 28/02/2026   | ********\_******** |
| QA Engineer     | ********\_******** | 28/02/2026   | ********\_******** |
| Project Manager | ********\_******** | 28/02/2026   | ********\_******** |

---

_Dokumen ini disusun berdasarkan unit kompetensi SKKNI J.62SAD00.009.1 — Meninjau Ulang (Review) Kebutuhan Perangkat Lunak._
