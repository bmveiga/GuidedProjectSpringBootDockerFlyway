package com.bmveiga.projects.gestaovendas.servico;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.bmveiga.projects.gestaovendas.entidades.Produto;
import com.bmveiga.projects.gestaovendas.excecao.RegraNegocioException;
import com.bmveiga.projects.gestaovendas.repositorio.ProdutoRepositorio;

@Service
public class ProdutoServico {

	@Autowired
	private ProdutoRepositorio produtoRepositorio;
	
	@Autowired
	private CategoriaServico categoriaServico;
	
	public List<Produto> listarTodos(Long codigoCategoria){
		return produtoRepositorio.findByCategoriaCodigo(codigoCategoria);
	}
	
	public Optional<Produto> buscarPorCodigo(Long codigo, Long codigoCategoria) {
		return produtoRepositorio.findByCodigoAndCategoriaCodigo(codigo, codigoCategoria);
	}
	
	public Produto salvar(Long codigoCategoria, Produto produto) {
		validarCategoriaProdutoExiste(codigoCategoria);
		validarProdutoDuplicado(produto);
		return produtoRepositorio.save(produto);
	}
	
	public Produto atualizar(Long codigoCategoria, Long codigo, Produto produto) {
		Produto produtoSalvar = validarProdutoExiste(codigo, codigoCategoria);
		validarCategoriaProdutoExiste(codigoCategoria);
		validarProdutoDuplicado(produto);
		BeanUtils.copyProperties(produto, produtoSalvar, "codigo");
		return produtoRepositorio.save(produtoSalvar);
	}
	
	public void deletar(Long codigoCategoria, Long codigo) {
		Produto produto = validarProdutoExiste(codigo, codigoCategoria);
		produtoRepositorio.delete(produto);
	}
	
	protected void atualizarQuantidadeAposVenda(Produto produto) {
		produtoRepositorio.save(produto);
	}
	
	protected Produto validarProdutoExiste(Long codigo) {
		Optional<Produto> produto = produtoRepositorio.findById(codigo);
		if(produto.isEmpty()) {
			throw new RegraNegocioException(String.format("Produto de código %s não encontrado", produto));
		}
		return produto.get();
	}
	
	private Produto validarProdutoExiste(Long codigo, Long codigoCategoria) {
		Optional<Produto> produto = buscarPorCodigo(codigo, codigoCategoria);
		if(produto.isEmpty()) {
			throw new EmptyResultDataAccessException(1);
		}
		return produto.get();
	}

	private void validarProdutoDuplicado(Produto produto) {
		Optional<Produto> produtoPorDescricao = produtoRepositorio.findByCategoriaCodigoAndDescricao(produto.getCategoria().getCodigo(), produto.getDescricao());
		if(produtoPorDescricao.isPresent() && produtoPorDescricao.get().getCodigo() != produto.getCodigo()) {
			throw new RegraNegocioException(String.format("O produto %s já existe no cadastro", produto.getDescricao()));
		}
	}
	
	private void validarCategoriaProdutoExiste(Long codigoCategoria) {
		if(codigoCategoria == null) {
			throw new RegraNegocioException("A categoria não pode ser nula!");
		} 
		if(categoriaServico.buscarPorId(codigoCategoria).isEmpty()) {
			throw new RegraNegocioException(String.format("A categoria de código %s não existe no cadastro", codigoCategoria));
		}
	}
}
