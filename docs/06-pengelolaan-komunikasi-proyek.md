# 📢 Pengelolaan Komunikasi Proyek

## Unit Kompetensi: M.702090.007.01 — Mengelola Komunikasi Proyek

**Nama Proyek:** Absensi Karyawan API  
**Versi Dokumen:** 1.0  
**Tanggal:** 27 Februari 2026  
**Penyusun:** Dika Fahrozy 

---

## 1. Pendahuluan

### 1.1 Tujuan Dokumen

Dokumen ini mengatur pengelolaan komunikasi proyek **Absensi Karyawan API**, mencakup:

1. Dokumentasi teknis yang dihasilkan selama pengembangan (SDLC)
2. Strategi komunikasi antar tim pengembang
3. Mekanisme penyampaian progres proyek ke manajemen
4. Standar pelaporan, dokumentasi API, dan distribusi informasi

### 1.2 Stakeholder Proyek

| Stakeholder           | Peran                               | Kebutuhan Informasi              |
| --------------------- | ----------------------------------- | -------------------------------- |
| **Project/HR Manager**| Mengelola output dan target absen   | Progress report, kelayakan fitur |
| **System Analyst**    | Merancang alur dan database         | SRS, desain dokumen              |
| **Backend Developer** | Mengimplementasikan sistem (Spring) | Spesifikasi teknis, rancangan API|
| **QA Engineer**       | Menguji sistem                      | Test plan, UAT result            |

---

## 2. Rencana Komunikasi Proyek

### 2.1 Matriks Komunikasi

| Jenis Komunikasi     | Frekuensi             | Media           | Peserta           | PIC             |
| -------------------- | --------------------- | --------------- | ----------------- | --------------- |
| Daily Sync           | Harian                | Koordinasi Tim  | Seluruh tim       | Technical Lead  |
| Sprint Planning      | Awal Pengembangan     | Meeting         | Seluruh tim       | Project Manager |
| Penyerahan Phase 1   | Akhir Sprint 1        | Handover Meeting| Tim + Stakeholder | Project Manager |
| Code Review          | Menyesuaikan          | Git Repository  | Developer         | Lead Developer  |
| Progress Report      | Mingguan              | Dokumen / Email | Management        | Project Manager |

---

## 3. Dokumentasi Teknis Proyek

### 3.1 Daftar Dokumen yang Dihasilkan

| No  | Nama Dokumen                          | Referensi SKKNI | Status     |
| --- | ------------------------------------- | --------------- | ---------- |
| 1   | Spesifikasi Kebutuhan Perangkat Lunak | J.62SAD00.008.1 | ✅ Selesai |
| 2   | Rancangan Struktur Perangkat Lunak    | J.62SAD00.011.1 | ✅ Selesai |
| 3   | Review Kebutuhan Perangkat Lunak      | J.62SAD00.009.1 | ✅ Selesai |
| 4   | Validasi Model & Uji Penerimaan       | J.62SAD00.010.1 | ✅ Selesai |
| 5   | Penerapan Akses Basis Data            | J.620100.021.02 | ✅ Selesai |
| 6   | Pengelolaan Komunikasi Proyek         | M.702090.007.01 | ✅ Selesai |

### 3.2 Struktur Direktori Dokumentasi

File-file dokumentasi disimpan terpusat di dalam repository source code pada folder `docs`:

```text
/absensi/docs/
├── 01-spesifikasi-kebutuhan-perangkat-lunak.md
├── 02-rancangan-struktur-perangkat-lunak.md
├── 03-review-kebutuhan-perangkat-lunak.md
├── 04-validasi-model-uji-penerimaan.md
├── 05-penerapan-akses-basis-data.md
└── 06-pengelolaan-komunikasi-proyek.md
```

---

## 4. Dokumentasi API (API Documentation)

### 4.1 Metode Dokumentasi API

Sistem backend menggunakan modul bawaan **Springdoc OpenAPI (Swagger)** yang akan meng-generate halaman spesifikasi API secara dinamis berdasarkan anotasi Controller. Ini menggantikan kebutuhan menulis ulang dokumentasi Postman secara manual.

- **Akses Swagger UI**: `http://localhost:8080/swagger-ui/index.html` (Bila dijalankan di lokal)

Komunikasi backend / frontend sangat bergantung pada kesepakatan format YAML/JSON dari OpenAPI documentation ini.

### 4.2 Format Standar Data JSON

Semua endpoints Absensi & Karyawan sepakat mematuhi JSON response format yang didefinisikan kelas `ApiResponse<T>`:

```json
{
  "success": true,
  "code": 200,
  "message": "Deskripsi respon server (mis: Berhasil mengambil data)",
  "data": { 
      // Payload object list karyawan / absensi
  }
}
```

---

## 5. Penyampaian Progres Proyek

### 5.1 Status Proyek Saat Ini

**Status Keseluruhan MVP Fase 1:** 🟢 **ON TRACK (Bisa Diserahkan)**

### 5.2 Milestone Proyek

| #   | Milestone                | Target       | Aktual       | Status     |
| --- | ------------------------ | ------------ | ------------ | ---------- |
| M1  | Setup Project Spring Boot| Sprint Hari 1| Sprint Hari 1| ✅ Selesai |
| M2  | Setup DB Skema           | Sprint Hari 2| Sprint Hari 2| ✅ Selesai |
| M3  | Implementasi Karyawan API| Sprint Hari 3| Sprint Hari 3| ✅ Selesai |
| M4  | Implementasi Absensi API | Sprint Hari 4| Sprint Hari 4| ✅ Selesai |
| M5  | Dokumentasi SKKNI        | Sprint Hari 5| Sprint Hari 5| ✅ Selesai |

---

## 6. Pengelolaan Risiko & Isu

### 6.1 Catatan Risiko Sistem

| No   | Risiko                      | Probabilitas | Dampak        | Mitigasi                                        | Status         |
| ---- | --------------------------- | ------------ | ------------- | ----------------------------------------------- | -------------- |
| R-01 | Waktu lokal server berbeda  | Sedang       | Tinggi        | Set default TimeZone di aplikasi ke waktu lokal (WIB)| 🟡 Dipantau    |
| R-02 | REST API tanpa keamanan Auth| Sangat Tinggi| Kritis        | Batasi akses IP sementara / Segera implement Spring Security auth. | 🔴 Butuh Tindakan |
| R-03 | Tidak ada pagination list Karyawan | Sedang| Menengah      | Membangun fitur di Fase 2 | 🟡 Backlog |

---

## 7. Laporan Distribusi Dokumen

| Dokumen               | PM/HR| Sist. Analyst | Developer | QA Tester |
| --------------------- | ---- | ------------- | --------- | --------- |
| 01 - SRS              | ✅   | ✅            | ✅        | ✅        |
| 02 - Arsitektur / ERD | 📋   | ✅            | ✅        | 📋        |
| 03 - Review Requirements| ✅ | ✅            | ✅        | ✅        |
| 04 - UAT & Test Cases | ✅   | 📋            | 📋        | ✅        |
| 05 - Database         | 📋   | 📋            | ✅        | 📋        |
| 06 - Komunikasi       | ✅   | ✅            | ✅        | ✅        |

*(Keterangan: ✅ Wajib dibaca  \| 📋 Untuk referensi)*

---

## 8. Kesimpulan

Tugas dokumentasi pengembangan sistem untuk aplikasi "Absensi Karyawan API" sudah terstruktur rapi dan transparan. Flow informasi antar tim berjalan lancar berbasis dokumen .md (Markdown) yang menempel langsung dengan source code yang memungkinkan revisi secara Version Control System.

---

_Dokumen ini disusun berdasarkan unit kompetensi SKKNI M.702090.007.01 — Mengelola Komunikasi Proyek._
