package com.bnsp.absensi.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "karyawan")
public class Karyawan {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String nama;

    @Column(unique = true, nullable = false)
    private String nip;

    @Column(nullable = false)
    private String email;

    private String departemen;

    private String jabatan;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
