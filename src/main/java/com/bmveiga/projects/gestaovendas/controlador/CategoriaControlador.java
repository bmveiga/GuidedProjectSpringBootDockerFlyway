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
import com.bmveiga.projects.gestaovendas.entidades.Categoria;
import com.bmveiga.projects.gestaovendas.servico.CategoriaServico;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Categoria")
@RestController
@RequestMapping("/categorias")
public class CategoriaControlador {

	@Autowired
	private CategoriaServico categoriaServico;

	@ApiOperation(value = "Listar Todos")
	@GetMapping
	public List<CategoriaResponseDTO> listarTodas() {
		return categoriaServico.listarTodas().stream()
				.map(categoria -> CategoriaResponseDTO.converterParaCategoriaDTO(categoria))
				.collect(Collectors.toList());
	}

	@ApiOperation(value = "Buscar por Id")
	@GetMapping("/{id}")
	public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable Long id) {
		Optional<Categoria> categoria = categoriaServico.buscarPorId(id);
		return categoria.isPresent() ? ResponseEntity.ok(CategoriaResponseDTO.converterParaCategoriaDTO(categoria.get())) : ResponseEntity.notFound().build();
	}

	@ApiOperation(value = "Inserir nova categoria")
	@PostMapping
	public ResponseEntity<CategoriaResponseDTO> salvar(@Valid @RequestBody CategoriaRequestDTO categoriaDto) {
		Categoria categoriaSalva = categoriaServico.salvar(categoriaDto.converterParaEntidade());
		return ResponseEntity.status(HttpStatus.CREATED).body(CategoriaResponseDTO.converterParaCategoriaDTO(categoriaSalva));
	}

	@ApiOperation(value = "Alterar categoria")
	@PutMapping("/{id}")
	public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO categoriaDto) {
		Categoria categoriaAtualizada = categoriaServico.atualizar(id, categoriaDto.converterParaEntidade(id));
		return ResponseEntity.ok(CategoriaResponseDTO.converterParaCategoriaDTO(categoriaAtualizada));
	}

	@ApiOperation(value = "Deletar categoria")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		categoriaServico.deletar(id);
	}
}
