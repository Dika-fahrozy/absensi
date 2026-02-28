package com.bnsp.absensi.DTO.Response;

import com.bnsp.absensi.Model.Absensi;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AbsensiResponseDTO {

    private Long id;

    private Long karyawanId;

    private String namaKaryawan;

    private String nip;

    private LocalDate tanggal;

    private LocalTime jamMasuk;

    private LocalTime jamKeluar;

    private Absensi.StatusAbsensi status;

    private String keterangan;

    private Integer durasiKerja;
}
