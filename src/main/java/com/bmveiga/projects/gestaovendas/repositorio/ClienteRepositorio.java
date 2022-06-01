package com.bmveiga.projects.gestaovendas.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bmveiga.projects.gestaovendas.entidades.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

	Cliente findByNome(String nome); 
}
