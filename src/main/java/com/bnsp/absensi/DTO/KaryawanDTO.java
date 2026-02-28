package com.bnsp.absensi.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KaryawanDTO {

    @NotBlank(message = "Nama wajib diisi")
    private String nama;

    @NotBlank(message = "NIP wajib diisi")
    private String nip;

    @NotBlank(message = "Email wajib diisi")
    @Email(message = "Format email tidak valid")
    private String email;

    private String departemen;

    private String jabatan;
}
