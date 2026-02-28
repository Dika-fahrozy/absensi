package com.bnsp.absensi.Service;

import com.bnsp.absensi.DTO.KaryawanDTO;
import com.bnsp.absensi.Model.Karyawan;

import java.util.List;
import java.util.UUID;

public interface KaryawanService {

    Karyawan createKaryawan(KaryawanDTO dto);

    List<Karyawan> getAllKaryawan();

    Karyawan getKaryawanById(UUID id);

    Karyawan updateKaryawan(UUID id, KaryawanDTO dto);

    void deleteKaryawan(UUID id);
}
