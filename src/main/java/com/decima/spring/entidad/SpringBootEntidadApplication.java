package com.decima.spring.entidad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.decima.spring.entidad.models.service.IUploadService;

// la interfaz de commandlinerunner me permite lanzar metodos por consola cuando inicia el proyecto
@SpringBootApplication
public class SpringBootEntidadApplication implements CommandLineRunner {

	@Autowired
	private IUploadService uploadService;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootEntidadApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		uploadService.deleteAll();
		uploadService.ini();
	}
}
