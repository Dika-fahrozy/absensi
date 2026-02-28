package com.bnsp.absensi.Repository;

import com.bnsp.absensi.Model.Absensi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AbsensiRepository extends JpaRepository<Absensi, Long> {

    Optional<Absensi> findByKaryawanIdAndTanggal(UUID karyawanId, LocalDate tanggal);

    Optional<Absensi> findByKaryawanIdOrderByTanggalDesc(UUID karyawanId);

    @Query("SELECT a FROM Absensi a WHERE a.tanggal BETWEEN :startDate AND :endDate")
    List<Absensi> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
