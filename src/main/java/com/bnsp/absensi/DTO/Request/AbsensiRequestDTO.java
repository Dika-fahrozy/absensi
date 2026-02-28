package com.bnsp.absensi.DTO.Request;

import com.bnsp.absensi.Model.Absensi;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class AbsensiRequestDTO {

    @NotNull(message = "ID Karyawan wajib diisi")
    private Long karyawanId;

    private LocalTime jamMasuk;

    private LocalTime jamKeluar;

    private Absensi.StatusAbsensi status;

    private String keterangan;
}
