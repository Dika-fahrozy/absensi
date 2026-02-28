package com.bnsp.absensi.Service.Impl;

import com.bnsp.absensi.DTO.Response.AbsensiResponseDTO;
import com.bnsp.absensi.Model.Absensi;
import com.bnsp.absensi.Model.Karyawan;
import com.bnsp.absensi.Repository.AbsensiRepository;
import com.bnsp.absensi.Repository.KaryawanRepository;
import com.bnsp.absensi.Service.AbsensiService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AbsensiServiceImpl implements AbsensiService {

    private final AbsensiRepository absensiRepository;
    private final KaryawanRepository karyawanRepository;

    private static final LocalTime JAM_MASUK_STANDAR = LocalTime.of(8, 0);

    @Override
    public AbsensiResponseDTO checkIn(String nip) {
        Karyawan karyawan = karyawanRepository.findByNip(nip)
                .orElseThrow(() -> new EntityNotFoundException("Karyawan tidak ditemukan"));

        // Cek apakah sudah absen hari ini
        if (absensiRepository.findByKaryawanIdAndTanggal(karyawan.getId(), LocalDate.now()).isPresent()) {
            throw new IllegalStateException("Anda sudah melakukan check-in hari ini");
        }

        Absensi absensi = new Absensi();
        absensi.setKaryawan(karyawan);
        absensi.setTanggal(LocalDate.now());
        absensi.setJamMasuk(LocalTime.now());

        // Tentukan status berdasarkan jam masuk
        if (absensi.getJamMasuk().isAfter(JAM_MASUK_STANDAR)) {
            absensi.setStatus(Absensi.StatusAbsensi.TERLAMBAT);
        } else {
            absensi.setStatus(Absensi.StatusAbsensi.HADIR);
        }

        Absensi saved = absensiRepository.save(absensi);
        return mapToResponseDTO(saved);
    }

    @Override
    public AbsensiResponseDTO checkOut(String nip) {
        Karyawan karyawan = karyawanRepository.findByNip(nip)
                .orElseThrow(() -> new EntityNotFoundException("Karyawan tidak ditemukan"));

        Absensi absensi = absensiRepository.findByKaryawanIdOrderByTanggalDesc(karyawan.getId())
                .orElseThrow(() -> new EntityNotFoundException("Data absensi tidak ditemukan"));

        if (absensi.getJamKeluar() != null) {
            throw new IllegalStateException("Anda sudah melakukan check-out hari ini");
        }

        absensi.setJamKeluar(LocalTime.now());
        Absensi saved = absensiRepository.save(absensi);
        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsensiResponseDTO>
    getAbsensiByKaryawanAndDateRange(
            LocalDate start, LocalDate end) {
        return absensiRepository.findByDateRange(start, end)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private AbsensiResponseDTO mapToResponseDTO(Absensi absensi) {
        Integer durasi = null;
        if (absensi.getJamMasuk() != null && absensi.getJamKeluar() != null) {
            durasi = (int) ChronoUnit.HOURS.between(absensi.getJamMasuk(), absensi.getJamKeluar());
        }

        return AbsensiResponseDTO.builder()
                .namaKaryawan(absensi.getKaryawan().getNama())
                .nip(absensi.getKaryawan().getNip())
                .tanggal(absensi.getTanggal())
                .jamMasuk(absensi.getJamMasuk())
                .jamKeluar(absensi.getJamKeluar())
                .status(absensi.getStatus())
                .durasiKerja(durasi)
                .build();
    }
}
