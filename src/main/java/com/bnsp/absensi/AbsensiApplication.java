package com.bnsp.absensi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Absensi Service", version = "1.0", description = "Absensi Service"))
public class AbsensiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbsensiApplication.class, args);
	}

}
