package com.bnsp.absensi.Repository;

import com.bnsp.absensi.Model.Karyawan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface KaryawanRepository extends JpaRepository<Karyawan, Long> {

    boolean existsByNip(String nip);

    boolean existsByEmail(String email);

    Optional<Karyawan> findById(UUID id);

    Optional<Karyawan> findByNip(String nip);
}
