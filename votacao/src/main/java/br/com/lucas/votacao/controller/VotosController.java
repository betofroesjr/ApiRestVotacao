package br.com.lucas.votacao.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import br.com.lucas.votacao.exception.EntidadeEmUsoException;
import br.com.lucas.votacao.exception.EntidadeNaoEncontradaException;
import br.com.lucas.votacao.exception.SessaoInvativaException;
import br.com.lucas.votacao.model.Votos;
import br.com.lucas.votacao.repository.VotosRepository;
import br.com.lucas.votacao.service.VotacaoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/votos")
//@Slf4j
public class VotosController {
	
	private @Autowired VotosRepository votosRepository;
	
	private @Autowired VotacaoService votosService;
	
	private final Logger log = LoggerFactory.getLogger(VotosController.class);
	
	@GetMapping
	@ApiOperation(value = "Retorna lista de votos")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Lista de votos retornado com sucesso"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public List<Votos> listar(){
		log.info("Sessões listada com sucesso.");
		return votosRepository.findAll();
		
	}
	
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Retorna lista de votos por Id")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Lista de votos retornado com sucesso"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public ResponseEntity<Votos> buscar(@PathVariable Long id) {
		Optional<Votos> votos = votosRepository.findById(id);
		
		if (votos.isPresent()) {
			return ResponseEntity.ok(votos.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Adiciona lista de votos")
	@ApiResponses(value = {
		    @ApiResponse(code = 201, message = "Lista de votos adicionado com sucesso"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public ResponseEntity<String> adicionar(@RequestBody Votos votos) {
		
		try {
			
			if (votosService.verificarSeAssociadoJaVotou(votos.getAssociado().stream().findFirst().get().getIdAssociado(), votos.getSessoes().get(0).getPauta().get(0).getIdPauta()) == 0) {
				
				log.info("Voto registrado com sucesso!");
				
				votosService.salvar(votos);
				
			}else {
				
				log.info("usuário já realizou o voto");
				
				return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("usuário já realizou o voto" );
				
			}
			
			
			ResponseEntity.status(HttpStatus.CREATED).body("Votos realizado com sucesso!");
			
		} catch (SessaoInvativaException e) {
			
			log.info("Sessão atual já encerrada, a mesma não pode receber voto");
			
			return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sessão atual já encerrada, a mesma não pode receber voto." );
		}
		return null;
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Atualiza lista de votos")
	@ApiResponses(value = {
		    @ApiResponse(code = 201, message = "Lista de votos atualizado com sucesso"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public ResponseEntity<Votos> atualizar(@PathVariable Long id,
			@RequestBody Votos votos) {
		Optional<Votos> votosAtual = votosRepository.findById(id);
		
		if (votosAtual.isPresent()) {
			BeanUtils.copyProperties(votos, votosAtual.get(), "id");
			
			Votos votosSalvo = votosService.salvar(votosAtual.get());
			
			log.info("para ficar completo o CRUD, voto atualizado com sucesso.");
			
			return ResponseEntity.ok(votosSalvo);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Remove lista de votos")
	@ApiResponses(value = {
		    @ApiResponse(code = 201, message = "Lista de votos removidos com sucesso"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public ResponseEntity<?> remover(@PathVariable Long id) {
		try {
			
			Votos votosRecuperado = votosRepository.findById(id).get();
			
			votosRepository.delete(votosRecuperado);
			
			log.info("para ficar completo o CRUD, voto removido com sucesso");
			
			return ResponseEntity.noContent().build();
			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
			
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(e.getMessage());
		}
	}
	
}
