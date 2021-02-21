package br.com.lucas.votacao.controller;

import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
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
import br.com.lucas.votacao.jms.ProducerResultadoVotacaoService;
import br.com.lucas.votacao.model.Sessao;
import br.com.lucas.votacao.repository.SessaoRepository;
import br.com.lucas.votacao.service.SessaoService;
import br.com.lucas.votacao.service.VotacaoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/sessoes")
@EnableScheduling
//@Slf4j
public class SessaoController {
	
	private @Autowired SessaoRepository sessaoRepository;
	
	private @Autowired SessaoService sessaoService;

	private @Autowired VotacaoService votacaoService;
	
	private @Autowired ProducerResultadoVotacaoService notificarTopicResultadoVotacao;
	
	private static Logger log = LoggerFactory.getLogger(SessaoController.class);
	
	
	@GetMapping
	@ApiOperation(value = "Mostra lista de sessoes")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Retorna a lista de sessoes"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public List<Sessao> listar(){
		log.info("Sessões listada com sucesso!!!!");
		return sessaoRepository.findAll();
		
	}
	
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Mostra lista de sessoes por Id")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Retorna a lista de sessoes por Id"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public ResponseEntity<Sessao> buscar(@PathVariable Long id) {
		Optional<Sessao> sessao = sessaoRepository.findById(id);
		
		if (sessao.isPresent()) {
			return ResponseEntity.ok(sessao.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Adiciona lista de sessoes")
	@ApiResponses(value = {
		    @ApiResponse(code = 201, message = "Adicionado sessoes com sucesso"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	@Transactional 
	public Sessao adicionar(@RequestBody Sessao sessao) {
		
		fecharSessao(sessao);

		log.info("Sessões adicionada com sucesso.");
		return sessaoService.salvar(sessao);
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Atualiza lista de sessoes")
	@ApiResponses(value = {
		    @ApiResponse(code = 201, message = "Lista de sessoes atualizado com sucesso"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public ResponseEntity<Sessao> atualizar(@PathVariable Long id,
			@RequestBody Sessao sessao) {
		Optional<Sessao> sessaoAtual = sessaoRepository.findById(id);
		
		if (sessaoAtual.isPresent()) {
			BeanUtils.copyProperties(sessao, sessaoAtual.get(), "id");
			
			Sessao sessaoSalvo = sessaoService.salvar(sessaoAtual.get());
			log.info("Sessões atualizada com sucesso.");
			return ResponseEntity.ok(sessaoSalvo);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Remove lista de sessoes")
	@ApiResponses(value = {
		    @ApiResponse(code = 201, message = "Lista de sessoes removido com sucesso"),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		})
	public ResponseEntity<?> remover(@PathVariable Long id) {
		try {
			
			Sessao sessaoRecuperado = sessaoRepository.findById(id).get();
			
			sessaoRepository.delete(sessaoRecuperado);
			
			log.info("Sessões removida com sucesso.");
			
			return ResponseEntity.noContent().build();
			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
			
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(e.getMessage());
		}
	}
	
	public void fecharSessao(Sessao sessao) {
		
		Timer timer = new Timer(); 
		
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override public void run() { 
				
				if(sessao.getAtivo() == true) {
					
					sessao.setAtivo(false);
					
					atualizar(sessao.getIdSessao(), sessao);
					
					log.info("Sessao fechada com sucesso!");
					
					notificarTopicResultadoVotacao.notifica(sessaoService.resumoVotacao(sessao, votacaoService.retornarNumeroDeVotosPorPauta(sessao.getPauta().get(0).getIdPauta())));
					
					sessaoService.resumoVotacao(sessao, votacaoService.retornarNumeroDeVotosPorPauta(sessao.getPauta().get(0).getIdPauta()));
					
				}else {
					
					log.info("sessão já fechada, nada será feito!");
				}
				
			  
			}
			
		}, sessao.getTempoDuracao() * 1000 *60 ,  sessao.getTempoDuracao() * 1000000000 *60);
		//calculado o tempo para ser em minutos.
	}
	
	//1000 1 second
	@Scheduled(fixedRate = 120000)
	public void sessoesAbertas(){
		
		//System.out.println("verificando sessoes abertas, será finalizada em alguns minutos....");
		
		log.info("verificando sessoes abertas, será finalizada em alguns minutos....");
		
		List<Sessao> sessoes = sessaoRepository.retornarSessoesAbertas();
		
		if (!sessoes.isEmpty()) {
			
			sessoes.forEach(sessao -> {
				
				fecharSessao(sessao);
			});
		}
	}
}
