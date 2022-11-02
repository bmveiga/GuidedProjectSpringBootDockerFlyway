package com.bmveiga.projects.gestaovendas.servico;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.bmveiga.projects.gestaovendas.dto.venda.ClienteVendaResponseDTO;
import com.bmveiga.projects.gestaovendas.dto.venda.ItemVendaRequestDTO;
import com.bmveiga.projects.gestaovendas.dto.venda.ItemVendaResponseDTO;
import com.bmveiga.projects.gestaovendas.dto.venda.VendaResponseDTO;
import com.bmveiga.projects.gestaovendas.entidades.ItemVenda;
import com.bmveiga.projects.gestaovendas.entidades.Produto;
import com.bmveiga.projects.gestaovendas.entidades.Venda;

public abstract class AbstractVendaServico {

	protected VendaResponseDTO criandoVendaResponseDto(Venda venda, List<ItemVenda> itensVendaList) {
		List<ItemVendaResponseDTO> itensVendaResponseDto = itensVendaList.stream()
				.map(this::criandoItensVendaResponseDto).collect(Collectors.toList());
		return new VendaResponseDTO(venda.getCodigo(), venda.getData(), itensVendaResponseDto);
	}

	protected ItemVendaResponseDTO criandoItensVendaResponseDto(ItemVenda itemVenda) {
		return new ItemVendaResponseDTO(itemVenda.getCodigo(), itemVenda.getQuantidade(), itemVenda.getPrecoVendido(),
				itemVenda.getProduto().getCodigo(), itemVenda.getProduto().getDescricao());
	}

	protected ClienteVendaResponseDTO retornandoClienteVendaResponseDto(Venda venda, List<ItemVenda> itemVendaList) {
		return new ClienteVendaResponseDTO(venda.getCliente().getNome(),
				Arrays.asList(criandoVendaResponseDto(venda, itemVendaList)));
	}

	protected ItemVenda criandoItemVenda(ItemVendaRequestDTO itemVendaDto, Venda venda) {
		return new ItemVenda(itemVendaDto.getQuantidade(), itemVendaDto.getPrecoVendido(),
				new Produto(itemVendaDto.getCodigoProduto()), venda);
	}
}
