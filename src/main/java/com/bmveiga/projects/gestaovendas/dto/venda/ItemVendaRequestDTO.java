package com.bmveiga.projects.gestaovendas.dto.venda;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Itens da venda requisição DTO")
public class ItemVendaRequestDTO {

	@ApiModelProperty(value = "Código Produto")
	@NotNull(message = "Codigo Produto")
	private Long codigoProduto;

	@ApiModelProperty(value = "Quantidade")
	@NotNull(message = "Quantidade")
	@Min(value = 1, message="Quantidade")
	private Integer quantidade;

	@ApiModelProperty(value = "Preço Vendido")
	@NotNull(message = "Preço vendido")
	private BigDecimal precoVendido;

	public Long getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(Long codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getPrecoVendido() {
		return precoVendido;
	}

	public void setPrecoVendido(BigDecimal precoVendido) {
		this.precoVendido = precoVendido;
	}

}
