package com.bmveiga.projects.gestaovendas.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bmveiga.projects.gestaovendas.entidades.Venda;

public interface VendaRepositorio extends JpaRepository<Venda, Long>{
	
	List<Venda> findByClienteCodigo(Long codigoCliente);

}
