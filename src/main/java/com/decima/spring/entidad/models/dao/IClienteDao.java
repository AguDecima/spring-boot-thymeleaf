package com.decima.spring.entidad.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.decima.spring.entidad.models.entity.Cliente;

// PagingAndSortingRepository hereda de CrudRepository y sirve para paginar
public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long> {
	 
}
