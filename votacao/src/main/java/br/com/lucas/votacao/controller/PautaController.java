package br.com.lucas.votacao.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.lucas.votacao.exception.EntidadeEmUsoException;
import br.com.lucas.votacao.exception.EntidadeNaoEncontradaException;
import br.com.lucas.votacao.model.Pauta;
import br.com.lucas.votacao.repository.PautaRepository;
import br.com.lucas.votacao.service.PautaService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/pautas")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PautaController {
	
	private @Autowired PautaRepository pautaRepository;
	
	private @Autowired PautaService pautaService;
	
	private static Logger log = LoggerFactory.getLogger(PautaController.class);
	
	
	
	@GetMapping
	@ApiOperation(value = "Mostra lista de pautas")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Retorna a lista de pautas"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public List<Pauta> listar(){
		log.info("pauta lista com sucesso!");
		return pautaRepository.findAll();
		
	}
	
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Mostra uma pauta buscada por Id")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Retorna a lista de pautas por id"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public ResponseEntity<Pauta> buscar(@PathVariable Long id) {
		Optional<Pauta> pauta = pautaRepository.findById(id);
		
		if (pauta.isPresent()) {
			return ResponseEntity.ok(pauta.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Adiciona uma pauta")
	@ApiResponses(value = {
		    @ApiResponse(code = 201, message = "Retorna a lista de pautas criado com sucesso"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public Pauta adicionar(@RequestBody Pauta pauta) {
		return pautaService.salvar(pauta);
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Atualiza uma lista de pauta")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Atualiza uma pautas"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public ResponseEntity<Pauta> atualizar(@PathVariable Long id,
			@RequestBody Pauta pauta) {
		Optional<Pauta> pautaAtual = pautaRepository.findById(id);
		
		if (pautaAtual.isPresent()) {
			BeanUtils.copyProperties(pauta, pautaAtual.get(), "id");
			
			Pauta pautaSalvo = pautaService.salvar(pautaAtual.get());
			log.info("pauta atualizado com sucesso!");
			return ResponseEntity.ok(pautaSalvo);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Remove uma pauta")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Remove uma pautas"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public ResponseEntity<?> remover(@PathVariable Long id) {
		try {
			
			Pauta pautaRecuperado = pautaRepository.findById(id).get();
			
			pautaRepository.delete(pautaRecuperado);
			
			log.info("pauta removida com sucesso!");
			
			return ResponseEntity.noContent().build();
			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
			
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(e.getMessage());
		}
	}
	
}
