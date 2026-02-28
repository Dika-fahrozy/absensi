package com.bnsp.absensi.Controller;

import com.bnsp.absensi.DTO.Response.AbsensiResponseDTO;
import com.bnsp.absensi.DTO.Response.ApiResponse;
import com.bnsp.absensi.Service.AbsensiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/absensi")
@Tag(name = "Absen Karyawan", description = "API untuk absen karyawan")
public class AbsensiController {

    private final AbsensiService absensiService;

    // Check-in
    @PostMapping("/checkin")
    public ResponseEntity<ApiResponse<AbsensiResponseDTO>> checkIn(@RequestParam String nip) {
        AbsensiResponseDTO response = absensiService.checkIn(nip);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, "Berhasil Check In"));
    }

    // Check-out
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<AbsensiResponseDTO>> checkOut(@RequestParam String nip) {
        AbsensiResponseDTO response = absensiService.checkOut(nip);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.created(response, "Berhasil Check out"));
    }

    // Get absensi by date range
    @GetMapping("/karyawan/range")
    public ResponseEntity<ApiResponse<List<AbsensiResponseDTO>>> getAbsensiByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.created(absensiService.getAbsensiByKaryawanAndDateRange(start, end), "Berhasil Mengambil Data"));
    }
}
