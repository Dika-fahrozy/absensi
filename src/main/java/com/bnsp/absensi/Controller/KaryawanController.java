package com.bnsp.absensi.Controller;

import com.bnsp.absensi.DTO.KaryawanDTO;
import com.bnsp.absensi.DTO.Response.ApiResponse;
import com.bnsp.absensi.Model.Karyawan;
import com.bnsp.absensi.Service.KaryawanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/karyawan")
@Tag(name = "Karyawan", description = "API untuk manajemen data karyawan")
public class KaryawanController {

    private final KaryawanService karyawanService;

    @PostMapping
    @Operation(summary = "Tambah karyawan baru")
    public ResponseEntity<ApiResponse<Karyawan>> createKaryawan(@Valid @RequestBody KaryawanDTO dto) {
        Karyawan karyawan = karyawanService.createKaryawan(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(karyawan, "Berhasil menambahkan karyawan baru"));
    }

    @GetMapping
    @Operation(summary = "View All Data karyawan")
    public ResponseEntity<ApiResponse<List<Karyawan>>> getAllKaryawan() {
        List<Karyawan> karyawanList = karyawanService.getAllKaryawan();
        return ResponseEntity.ok(ApiResponse.success(karyawanList, "Berhasil mengambil data karyawan"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get data karyawan berdasarkan ID")
    public ResponseEntity<ApiResponse<Karyawan>> getKaryawanById(@PathVariable UUID id) {
        Karyawan karyawan = karyawanService.getKaryawanById(id);
        return ResponseEntity.ok(ApiResponse.success(karyawan, "Berhasil mengambil detail karyawan"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Ubah data karyawan")
    public ResponseEntity<ApiResponse<Karyawan>> updateKaryawan(
            @PathVariable UUID id,
            @Valid @RequestBody KaryawanDTO dto) {
        Karyawan updated = karyawanService.updateKaryawan(id, dto);
        return ResponseEntity.ok(ApiResponse.updated(updated, "Berhasil mengupdate data karyawan"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Hapus data karyawan")
    public ResponseEntity<ApiResponse<Void>> deleteKaryawan(@PathVariable UUID id) {
        karyawanService.deleteKaryawan(id);
        return ResponseEntity.ok(ApiResponse.deleted("Berhasil menghapus data karyawan"));
    }
}
