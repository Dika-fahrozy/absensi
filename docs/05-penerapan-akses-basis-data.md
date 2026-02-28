# 🗄️ Penerapan Akses Basis Data

## Unit Kompetensi: J.620100.021.02 — Menerapkan Akses Basis Data

**Nama Proyek:** Absensi Karyawan API  
**Versi Dokumen:** 1.0  
**Tanggal:** 27 Februari 2026  
**Penyusun:** Tim Pengembang  

---

## 1. Pendahuluan

### 1.1 Tujuan Dokumen

Dokumen ini menjelaskan implementasi akses basis data pada sistem **Absensi Karyawan API**, mencakup:

1. Konfigurasi koneksi database
2. Implementasi operasi CRUD (Create, Read, Update, Delete)
3. Pengelolaan data backend menggunakan ORM Spring Data JPA
4. Strategi pooling dan transaksi

### 1.2 Teknologi Database

| Komponen            | Teknologi         | Versi  |
| ------------------- | ----------------- | ------ |
| **RDBMS**           | MySQL             | 8.x    |
| **ORM**             | Spring Data JPA   | 3.x    |
| **Connection Pool** | HikariCP          | bawaan Spring Boot |
| **JDBC Driver**     | MySQL Connector/J | 8.x    |

---

## 2. Konfigurasi Koneksi Database

### 2.1 Konfigurasi Aplikasi (application.properties / application.yml)

Konfigurasi database dibaca dari file konfigurasi `application.properties` bawaan Spring Boot:

```properties
# src/main/resources/application.properties

spring.datasource.url=jdbc:mysql://localhost:3306/absensi_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=supersecret
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA & Hibernate Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# HikariCP Connection Pool (opsional, tuning)
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
```

**Penjelasan Konfigurasi:**

| Parameter                             | Penjelasan                                    |
| ------------------------------------- | --------------------------------------------- |
| `spring.datasource.url`               | JDBC String koneksi ke MySQL                  |
| `spring.jpa.hibernate.ddl-auto=update`| Hibernate akan otomatis membuat/mengupdate struktur tabel pada database sesuai file Model Entity. |
| `spring.jpa.show-sql=true`            | Menampilkan query SQL yang dieksekusi JPA di log terminal. |
| `maximum-pool-size=10`                | Setting pool HikariCP membatasi max 10 koneksi simultan. |

---

## 3. Definisi Tabel (Schema Entities)

Sistem menggunakan model berbasis anotasi JPA (`@Entity`) tanpa harus menulis script SQL DDL manual.

### 3.1 Model Karyawan (`Karyawan.java`)

```java
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "karyawan")
public class Karyawan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;

    @Column(unique = true, nullable = false)
    private String nip;

    @Column(nullable = false)
    private String email;

    private String departemen;
    private String jabatan;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
```

### 3.2 Model Absensi (`Absensi.java`)

```java
@Data
@Entity
@Table(name = "absensi")
public class Absensi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relasi Many-to-One dengan tabel Karyawan
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "karyawan_id", nullable = false)
    private Karyawan karyawan;

    @Column(nullable = false)
    private LocalDate tanggal;

    @Column(name = "jam_masuk")
    private LocalTime jamMasuk;

    @Column(name = "jam_keluar")
    private LocalTime jamKeluar;

    @Enumerated(EnumType.STRING)
    private StatusAbsensi status;

    private String keterangan;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        tanggal = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum StatusAbsensi {
        HADIR, TERLAMBAT, IZIN, SAKIT, ALFA
    }
}
```

---

## 4. Implementasi Operasi CRUD (Repositories)

Spring Data JPA tidak membutuhkan implementasi class untuk CRUD API standar. Cukup dengan membuat Interface turunan `JpaRepository`.

### 4.1 Karyawan Repository

```java
public interface KaryawanRepository extends JpaRepository<Karyawan, Long> {
    
    // Derived Query Methods - Otomatis dibuatkan query SQL Exception by Spring Data
    boolean existsByNip(String nip);
    
    boolean existsByEmail(String email);
}
```

**Penggunaan di `KaryawanServiceImpl` (Akses Basis Data Aktual):**
- **Create**: `karyawanRepository.save(karyawan)`
- **Read All**: `karyawanRepository.findAll()`
- **Read One**: `karyawanRepository.findById(id)`
- **Update**: Modifikasi object entitas, lalu panggil `karyawanRepository.save(karyawan)`
- **Delete**: `karyawanRepository.delete(karyawan)`

### 4.2 Absensi Repository

```java
public interface AbsensiRepository extends JpaRepository<Absensi, Long> {

    // 1. Cek apakah karyawan sudah punya tiket check-in di hari tertentu
    Optional<Absensi> findByKaryawanIdAndTanggal(Long karyawanId, LocalDate tanggal);

    // 2. Ambil tiket absensi terakhir karyawan, diurutkan desc
    Optional<Absensi> findByKaryawanIdOrderByTanggalDesc(Long karyawanId);

    // 3. Custom Query (@Query) JPQL untuk ambil rentang tanggal laporan rekap absensi.
    @Query("SELECT a FROM Absensi a WHERE a.tanggal BETWEEN :start AND :end")
    List<Absensi> findByDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
```

---

## 5. Pengelolaan Transaksi (Transaction Management)

Spring menggunakan paradigma AOP (Aspect Oriented Programming) untuk membungkus operasi database dalam scope transaksi melalui anotasi `@Transactional`.

### 5.1 Penggunaan pada Service Layer

Seluruh blok *Service* diberikan anotasi `@Transactional` yang menjamin konsistensi data (ACID properties).

```java
@Service
@Transactional // <- Level Class, metode CUD akan masuk ke dalam 1 transaksi
@RequiredArgsConstructor
public class KaryawanServiceImpl implements KaryawanService {
    // ...
    @Override
    @Transactional(readOnly = true) // <- Optimasi Performa Read tanpa lock transaksi
    public List<Karyawan> getAllKaryawan() {
        return karyawanRepository.findAll();
    }
}
```

### 5.2 Flow: Transaksi Check-In Transaksional

```text
Client/Controller           AbsensiService                      Database (MySQL)
   │                              │                                     │
   │ POST /absensi/checkin        │                                     │
   │─────────────────────────────▶│                                     │
   │                              │ @Transactional starts               │
   │                              │                                     │
   │                              │ 1. findById (karyawan)              │
   │                              │────────────────────────────────────▶│ SELECT * FROM karyawan
   │                              │                                     │
   │                              │ 2. findByKaryawanIdAndTanggal       │
   │                              │────────────────────────────────────▶│ SELECT * FROM absensi
   │                              │                                     │
   │                              │ Cek Business Rules                  │
   │                              │                                     │
   │                              │ 3. save (absensi baru)              │
   │                              │────────────────────────────────────▶│ INSERT INTO absensi
   │                              │                                     │
   │                              │ @Transactional commit               │
   │ ◀── ApiResponse              │                                     │
```

Fitur fail-safe Hibernate: Bila poin 3 `save()` menyebabkan unhandled exception (Misal koneksi putus tiba-tiba atau constrain gagal), maka transaksi otomatis di-`rollback` secara utuh.

---

## 6. Ringkasan Operasi Database & Equivalent SQL

| Operasi API     | Spring Data JPA Method (Repository) | Perkiraan Execute Generik SQL yang dihasilkan Hibernate |
| --------------- | ----------------------------------- | ------------------------------------------------------------- |
| `POST /karyawan`| `karyawanRepository.save`           | `INSERT INTO karyawan (nama, nip, email, departemen, jabatan, created_at) VALUES (?, ?, ?, ?, ?, ?)` |
| `GET /karyawan` | `karyawanRepository.findAll`        | `SELECT k.* FROM karyawan k` |
| `POST /checkin` | `absensiRepository.save`            | `INSERT INTO absensi (karyawan_id, tanggal, jam_masuk, status, ...) VALUES (?, ?, ?, ...)` |
| `POST /checkout`| `absensiRepository.save` (Update)   | `UPDATE absensi SET jam_keluar = ?, updated_at = ? WHERE id = ?` |
| `GET /range`    | `absensiRepository.findByDateRange` | `SELECT a.* FROM absensi a WHERE a.tanggal BETWEEN ? AND ?` |

---

_Dokumen ini disusun berdasarkan unit kompetensi SKKNI J.620100.021.02 — Menerapkan Akses Basis Data._
