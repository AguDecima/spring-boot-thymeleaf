package com.decima.spring.entidad.models.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.decima.spring.entidad.models.dao.IClienteDao;
import com.decima.spring.entidad.models.entity.Cliente;

@Service
public class ClienteService implements IClienteService {
	
	@Autowired
	private IClienteDao clienteDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<Cliente> findall() {
		// TODO Auto-generated method stub
		return (List<Cliente>) clienteDao.findAll();
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		// TODO Auto-generated method stub
		clienteDao.save(cliente);
	}

	@Override
	@Transactional(readOnly=true)
	public Optional<Cliente> findOne(Long id) {
		// TODO Auto-generated method stub
		return clienteDao.findById(id);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		// TODO Auto-generated method stub
		clienteDao.deleteById(id);
	}

	@Override
	public Page<Cliente> findall(Pageable pageable) {
		// TODO Auto-generated method stub
		return clienteDao.findAll(pageable);
	}

}
