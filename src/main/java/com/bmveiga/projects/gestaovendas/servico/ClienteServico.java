package com.bmveiga.projects.gestaovendas.servico;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.bmveiga.projects.gestaovendas.entidades.Cliente;
import com.bmveiga.projects.gestaovendas.excecao.RegraNegocioException;
import com.bmveiga.projects.gestaovendas.repositorio.ClienteRepositorio;

@Service
public class ClienteServico {

	@Autowired
	private ClienteRepositorio clienteRepositorio;
	
	public List<Cliente> listarTodas() {
		return clienteRepositorio.findAll();
	}

	public Optional<Cliente> buscarPorId(Long id) {
		return clienteRepositorio.findById(id);
	}
	
	public Cliente salvar(Cliente cliente) {
		validarClienteDuplicado(cliente);
		return clienteRepositorio.save(cliente);
	}
	
	public Cliente atualizar(Long codigo, Cliente cliente) {
		Cliente clienteAtualizar = validarClienteExiste(codigo);
		validarClienteDuplicado(cliente);
		BeanUtils.copyProperties(cliente, clienteAtualizar, "codigo");
		return clienteRepositorio.save(clienteAtualizar);
	}
	
	public void deletar(Long codigo) {
		clienteRepositorio.deleteById(codigo);
	}
	
	private Cliente validarClienteExiste(Long codigo) {
		Optional<Cliente> cliente = buscarPorId(codigo);
		if(cliente.isEmpty()) {
			throw new EmptyResultDataAccessException(1);
		}
		return cliente.get();
	}

	private void validarClienteDuplicado(Cliente cliente) {
		Cliente byNome = clienteRepositorio.findByNome(cliente.getNome());
		if(byNome != null && byNome.getCodigo() != cliente.getCodigo()) {
			throw new RegraNegocioException(String.format("O cliente %s já está cadastrado", cliente.getNome().toUpperCase()));
		}
	}
}
