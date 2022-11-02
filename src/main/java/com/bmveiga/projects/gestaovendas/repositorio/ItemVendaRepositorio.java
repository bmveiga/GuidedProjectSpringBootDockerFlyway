package com.bmveiga.projects.gestaovendas.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bmveiga.projects.gestaovendas.entidades.ItemVenda;

public interface ItemVendaRepositorio extends JpaRepository<ItemVenda, Long> {
	
	@Query("select new com.bmveiga.projects.gestaovendas.entidades.ItemVenda("
			+ "iv.codigo, iv.quantidade , iv.precoVendido , iv.produto , iv.venda)"
			+ " from ItemVenda iv"
			+ " where iv.venda.codigo = :codigoVenda")
	List<ItemVenda> findByVendaPorCodigo(Long codigoVenda);
}
