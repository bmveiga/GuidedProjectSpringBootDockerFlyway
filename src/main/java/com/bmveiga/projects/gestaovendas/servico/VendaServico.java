package com.bmveiga.projects.gestaovendas.servico;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bmveiga.projects.gestaovendas.dto.venda.ClienteVendaResponseDTO;
import com.bmveiga.projects.gestaovendas.dto.venda.ItemVendaRequestDTO;
import com.bmveiga.projects.gestaovendas.dto.venda.VendaRequestDTO;
import com.bmveiga.projects.gestaovendas.dto.venda.VendaResponseDTO;
import com.bmveiga.projects.gestaovendas.entidades.Cliente;
import com.bmveiga.projects.gestaovendas.entidades.ItemVenda;
import com.bmveiga.projects.gestaovendas.entidades.Produto;
import com.bmveiga.projects.gestaovendas.entidades.Venda;
import com.bmveiga.projects.gestaovendas.excecao.RegraNegocioException;
import com.bmveiga.projects.gestaovendas.repositorio.ItemVendaRepositorio;
import com.bmveiga.projects.gestaovendas.repositorio.VendaRepositorio;

@Service
public class VendaServico extends AbstractVendaServico {

	private ClienteServico clienteServico;
	private ItemVendaRepositorio itemVendaRepositorio;
	private VendaRepositorio vendaRepositorio;
	private ProdutoServico produtoServico;

	@Autowired
	public VendaServico(ClienteServico clienteServico, VendaRepositorio vendaRepositorio,
			ItemVendaRepositorio itemVendaRepositorio, ProdutoServico produtoServico) {
		this.clienteServico = clienteServico;
		this.vendaRepositorio = vendaRepositorio;
		this.itemVendaRepositorio = itemVendaRepositorio;
		this.produtoServico = produtoServico;
	}

	public ClienteVendaResponseDTO listarVendaPorCliente(Long codigoCliente) {
		Cliente cliente = validarClienteVendaExiste(codigoCliente);
		List<VendaResponseDTO> vendaResponseDtoList = vendaRepositorio.findByClienteCodigo(codigoCliente).stream().map(
				venda -> criandoVendaResponseDto(venda, itemVendaRepositorio.findByVendaPorCodigo(venda.getCodigo())))
				.collect(Collectors.toList());
		return new ClienteVendaResponseDTO(cliente.getNome(), vendaResponseDtoList);

	}

	public ClienteVendaResponseDTO listarVendaPorCodigo(Long codigoVenda) {
		Venda venda = validarVendaExiste(codigoVenda);
		List<ItemVenda> itensVendaList = itemVendaRepositorio.findByVendaPorCodigo(venda.getCodigo());
		return retornandoClienteVendaResponseDto(venda, itensVendaList);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public ClienteVendaResponseDTO salvar(Long codigoCliente, VendaRequestDTO vendaDto) {
		Cliente cliente = validarClienteVendaExiste(codigoCliente);
		validarProdutoExiste(vendaDto.getItensVendaDto());
		Venda salvarVenda = salvarVenda(cliente, vendaDto);
		return retornandoClienteVendaResponseDto(salvarVenda,
				itemVendaRepositorio.findByVendaPorCodigo(salvarVenda.getCodigo()));
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void deletar (Long codigoVenda) {
		validarVendaExiste(codigoVenda);
		List<ItemVenda> itensVenda = itemVendaRepositorio.findByVendaPorCodigo(codigoVenda);
		validarProdutoExisteEDevolverEstoque(itensVenda);
		itemVendaRepositorio.deleteAll(itensVenda);
		vendaRepositorio.deleteById(codigoVenda);
	}
	
	private void validarProdutoExisteEDevolverEstoque(List<ItemVenda> itensVenda) {
		itensVenda.forEach(item ->{
			Produto produto = produtoServico.validarProdutoExiste(item.getProduto().getCodigo());
			produto.setQuantidade(produto.getQuantidade() + item.getQuantidade());
			produtoServico.atualizarQuantidadeEmEstoque(produto);
		});
	}

	private Venda salvarVenda(Cliente cliente, VendaRequestDTO vendaDto) {
		Venda vendaSalva = vendaRepositorio.save(new Venda(vendaDto.getData(), cliente));
		vendaDto.getItensVendaDto().stream().map(itemVendaDto -> criandoItemVenda(itemVendaDto, vendaSalva))
				.forEach(itemVendaRepositorio::save);
		return vendaSalva;
	}

	private void validarProdutoExiste(List<ItemVendaRequestDTO> itensVendaDto) {
		itensVendaDto.forEach(item -> {
				Produto produto = produtoServico.validarProdutoExiste(item.getCodigoProduto());
				validarQuantidadeProdutoExiste(produto, item.getQuantidade());
				produto.setQuantidade(produto.getQuantidade() - item.getQuantidade());
				produtoServico.atualizarQuantidadeEmEstoque(produto);
		});
	}
	
	private void validarQuantidadeProdutoExiste(Produto produto, Integer quantidade) {
		if(!(produto.getQuantidade() >= quantidade)) {
			throw new RegraNegocioException(String.format("A quantidade %s informada para o produto %s não está disponível em estoque"
					, quantidade, produto.getDescricao()));
		}
	}

	private Venda validarVendaExiste(Long codigoVenda) {
		Optional<Venda> venda = vendaRepositorio.findById(codigoVenda);
		if (venda.isEmpty()) {
			throw new RegraNegocioException(String.format("Venda de código %s não encontrada", codigoVenda));
		}
		return venda.get();
	}

	private Cliente validarClienteVendaExiste(Long codigoCliente) {
		Optional<Cliente> cliente = clienteServico.buscarPorId(codigoCliente);
		if (cliente.isEmpty()) {
			throw new RegraNegocioException(
					String.format("O cliente de código %s não existe no cadastro", codigoCliente));
		}
		return cliente.get();
	}

}
