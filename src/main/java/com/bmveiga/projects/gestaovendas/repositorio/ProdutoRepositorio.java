package com.bmveiga.projects.gestaovendas.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bmveiga.projects.gestaovendas.entidades.Produto;

public interface ProdutoRepositorio extends JpaRepository<Produto, Long> {

	List<Produto> findByCategoriaCodigo(Long codigoCategoria);
	
	Optional<Produto> findByCodigoAndCategoriaCodigo(Long codigo, Long codigoCategoria);
	
	Optional<Produto> findByCategoriaCodigoAndDescricao(Long codigo, String descricao);
		
}
