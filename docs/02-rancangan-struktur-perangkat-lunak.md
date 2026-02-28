# 🏗️ Rancangan Struktur Perangkat Lunak

## Unit Kompetensi: J.62SAD00.011.1 — Merancang Struktur Perangkat Lunak

**Nama Proyek:** Absensi Karyawan API  
**Versi Dokumen:** 1.0  
**Tanggal:** 27 Februari 2026  
**Penyusun:** Tim Pengembang  

---

## 1. Pendahuluan

### 1.1 Tujuan Dokumen

Dokumen ini menjelaskan perancangan arsitektur software, modul-modul, desain API, dan struktur database pada sistem **Absensi Karyawan API**. Dokumen ini menjadi panduan teknis utama bagi tim pengembang dalam membangun dan memelihara sistem.

### 1.2 Ruang Lingkup

Dokumen mencakup:

- Arsitektur sistem secara keseluruhan
- Desain modul dan komponen
- Desain RESTful API
- Struktur database dan relasi antar tabel (Entities)

---

## 2. Arsitektur Sistem

### 2.1 Arsitektur Tingkat Tinggi (High-Level Architecture)

```text
┌────────────────┐      HTTP/JSON      ┌─────────────────────────────────┐
│                │◀───────────────────▶│          SPRING BOOT APP        │
│   API Client   │                     │        (Tomcat Embedded)        │
│    (Postman/   │                     │                                 │
│    Web/Mobile) │                     │   ┌─────────────────────────┐   │
│                │                     │   │       Controllers       │   │
└────────────────┘                     │   └────────────┬────────────┘   │
                                       │                │                │
                                       │   ┌────────────▼────────────┐   │
                                       │   │        Services         │   │
                                       │   └────────────┬────────────┘   │
                                       │                │                │
                                       │   ┌────────────▼────────────┐   │
                                       │   │      Repositories       │   │
                                       │   └────────────┬────────────┘   │
                                       └────────────────┼────────────────┘
                                                        │
                                                  JDBC/Hibernate
                                                        │
                                                        ▼
                                       ┌─────────────────────────────────┐
                                       │            MySQL DB             │
                                       │                                 │
                                       │   ┌───────────┐ ┌───────────┐   │
                                       │   │  karyawan │ │  absensi  │   │
                                       │   └───────────┘ └───────────┘   │
                                       └─────────────────────────────────┘
```

### 2.2 Pola Arsitektur: Layered Architecture

Sistem menggunakan **Layered Architecture** dengan pemisahan komponen sebagai berikut:

```text
┌──────────────────────────────────────────────────┐
│                 PRESENTATION LAYER               │
│                   (Controllers)                  │
│  AbsensiController.java │ KaryawanController.java│
├──────────────────────────────────────────────────┤
│                 BUSINESS LOGIC LAYER             │
│                     (Services)                   │
│  AbsensiServiceImpl.java │ KaryawanServiceImpl.java|
├──────────────────────────────────────────────────┤
│                 DATA ACCESS LAYER                │
│             (Repositories / Spring Data)         │
│  AbsensiRepository.java │ KaryawanRepository.java│
├──────────────────────────────────────────────────┤
│                 DATA LAYER                       │
│              (Entities & Models)                 │
│  Absensi.java           │ Karyawan.java          │
├──────────────────────────────────────────────────┤
│               CROSS-CUTTING CONCERNS             │
│        (DTOs, Configurations, Exception Handling)│
│  GlobalExceptionHandler.java | *Request/Response |
└──────────────────────────────────────────────────┘
```

---

## 3. Struktur Proyek & Modul

### 3.1 Struktur Direktori (Package)

```text
com.bnsp.absensi/
├── AbsensiApplication.java           # Entry point aplikasi Spring Boot
├── Config/                           # Konfigurasi aplikasi
│   └── GlobalExceptionHandler.java   # Centralized error handling
├── Controller/                       # REST endpoint handler
│   ├── AbsensiController.java
│   └── KaryawanController.java
├── DTO/                              # Data Transfer Objects
│   ├── KaryawanDTO.java
│   ├── Request/
│   │   └── AbsensiRequestDTO.java
│   └── Response/
│       ├── AbsensiResponseDTO.java
│       └── ApiResponse.java          # Wrapper respons standar
├── Model/                            # Definisi Entitas Database
│   ├── Absensi.java                  # Entitas Absensi (transaksi)
│   └── Karyawan.java                 # Entitas Karyawan (master)
├── Repository/                       # Database interface (Spring Data JPA)
│   ├── AbsensiRepository.java
│   └── KaryawanRepository.java
└── Service/                          # Business logic interface & implementasi
    ├── AbsensiService.java
    ├── KaryawanService.java
    └── Impl/
        ├── AbsensiServiceImpl.java
        └── KaryawanServiceImpl.java
```

### 3.2 Deskripsi Modul

| Layer / Package | Deskripsi                                      |
| :--- | :--- |
| **Config** | Konfigurasi global aplikasi. Berisi `GlobalExceptionHandler` untuk menangkap dan merespons berbagai Exception (seperti validasi gagal, entitas tidak ditemukan) ke bentuk `ApiResponse`. |
| **Controller** | Menerima request HTTP dari client, memanggil Service yang sesuai, dan mengembalikan `ApiResponse`. Kelas dianotasi dengan `@RestController` dan OpenAPI/Swagger tags. |
| **DTO** | Obyek untuk pertukaran data antara layer Controller dan layer Service. Mencegah eksposur entitas database secara langsung. Berisi anotasi validasi (`@NotBlank`, dll). |
| **Model** | Entitas JPA yang merepresentasikan tabel di dalam database (`@Entity`). Berisi business rules sederhana (seperti setting `createdAt` saat `@PrePersist`). |
| **Repository** | Interface dari Spring Data JPA. Digunakan untuk query ke database tanpa menulis query manual. Terdapat pencarian custom dengan derived query methods maupun `@Query`. |
| **Service** | Layer yang berisi core business logic (transaksional). Validasi logika bisnis seperti cek apakah karyawan sudah absen hari ini, penentuan status terlambat, dll. |

---

## 4. Desain API (RESTful)

### 4.1 Konvensi API

- **Base URL Asumsi:** `http://localhost:8080` (sesuaikan port saat running)
- **Format Payload & Response:** JSON (`application/json`)
- **Struktur Standard Response (ApiResponse Wrapper):**
  ```json
  {
    "success": true,
    "code": 200,
    "message": "Pesan deskriptif",
    "data": {}
  }
  ```

### 4.2 Endpoint Manajemen Karyawan (`/karyawan`)

| Method | Endpoint | Keterangan | Request Body | Response (Sukses) |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/karyawan` | Tambah karyawan | `KaryawanDTO` | `201 Created` |
| `GET` | `/karyawan` | Lihat daftar karyawan | - | `200 OK` + List |
| `GET` | `/karyawan/{id}` | Lihat detail karyawan | - | `200 OK` |
| `PUT` | `/karyawan/{id}` | Update data karyawan | `KaryawanDTO` | `200 OK` (Updated) |
| `DELETE` | `/karyawan/{id}`| Hapus karyawan | - | `200 OK` |

**Skema Payload: `KaryawanDTO`**
```json
{
  "nama": "String (wajib)",
  "nip": "String (wajib, unik)",
  "email": "String (wajib, email format, unik)",
  "departemen": "String",
  "jabatan": "String"
}
```

### 4.3 Endpoint Manajemen Absensi (`/absensi`)

| Method | Endpoint | Keterangan | Request Parameters | Response |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/absensi/checkin` | Absen masuk | `?karyawanId={id}` | `201 Created` |
| `POST` | `/absensi/checkout` | Absen keluar | `?karyawanId={id}` | `200 OK` |
| `GET` | `/absensi/karyawan/range` | Rekap absensi | `?start=YYYY-MM-DD&end=YYYY-MM-DD` | `200 OK` + List |

**Skema Payload Response: `AbsensiResponseDTO`**
```json
{
  "id": 1,
  "karyawanId": 123,
  "namaKaryawan": "Budi",
  "nip": "EMP001",
  "tanggal": "2026-02-27",
  "jamMasuk": "07:55:00",
  "jamKeluar": "17:05:00",
  "status": "HADIR",
  "keterangan": null,
  "durasiKerja": 9
}
```

---

## 5. Struktur Database

Sistem menggunakan database relasional (MySQL). Skema dibuat otomatis oleh JPA Hibernate (berdasarkan setting properti model).

### 5.1 Entity Relationship Diagram (ERD)

```text
   ┌──────────────────────────┐           ┌───────────────────────────┐
   │       KARYAWAN           │           │         ABSENSI           │
   ├──────────────────────────┤           ├───────────────────────────┤
   │ id          BIGINT   PK  │◀────┐     │ id           BIGINT   PK  │
   │ nama        VARCHAR      │     │     │ karyawan_id  BIGINT   FK  │
   │ nip         VARCHAR (UQ) │     │     │ tanggal      DATE         │
   │ email       VARCHAR      │     └─────│ jam_masuk    TIME         │
   │ departemen  VARCHAR      │    1:N    │ jam_keluar   TIME         │
   │ jabatan     VARCHAR      │           │ status       ENUM         │
   │ created_at  DATETIME     │           │ keterangan   VARCHAR      │
   └──────────────────────────┘           │ created_at   DATETIME     │
                                          │ updated_at   DATETIME     │
                                          └───────────────────────────┘
```

### 5.2 Data Dictionary

#### Tabel: `karyawan`

| Kolom | Tipe Data | Constraint | Keterangan |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | ID Identifier data karyawan. |
| `nama` | VARCHAR | NOT NULL | Nama lengkap karyawan. |
| `nip` | VARCHAR | NOT NULL, UNIQUE | Nomor Induk Pegawai. |
| `email` | VARCHAR | NOT NULL, UNIQUE | Email resmi karyawan. |
| `departemen` | VARCHAR | NULLABLE | Divisi/Departemen penempatan kerja. |
| `jabatan` | VARCHAR | NULLABLE | Posisi jabatan kerja saat ini. |
| `created_at` | DATETIME | | Waktu record dibuat di dalam sistem (`@PrePersist`). |

#### Tabel: `absensi`

| Kolom | Tipe Data | Constraint | Keterangan |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | ID Identifier data absensi. |
| `karyawan_id`| BIGINT | FOREIGN KEY -> karyawan.id, NOT NULL| ID Karyawan yang melakukan absensi. |
| `tanggal` | DATE | NOT NULL | Tanggal absensi berjalan. |
| `jam_masuk` | TIME | NULLABLE | Waktu saat check-in. |
| `jam_keluar` | TIME | NULLABLE | Waktu saat check-out. |
| `status` | ENUM ('HADIR', 'TERLAMBAT', 'IZIN', 'SAKIT', 'ALFA') | NULLABLE | Status kehadiran di-derive saat check-in. |
| `keterangan` | VARCHAR | NULLABLE | Alasan jika karyawan absensi selain hadir. |
| `created_at` | DATETIME | | Waktu pembuatan tiket absensi. |
| `updated_at` | DATETIME | | Waktu update saat proses check-out bekerja (`@PreUpdate`). |

---

_Dokumen ini disusun berdasarkan unit kompetensi SKKNI J.62SAD00.011.1 — Merancang Struktur Perangkat Lunak._
