package com.decima.spring.entidad.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService implements IUploadService {

	@Override
	public Resource load(String filename) throws MalformedURLException {
		Path rootPath = pathFoto(filename);
		Resource recurso = null;

		recurso = new UrlResource(rootPath.toUri());
		if(!recurso.exists() || !recurso.isReadable())
		{
			throw new RuntimeException("Error : No se puede cargar la imagen " + rootPath.toString());
		} 

		return recurso;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		
		String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path rootPath = pathFoto(uniqueFilename);
		
		Files.copy(file.getInputStream(), rootPath);
		
		return uniqueFilename;
	}

	@Override
	public boolean delete(String filename) {
		Path rootPath = pathFoto(filename);
		File archivo = rootPath.toFile();
		
		if(archivo.exists() && archivo.canRead())
		{
			if(archivo.delete()) return true;
		}
		return false;
	}
	
	public Path pathFoto(String filename)
	{
		return Paths.get("uploads").resolve(filename).toAbsolutePath();
	}
	
	// metodo para eliminar todos los archivos de el recurso uploads
	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get("uploads").toFile());
		
	}

	// inicia la carpeta uploads si no esta creada
	@Override
	public void ini() throws IOException {
		Files.createDirectory(Paths.get("uploads"));
	}

}
