package com.bmveiga.projects.gestaovendas.dto.venda;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Cliente da venda retorno DTO")
public class ClienteVendaResponseDTO {

	@ApiModelProperty(value = "Nome Cliente")
	private String nome;

	@ApiModelProperty(value = "Venda")
	private List<VendaResponseDTO> vendaRespondeDTO;

	public ClienteVendaResponseDTO(String nome, List<VendaResponseDTO> vendaRespondeDTO) {
		this.nome = nome;
		this.vendaRespondeDTO = vendaRespondeDTO;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<VendaResponseDTO> getVendaRespondeDTO() {
		return vendaRespondeDTO;
	}

	public void setVendaRespondeDTO(List<VendaResponseDTO> vendaRespondeDTO) {
		this.vendaRespondeDTO = vendaRespondeDTO;
	}

}
