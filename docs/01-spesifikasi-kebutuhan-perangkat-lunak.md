# 📋 Spesifikasi Kebutuhan Perangkat Lunak (SRS)

## Unit Kompetensi: J.62SAD00.008.1 — Menyusun Spesifikasi Kebutuhan Perangkat Lunak

**Nama Proyek:** Absensi Karyawan API  
**Versi Dokumen:** 1.0  
**Tanggal:** 27 Februari 2026  
**Penyusun:** Dika Fahrozy

---

## 1. Pendahuluan

### 1.1 Tujuan Dokumen

Dokumen ini bertujuan untuk mengidentifikasi dan mendokumentasikan seluruh kebutuhan perangkat lunak (**fungsional** dan **non-fungsional**) pada sistem **Absensi Karyawan API**. Dokumen ini menjadi acuan utama bagi seluruh stakeholder (developer, tester, project manager) dalam proses pengembangan.

### 1.2 Ruang Lingkup Sistem

**Absensi Karyawan API** adalah sistem backend RESTful API untuk pengelolaan data karyawan dan pencatatan absensi harian. Sistem ini memungkinkan pengguna untuk:

- Mengelola data master karyawan (CRUD)
- Melakukan check-in dan check-out absensi harian
- Melihat histori absensi berdasarkan rentang tanggal
- Menentukan status keterlambatan secara otomatis berdasarkan jam masuk standar (08:00)

### 1.3 Definisi, Akronim, dan Singkatan

| Istilah  | Definisi                            |
|----------|-------------------------------------|
| **API**  | Application Programming Interface   |
| **REST** | Representational State Transfer     |
| **CRUD** | Create, Read, Update, Delete        |
| **SRS**  | Software Requirements Specification |
| **DTO**  | Data Transfer Object                |
| **ORM**  | Object-Relational Mapping           |
| **NIP**  | Nomor Induk Pegawai                 |
| **UUID** | Universally Unique Identifier       |

### 1.4 Referensi

- Spring Boot Framework Documentation v4.0.3
- Spring Data JPA Reference
- OpenAPI 3 (Swagger) Documentation

---

## 2. Deskripsi Umum Sistem

### 2.1 Perspektif Produk

Absensi Karyawan API merupakan sistem backend standalone yang menyediakan RESTful API layanan absensi. Sistem ini dibangun menggunakan:

| Komponen               | Teknologi                   |
|------------------------|-----------------------------|
| **Bahasa Pemrograman** | Java 17                     |
| **Framework**          | Spring Boot 4.0.3           |
| **Database**           | MySQL                       |
| **ORM**                | Spring Data JPA (Hibernate) |
| **API Documentation**  | Springdoc OpenAPI v3.0.1    |
| **Build System**       | Maven                       |

### 2.2 Fungsi Utama Produk

```
┌──────────────────────────────────────────────────┐
│             ABSENSI KARYAWAN API                 │
├──────────────────────────────────────────────────┤
│  👥 Modul Manajemen Karyawan                     │
│     ├─ Tambah Karyawan Baru                      │
│     ├─ Lihat Semua Data Karyawan                 │
│     ├─ Lihat Detail Karyawan (by ID)             │
│     ├─ Update Data Karyawan                      │
│     └─ Hapus Data Karyawan                       │
│                                                  │
│  ⏰ Modul Absensi                                │
│     ├─ Check-In Karyawan                         │
│     ├─ Check-Out Karyawan                        │
│     └─ Rekap Absensi (Date Range)                │
└──────────────────────────────────────────────────┘
```

### 2.3 Karakteristik Pengguna

| Aktor             | Deskripsi                | Hak Akses                                                           |
|-------------------|--------------------------|---------------------------------------------------------------------|
| **System/Client** | Aplikasi frontend client | Akses semua endpoint API data master karyawan dan transaksi absensi |

*(Catatan: Saat ini sistem belum mengimplementasikan layer autentikasi spesifik/role-based).*

### 2.4 Batasan Sistem

- Sistem hanya menyediakan REST API.
- Cek batas waktu Jam Masuk standar diset hardcode pada pukul 08:00 (LocalTime).
- Karyawan hanya bisa melakukan 1 kali check-in dan 1 kali check-out per hari.
- Validasi data menggunakan Jakarta Validation di level API.

---

## 3. Kebutuhan Fungsional

### 3.1 FR-01: Tambah Karyawan Baru

| Atribut       | Detail               |
|---------------|----------------------|
| **ID**        | FR-01                |
| **Nama**      | Tambah Karyawan Baru |
| **Prioritas** | Tinggi               |
| **Endpoint**  | `POST /karyawan`     |

**Deskripsi:**  
Sistem harus dapat menyimpan data karyawan baru. NIP dan Email tidak boleh duplikat.

**Input (`KaryawanDTO`):**

| Field        | Tipe   | Wajib | Validasi                       |
|--------------|--------|-------|--------------------------------|
| `nama`       | String | ✅    | Tidak boleh kosong             |
| `nip`        | String | ✅    | Tidak boleh kosong, harus unik |
| `email`      | String | ✅    | Format email valid, harus unik |
| `departemen` | String | ❌    | -                              |
| `jabatan`    | String | ❌    | -                              |

**Output Sukses (201 Created):**
Mengembalikan data karyawan yang baru dibuat dibungkus `ApiResponse`.

**Kondisi Error:**
- `400 Bad Request` — Validasi gagal (NIP/Email duplikat atau field wajib kosong).

---

### 3.2 FR-02: Lihat Daftar Karyawan

| Atribut       | Detail                |
|---------------|-----------------------|
| **ID**        | FR-02                 |
| **Nama**      | Lihat Daftar Karyawan |
| **Prioritas** | Sedang                |
| **Endpoint**  | `GET /karyawan`       |

**Deskripsi:**  
Sistem dapat menampilkan seluruh daftar karyawan yang terdaftar.

---

### 3.3 FR-03: Lihat Detail Karyawan

| Atribut       | Detail                |
|---------------|-----------------------|
| **ID**        | FR-03                 |
| **Nama**      | Lihat Detail Karyawan |
| **Prioritas** | Sedang                |
| **Endpoint**  | `GET /karyawan/{id}`  |

**Kondisi Error:**
- `404 Not Found` — Karyawan tidak ditemukan (EntityNotFoundException).

---

### 3.4 FR-04: Update Data Karyawan

| Atribut       | Detail               |
|---------------|----------------------|
| **ID**        | FR-04                |
| **Nama**      | Update Data Karyawan |
| **Prioritas** | Sedang               |
| **Endpoint**  | `PUT /karyawan/{id}` |

**Deskripsi:**  
Sistem dapat mengupdate data detail karyawan (Nama, Email, Departemen, Jabatan) menggunakan `KaryawanDTO`.

---

### 3.5 FR-05: Hapus Karyawan

| Atribut       | Detail                  |
|---------------|-------------------------|
| **ID**        | FR-05                   |
| **Nama**      | Hapus Data Karyawan     |
| **Prioritas** | Sedang                  |
| **Endpoint**  | `DELETE /karyawan/{id}` |

**Deskripsi:**  
Menghapus master data karyawan berdasarkan ID.

---

### 3.6 FR-06: Check-In Absensi

| Atribut       | Detail                            |
|---------------|-----------------------------------|
| **ID**        | FR-06                             |
| **Nama**      | Check-In Karyawan                 |
| **Prioritas** | Tinggi                            |
| **Endpoint**  | `POST /absensi/checkin?nip={nip}` |

**Deskripsi:**  
Karyawan melakukan absensi masuk (check-in) untuk hari ini.

**Aturan Bisnis:**
- Karyawan harus valid (`karyawanId` terdaftar).
- Jika waktu check-in melebihi 08:00, status di set `TERLAMBAT`, jika tidak `HADIR`.
- Karyawan tidak dapat check-in lebih dari satu kali di hari yang sama.

**Kondisi Error:**
- `404 Not Found` - Karyawan tidak ditemukan.
- `400/500` (IllegalStateException) - Anda sudah melakukan check-in hari ini.

---

### 3.7 FR-07: Check-Out Absensi

| Atribut       | Detail                             |
|---------------|------------------------------------|
| **ID**        | FR-07                              |
| **Nama**      | Check-Out Karyawan                 |
| **Prioritas** | Tinggi                             |
| **Endpoint**  | `POST /absensi/checkout?nip={nip}` |

**Deskripsi:**  
Karyawan melakukan pencatatan jam keluar kerja.

**Aturan Bisnis:**
- Karyawan harus sudah check-in pada hari tersebut.
- Tidak dapat melakukan check-out lebih dari 1 kali (jika `jamKeluar` sudah terisi, tolak).

---

### 3.8 FR-08: Rekap Absensi (Date Range)

| Atribut       | Detail                        |
|---------------|-------------------------------|
| **ID**        | FR-08                         |
| **Nama**      | Rekap Absensi                 |
| **Prioritas** | Sedang                        |
| **Endpoint**  | `GET /absensi/karyawan/range` |

**Input Parameters:**
- `start` (LocalDate ISO)
- `end` (LocalDate ISO)

**Deskripsi:**  
Menampilkan data absensi seluruh karyawan yang berada pada rentang tanggal `start` hingga `end`. Sistem otomatis menghitung `durasiKerja` (dalam jam) berdasarkan selisih jam masuk dan jam keluar.

---

## 4. Kebutuhan Non-Fungsional

### 4.1 NFR-01: Performa (Performance)

| ID      | Kebutuhan         | Target              |
|---------|-------------------|---------------------|
| NFR-01a | Response time API | < 500ms per request |

### 4.2 NFR-02: Desain & Arsitektur

| ID      | Kebutuhan               | Detail                                                             |
|---------|-------------------------|--------------------------------------------------------------------|
| NFR-02a | Standar Arsitektur      | Layered Architecture (Controller, Service, Repository, Model, DTO) |
| NFR-02b | Pemisahan Data Transfer | Menggunakan DTO (`KaryawanDTO`, `AbsensiResponseDTO`)              |

### 4.3 NFR-03: Standar Respons API

| ID      | Kebutuhan               | Detail                                                     |
|---------|-------------------------|------------------------------------------------------------|
| NFR-03a | Format respons terpusat | Menggunakan generic `ApiResponse<T>`                       |
| NFR-03b | Error handling global   | `GlobalExceptionHandler` (Asumsi Spring @ControllerAdvice) |

### 4.4 NFR-04: API Dokumentasi

| ID      | Kebutuhan       | Detail                                                           |
|---------|-----------------|------------------------------------------------------------------|
| NFR-04a | Spesifikasi API | Auto-generated menggunakan Swagger/OpenAPI (`springdoc-openapi`) |

---

## 5. Use Case Diagram

```
                    ┌─────────────────────────────────┐
                    │     Absensi Karyawan API        │
                    │                                 │
    ┌────────┐      │  ┌────────────────────────┐     │
    │        │──────┼─▶│ UC-01: Kelola Karyawan │     │
    │Client /│      │  │ (CRUD)                 │     │
    │System  │      │  └────────────────────────┘     │
    │        │      │                                 │
    │        │      │  ┌──────────────────────┐       │
    │        │──────┼─▶│ UC-02: Check-In      │       │
    │        │      │  └──────────────────────┘       │
    │        │      │  ┌──────────────────────┐       │
    │        │──────┼─▶│ UC-03: Check-Out     │       │
    │        │      │  └──────────────────────┘       │
    │        │      │  ┌──────────────────────┐       │
    │        │──────┼─▶│ UC-04: Rekap Absensi │       │
    │        │      │  └──────────────────────┘       │
    └────────┘      └─────────────────────────────────┘
```

---

## 6. Matriks Kebutuhan (Traceability Matrix)

| Kebutuhan | Endpoint                    | Model/DTO             | Service / Repository                            | Status    |
|-----------|-----------------------------|-----------------------|-------------------------------------------------|-----------|
| FR-01     | POST /karyawan              | KaryawanDTO, Karyawan | KaryawanService.createKaryawan                  | ✅ Selesai |
| FR-02     | GET /karyawan               | Karyawan              | KaryawanService.getAllKaryawan                  | ✅ Selesai |
| FR-03     | GET /karyawan/{id}          | Karyawan              | KaryawanService.getKaryawanById                 | ✅ Selesai |
| FR-04     | PUT /karyawan/{id}          | KaryawanDTO, Karyawan | KaryawanService.updateKaryawan                  | ✅ Selesai |
| FR-05     | DELETE /karyawan/{id}       | —                     | KaryawanService.deleteKaryawan                  | ✅ Selesai |
| FR-06     | POST /absensi/checkin       | AbsensiResponseDTO    | AbsensiService.checkIn                          | ✅ Selesai |
| FR-07     | POST /absensi/checkout      | AbsensiResponseDTO    | AbsensiService.checkOut                         | ✅ Selesai |
| FR-08     | GET /absensi/karyawan/range | AbsensiResponseDTO    | AbsensiService.getAbsensiByKaryawanAndDateRange | ✅ Selesai |

---

_Dokumen ini disusun berdasarkan unit kompetensi SKKNI J.62SAD00.008.1 — Menyusun Spesifikasi Kebutuhan Perangkat Lunak._
