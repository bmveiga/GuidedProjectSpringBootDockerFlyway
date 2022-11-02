package com.bmveiga.projects.gestaovendas.controlador;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bmveiga.projects.gestaovendas.dto.venda.ClienteVendaResponseDTO;
import com.bmveiga.projects.gestaovendas.dto.venda.VendaRequestDTO;
import com.bmveiga.projects.gestaovendas.servico.VendaServico;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Venda")
@RestController
@RequestMapping("/venda")
public class VendaControlador {

	@Autowired
	private VendaServico servico;
	
	@ApiOperation( value = "Listar vendas por cliente")
	@GetMapping("/cliente/{codigoCliente}")
	public ResponseEntity<ClienteVendaResponseDTO> listarVendaPorCliente(@PathVariable Long codigoCliente){
		return ResponseEntity.ok(servico.listarVendaPorCliente(codigoCliente));
	}
	
	@ApiOperation( value = "Listar venda por código")
	@GetMapping("/{codigoVenda}")
	public ResponseEntity<ClienteVendaResponseDTO> listarVendaPorCodigo(@PathVariable Long codigoVenda){
		return ResponseEntity.ok(servico.listarVendaPorCodigo(codigoVenda));
	}
	
	@ApiOperation( value = "Registrar venda", nickname = "salvarVenda")
	@PostMapping("/cliente/{codigoCliente}")
	public ResponseEntity<ClienteVendaResponseDTO> salvar(@PathVariable Long codigoCliente,@Valid @RequestBody VendaRequestDTO vendaDto){
		return ResponseEntity.status(HttpStatus.CREATED).body(servico.salvar(codigoCliente, vendaDto));
	}
	
	@ApiOperation( value = "Deletar venda", nickname = "deletarVenda")
	@DeleteMapping("/{codigoVenda}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long codigoVenda) {
		servico.deletar(codigoVenda);
	}
}
