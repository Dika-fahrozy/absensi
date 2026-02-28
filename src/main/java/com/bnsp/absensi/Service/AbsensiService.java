package com.bnsp.absensi.Service;

import com.bnsp.absensi.DTO.Response.AbsensiResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface AbsensiService {

    AbsensiResponseDTO checkIn(String nip);

    AbsensiResponseDTO checkOut(String nip);

    List<AbsensiResponseDTO> getAbsensiByKaryawanAndDateRange(LocalDate start, LocalDate end);

}
