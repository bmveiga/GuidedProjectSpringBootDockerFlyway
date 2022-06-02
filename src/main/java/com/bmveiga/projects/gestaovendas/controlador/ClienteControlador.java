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

import com.bmveiga.projects.gestaovendas.dto.categoria.CategoriaRequestDTO;
import com.bmveiga.projects.gestaovendas.dto.categoria.CategoriaResponseDTO;
import com.bmveiga.projects.gestaovendas.dto.cliente.ClienteRequestDTO;
import com.bmveiga.projects.gestaovendas.dto.cliente.ClienteResponseDTO;
import com.bmveiga.projects.gestaovendas.entidades.Categoria;
import com.bmveiga.projects.gestaovendas.entidades.Cliente;
import com.bmveiga.projects.gestaovendas.servico.ClienteServico;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Cliente")
@RestController
@RequestMapping("/clientes")
public class ClienteControlador {

	@Autowired
	private ClienteServico clienteServico;

	@ApiOperation(value = "Listar Todos")
	@GetMapping
	public List<ClienteResponseDTO> listarTodas() {
		return clienteServico.listarTodas().stream().map(cliente -> ClienteResponseDTO.converterparaClienteDTO(cliente))
				.collect(Collectors.toList());
	}

	@ApiOperation(value = "Buscar por Id")
	@GetMapping("/{id}")
	public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
		Optional<Cliente> cliente = clienteServico.buscarPorId(id);
		return cliente.isPresent() ? ResponseEntity.ok(ClienteResponseDTO.converterparaClienteDTO(cliente.get()))
				: ResponseEntity.notFound().build();
	}
	
	@ApiOperation(value = "Salvar cliente")
	@PostMapping
	public ResponseEntity<ClienteResponseDTO> salvar(@Valid @RequestBody ClienteRequestDTO cliente) {
		Cliente clienteSalvo = clienteServico.salvar(cliente.converterParaEntidade());
		return ResponseEntity.status(HttpStatus.CREATED).body(ClienteResponseDTO.converterparaClienteDTO(clienteSalvo));
	}
	
	@ApiOperation(value = "Atualizar cliente")
	@PutMapping("/{id}")
	public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO cliente){
		Cliente clienteAtualizado = clienteServico.atualizar(id, cliente.converterParaEntidade(id));
		return ResponseEntity.ok(ClienteResponseDTO.converterparaClienteDTO(clienteAtualizado));
	}
	
	@ApiOperation(value = "Deletar cliente")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id) {
		clienteServico.deletar(id);
	}

}
