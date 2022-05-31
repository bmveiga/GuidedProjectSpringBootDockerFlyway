package com.bmveiga.projects.gestaovendas.controlador;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bmveiga.projects.gestaovendas.dto.produto.ProdutoRequestDTO;
import com.bmveiga.projects.gestaovendas.dto.produto.ProdutoResponseDTO;
import com.bmveiga.projects.gestaovendas.entidades.Produto;
import com.bmveiga.projects.gestaovendas.servico.ProdutoServico;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Produto")
@RestController
@RequestMapping("/categoria{codigoCategoria}/produtos")
public class ProdutoControlador {

	@Autowired
	private ProdutoServico produtoServico;

	@ApiOperation(value = "Listar todos")
	@GetMapping
	public List<ProdutoResponseDTO> listarTodos(@PathVariable Long codigoCategoria) {
		return produtoServico.listarTodos(codigoCategoria).stream()
				.map(produto -> ProdutoResponseDTO.converterParaProdutoDTO(produto)).collect(Collectors.toList());
	}

	@ApiOperation(value = "Buscar produto por código")
	@GetMapping("/{id}")
	public ResponseEntity<ProdutoResponseDTO> buscarPorCodigo(@PathVariable Long codigoCategoria,
			@PathVariable Long id) {
		Optional<Produto> produto = produtoServico.buscarPorCodigo(id, codigoCategoria);
		return produto.isPresent() ? ResponseEntity.ok(ProdutoResponseDTO.converterParaProdutoDTO(produto.get())) : ResponseEntity.notFound().build();
	}

	@ApiOperation(value = "Salvar produto")
	@PostMapping
	public ResponseEntity<ProdutoResponseDTO> salvar(@PathVariable Long codigoCategoria, @Valid @RequestBody ProdutoRequestDTO produto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoResponseDTO.converterParaProdutoDTO(produtoServico.salvar(codigoCategoria, produto.converterParaEntidade(codigoCategoria))));
	}

	@ApiOperation(value = "Atualizar produto")
	@PutMapping("/{id}")
	public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long codigoCategoria, @PathVariable Long codigoProduto,
			@Valid @RequestBody ProdutoRequestDTO produto) {
		Produto produtoAtualizado = produtoServico.atualizar(codigoCategoria, codigoProduto, produto.converterParaEntidade(codigoCategoria, codigoProduto));
		return ResponseEntity.ok(ProdutoResponseDTO.converterParaProdutoDTO(produtoAtualizado));
	}

	@ApiOperation(value = "Deletar produto")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long codigoCategoria, @PathVariable Long codigoProduto) {
		produtoServico.deletar(codigoCategoria, codigoProduto);
	}
}
