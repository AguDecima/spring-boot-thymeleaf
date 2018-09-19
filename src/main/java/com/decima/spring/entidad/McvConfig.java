package com.decima.spring.entidad;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// clase para configurar un resource publico que puede ser o no externo al proyecto
@Configuration
public class McvConfig implements WebMvcConfigurer {

	/*private final Logger log = LoggerFactory.getLogger(getClass());
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		WebMvcConfigurer.super.addResourceHandlers(registry);
		
		// creo la ruta absoluta y con uri le agrego el esquema file:
		String resourcesPath = Paths.get("uploads").toUri().toString();
		log.info("resourcesPath : " + resourcesPath);
		registry.addResourceHandler("/uploads/**")
		.addResourceLocations(resourcesPath);
	}*/
	
}
