package com.bnsp.absensi.Service.Impl;

import com.bnsp.absensi.DTO.KaryawanDTO;
import com.bnsp.absensi.Model.Karyawan;
import com.bnsp.absensi.Repository.KaryawanRepository;
import com.bnsp.absensi.Service.KaryawanService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class KaryawanServiceImpl implements KaryawanService {

    private final KaryawanRepository karyawanRepository;

    @Override
    public Karyawan createKaryawan(KaryawanDTO dto) {
        if (karyawanRepository.existsByNip(dto.getNip())) {
            throw new IllegalArgumentException("NIP sudah terdaftar");
        }
        if (karyawanRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email sudah terdaftar");
        }

        Karyawan karyawan = new Karyawan();
        karyawan.setNama(dto.getNama());
        karyawan.setNip(dto.getNip());
        karyawan.setEmail(dto.getEmail());
        karyawan.setDepartemen(dto.getDepartemen());
        karyawan.setJabatan(dto.getJabatan());

        return karyawanRepository.save(karyawan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Karyawan> getAllKaryawan() {
        return karyawanRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Karyawan getKaryawanById(UUID id) {
        return karyawanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Karyawan tidak ditemukan"));
    }

    @Override
    public Karyawan updateKaryawan(UUID id, KaryawanDTO dto) {
        Karyawan karyawan = getKaryawanById(id);
        karyawan.setNama(dto.getNama());
        karyawan.setEmail(dto.getEmail());
        karyawan.setDepartemen(dto.getDepartemen());
        karyawan.setJabatan(dto.getJabatan());
        return karyawanRepository.save(karyawan);
    }

    @Override
    public void deleteKaryawan(UUID id) {
        Karyawan karyawan = getKaryawanById(id);
        karyawanRepository.delete(karyawan);
    }

}
