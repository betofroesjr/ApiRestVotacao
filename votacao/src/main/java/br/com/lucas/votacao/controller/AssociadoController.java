package br.com.lucas.votacao.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.lucas.votacao.exception.CpfInvalidoException;
import br.com.lucas.votacao.exception.EntidadeEmUsoException;
import br.com.lucas.votacao.exception.EntidadeNaoEncontradaException;
import br.com.lucas.votacao.model.Associado;
import br.com.lucas.votacao.repository.AssociadoRepository;
import br.com.lucas.votacao.service.AssociadoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/associados")
@Api(value = "Associados")
public class AssociadoController  {
	
	private @Autowired AssociadoRepository associadoRepository;
	
	private @Autowired AssociadoService associadoService;
	
	private final Logger log = LoggerFactory.getLogger(AssociadoController.class);
	
	
	@GetMapping
	@ApiOperation(value = "Mostra lista de Associados")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Retorna a lista de Associados"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public List<Associado> listar(){
		
		log.info("Listado associados com sucesso!!!");
		return associadoRepository.findAll();
		
	}
	
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Consultar lista de Associados por id do associado")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Retorna a lista de Associados pelo Id"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public ResponseEntity<Associado> buscar(@PathVariable Long id) {
		Optional<Associado> associado = associadoRepository.findById(id);
		
		if (associado.isPresent()) {
			return ResponseEntity.ok(associado.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Adicionar Associados")
	@ApiResponses(value = {
		    @ApiResponse(code = 201, message = "Retorna a criação do  Associados"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public  ResponseEntity<String> adicionar(@RequestBody Associado associado) {
		
		if(associado.isCpfValido()) {
			
			if(associadoService.verificarCpfJaCadastrado(associado.getCpf()) == 0) {
				
				associadoService.salvar(associado);
				
			}else {
				log.info("Cpf " + associado.getCpf() + "  já cadastrado!");
				
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cpf " + associado.getCpf() + "  já cadastrado!");
			}
			
			log.info("Associado " + associado.getNome() + " cadastrado com sucesso!");
			return ResponseEntity.status(HttpStatus.CREATED).body("Associado " + associado.getNome() + " cadastrado com sucesso!");
			 
		}else {
			
			log.info("Cpf " + associado.getCpf() + " invalido!");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cpf " + associado.getCpf() + " invalido!");
			
		}
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Atualizar Associados")
	@ApiResponses(value = {
		    @ApiResponse(code = 201, message = "Atualiza um Associados pelo Id"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public ResponseEntity<Associado> atualizar(@PathVariable Long id,
			@RequestBody Associado associado) {
		Optional<Associado> associadoAtual = associadoRepository.findById(id);
		
		if (associadoAtual.isPresent()) {
			BeanUtils.copyProperties(associado, associadoAtual.get(), "id");
			
			Associado associadoSalvo = associadoService.salvar(associadoAtual.get());
			
			log.info("Associado atualizado com sucesso. ");
			return ResponseEntity.ok(associadoSalvo);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Remover Associados")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Remove um Associados pelo Id"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public ResponseEntity<?> remover(@PathVariable Long id) {
		try {
			
			Associado associadoRecuperado = associadoRepository.findById(id).get();
			
			associadoRepository.delete(associadoRecuperado);
			
			log.info("removido associado com sucesso!! ");
			
			return ResponseEntity.noContent().build();
			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
			
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(e.getMessage());
		}
	}
	
}
