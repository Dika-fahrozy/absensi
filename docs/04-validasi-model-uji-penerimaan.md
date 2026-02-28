# ✅ Validasi Model dan Uji Penerimaan Pengguna

## Unit Kompetensi: J.62SAD00.010.1 — Melakukan Validasi Model dan Uji Penerimaan Pengguna

**Nama Proyek:** Absensi Karyawan API  
**Versi Dokumen:** 1.0  
**Tanggal:** 27 Februari 2026  
**Penyusun:** Dika Fahrozy 

---

## 1. Pendahuluan

### 1.1 Tujuan Dokumen

Dokumen ini mendefinisikan:

1. **Rencana pengujian** (Test Plan) untuk fitur sistem pencatatan kehadiran.
2. **Test case** detail untuk setiap endpoint utama (Karyawan & Absensi).
3. **Kriteria penerimaan** (Acceptance Criteria) dari perspektif pengguna / sistem klien.
4. **Hasil uji penerimaan** (User Acceptance Testing / UAT).

### 1.2 Ruang Lingkup Pengujian

| Aspek          | Cakupan                                  |
| -------------- | ---------------------------------------- |
| **Jenis Test** | API E2E Testing, UAT                     |
| **Endpoint**   | Seluruh 8 endpoint API utama             |
| **Tools**      | Postman Collection, Swagger UI (Springdoc)|
| **Database**   | MySQL Lokal                              |
| **Test Data**  | Data master Karyawan dan Transaksi Dummy |

### 1.3 Referensi

| Dokumen               | Kode                                        |
| --------------------- | ------------------------------------------- |
| Spesifikasi Kebutuhan | 01-spesifikasi-kebutuhan-perangkat-lunak.md |
| Rancangan Struktur    | 02-rancangan-struktur-perangkat-lunak.md    |
| Review Kebutuhan      | 03-review-kebutuhan-perangkat-lunak.md      |

---

## 2. Strategi Pengujian

### 2.1 Level Pengujian

```text
┌─────────────────────────────────────────────────┐
│            Level 3: UAT (User Acceptance)       │
│    Pengguna menguji via Swagger UI / Postman    │
│    Validasi flow check-in & check-out           │
├─────────────────────────────────────────────────┤
│            Level 2: System Testing              │
│    Pengujian end-to-end seluruh API flow        │
│    Buat Master Karyawan → Check In → Check Out  │
├─────────────────────────────────────────────────┤
│            Level 1: Unit / DB Testing           │
│    Uji Validasi DTO, Model Constraint (NIP Unik)│
└─────────────────────────────────────────────────┘
```

### 2.2 Lingkungan Pengujian

| Komponen           | Spesifikasi                          |
| ------------------ | ------------------------------------ |
| **Server**         | Spring Boot 4.x (localhost:8080)     |
| **Database**       | MySQL Service Desktop Lokal          |
| **JDK**            | Java 17                              |
| **Build Tool**     | Maven                                |
| **API Client**     | Swagger UI UI (`http://localhost:8080/swagger-ui.html`)|

---

## 3. Test Case — Kebutuhan Fungsional Manajeman Karyawan

### 3.1 TC-01: Tambah Karyawan Baru (FR-01)

| ID       | Skenario                      | Input                                                               | Expected Result                                    | Status  |
| -------- | ----------------------------- | ------------------------------------------------------------------- | -------------------------------------------------- | ------- |
| TC-01-01 | Tambah Karyawan valid         | `{"nama":"Andi", "nip":"123", "email":"a@b.com", "departemen":"IT"}`| 201 Created, success: true, data tersimpan         | ✅ Pass |
| TC-01-02 | Tambah Karyawan NIP Duplikat  | Input ulang object dengan nip `"123"`                               | 400/500 Bad Request, "NIP sudah terdaftar"         | ✅ Pass |
| TC-01-03 | Tambah Karyawan Email Duplikat| Data berbeda tapi Email tetap `"a@b.com"`                           | 400/500 Bad Request, "Email sudah terdaftar"       | ✅ Pass |
| TC-01-04 | Tanpa Nama                    | `{"nama":"", "nip":"124", "email":"c@b.com"}`                       | 400 Bad Request (Validation Exception)             | ✅ Pass |

### 3.2 TC-02: Lookup & Modifikasi Karyawan (FR-02, FR-03, FR-04, FR-05)

| ID       | Skenario               | Skenario Aksi                                        | Expected Result                               | Status  |
| -------- | ---------------------- | ---------------------------------------------------- | --------------------------------------------- | ------- |
| TC-02-01 | Get list Karyawan      | GET `/karyawan`                                      | 200 OK, return list length >= 1               | ✅ Pass |
| TC-02-02 | Get detail Karyawan    | GET `/karyawan/{id_andi}`                            | 200 OK, detail "Andi"                         | ✅ Pass |
| TC-02-03 | Edit Karyawan          | PUT `/karyawan/{id_andi}` data: nama="Andi Baru"     | 200 OK, data response nama="Andi Baru"        | ✅ Pass |
| TC-02-04 | Delete Karyawan        | DELETE `/karyawan/{id_baru}`                         | 200 OK, data terhapus                         | ✅ Pass |

---

## 4. Test Case — Kebutuhan Fungsional Transaksi Absensi

### 4.1 TC-03: Transaksi Check-In (FR-06)

| ID       | Skenario                    | Kondisi                        | Expected Result                                         | Status  |
| -------- | --------------------------- | ------------------------------ | ------------------------------------------------------- | ------- |
| TC-03-01 | Check-In Sukses Pertama Kali| Belum absen hari ini           | 201 Created, jam_masuk terisi, status `HADIR`/`TERLAMBAT`| ✅ Pass |
| TC-03-02 | Verifikasi Status Hadir     | Jam komputer sebelum 08:00     | 201 Created, status = `HADIR`                           | ✅ Pass |
| TC-03-03 | Verifikasi Status Terlambat | Jam komputer setelah 08:00     | 201 Created, status = `TERLAMBAT`                       | ✅ Pass |
| TC-03-04 | Check-in Kedua (Double)     | User yang sama dipanggil lagi  | 400/500 Error, "Anda sudah melakukan check-in hari ini" | ✅ Pass |
| TC-03-05 | ID Karyawan Tidak Ditemukan | `karyawanId` ngasal            | 404 Not Found, "Karyawan tidak ditemukan"               | ✅ Pass |


### 4.2 TC-04: Transaksi Check-Out (FR-07)

| ID       | Skenario                    | Kondisi                        | Expected Result                                         | Status  |
| -------- | --------------------------- | ------------------------------ | ------------------------------------------------------- | ------- |
| TC-04-01 | Belum Check-in Minta Out    | User tidak punya data checkin  | 404 Error, "Data absensi tidak ditemukan"               | ✅ Pass |
| TC-04-02 | Check-Out Sukses            | Hari ini masuk, jam_keluar=null| 200 OK, jam_keluar terisi (LocalTime.now())             | ✅ Pass |
| TC-04-03 | Check-Out Double            | Sudah tekan checkout hari ini  | 400/500 Error, "Anda sudah melakukan check-out hari ini"| ✅ Pass |

### 4.3 TC-05: Rekap Absensi (FR-08)

| ID       | Skenario              | Input                                     | Expected Result                       | Status  |
| -------- | --------------------- | ----------------------------------------- | ------------------------------------- | ------- |
| TC-05-01 | Range Data Benar      | `?start=2026-02-01&end=2026-02-28`        | 200 OK, data array rekap return       | ✅ Pass |
| TC-05-02 | Kalkulasi Durasi Kerja| Data punya jam masuk dan keluar           | respon `durasiKerja` (integer hrs) ada| ✅ Pass |
| TC-05-03 | Bad Format Date       | `?start=01-02-2026`                       | 400 Bad Request (Type Mismatch)       | ✅ Pass |

---

## 5. Skenario Pengujian End-to-End (E2E)

### 5.1 Skenario Utama: Alur Lengkap Absensi Harian

```text
Langkah 1: Setup Pegawai Master
───────────────────────────────
POST /karyawan
Body: {"nama":"Joko","nip":"EMP99","email":"joko@emp.com"}
Expected: 201 Created → Simpan id karyawan = 1.

Langkah 2: Pegawai Datang Pagi Hari
───────────────────────────────────
POST /absensi/checkin?karyawanId=1
Expected: 201 Created → Validasi status (Hadir/Terlambat sesuai waktu server).

Langkah 3: Pegawai Coba Iseng Check-In Ulang
─────────────────────────────────────────────
POST /absensi/checkin?karyawanId=1
Expected: 500 Error (Anda sudah melakukan check-in hari ini)

Langkah 4: Pegawai Pulang Sore Hari
───────────────────────────────────
POST /absensi/checkout?karyawanId=1
Expected: 200 OK → Durasi kerja kalkulasi & Jam Keluar terisi.

Langkah 5: Admin HR Export/Rekap
────────────────────────────────
GET /absensi/karyawan/range?start=2026-02-01&end=2026-02-28
Expected: 200 OK → Data record Joko hari tersebut mucul. 
```

**Status E2E: ✅ PASS**

---

## 6. Kriteria Penerimaan (UAT Acceptance Criteria)

### 6.1 Kriteria Wajib (Must Have)

| No  | Kriteria                                        | Status       |
| --- | ----------------------------------------------- | ------------ |
| 1   | Format response dibungkus wrapper API yang konsisten (`ApiResponse`) | ✅ Terpenuhi |
| 2   | Sistem mencegah NIP dan Email Karyawan kembar        | ✅ Terpenuhi |
| 3   | Karyawan bisa Check-In presensi 1 kali dalam 1 hari kalender | ✅ Terpenuhi |
| 4   | Sistem mencegah Karyawan check out bila belum ada record check in | ✅ Terpenuhi |
| 5   | Karyawan bisa Check-Out presensi 1 kali dalam 1 hari kalender | ✅ Terpenuhi |
| 6   | Logika status Terlambat vs Hadir berjalan saat proses check in | ✅ Terpenuhi |
| 7   | API Rekap data histori periode bulanan berjalan   | ✅ Terpenuhi |

### 6.2 Ringkasan UAT

| Aspek               | Hasil |
| ------------------- | ----- |
| **Total Skenario**  | 16    |
| **Pass**            | 16    |
| **Fail**            | 0     |
| **Pass Rate**       | 100%  |

---

## 7. Kesimpulan

Berdasarkan hasil validasi model dan uji penerimaan pengguna:

1. ✅ Seluruh API endpoint (CRUD Karyawan & Transaksi Absensi) berjalan tanpa malfungsi.
2. ✅ Alur transaksi dari pendaftaran master data ke check-in hingga checkout bekerja linear.
3. ✅ Sistem memenuhi kriteria bisnis penerimaan minimal untuk rilis produksi (fase 1).
4. ✅ Validation check (`@Valid`) dari DTO Model terikat aman oleh Exception Handler global.

**Status UAT: ✅ ACCEPTED (Lolos Uji)**

---

## 8. Tanda Tangan Penerimaan

| Peran               | Nama               | Tanggal      | Status                 |
| ------------------- | ------------------ | ------------ | ---------------------- |
| HR Manager (PO)     | ********\_******** | 28/02/2026   | ☑ Diterima / ☐ Ditolak |
| QA Lead             | ********\_******** | 28/02/2026   | ☑ Diterima / ☐ Ditolak |
| Lead Developer      | ********\_******** | 28/02/2026   | ☑ Diterima / ☐ Ditolak |

---

_Dokumen ini disusun berdasarkan unit kompetensi SKKNI J.62SAD00.010.1 — Melakukan Validasi Model dan Uji Penerimaan Pengguna._
