package com.decima.spring.entidad.models.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.decima.spring.entidad.models.entity.Cliente;

public interface IClienteService {
	
	public List<Cliente> findall();
	public Page<Cliente> findall(Pageable pageable);
	public void save(Cliente cliente);
	public Optional<Cliente> findOne(Long id);
	public void delete(Long id);
}
