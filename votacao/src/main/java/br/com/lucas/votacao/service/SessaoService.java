package br.com.lucas.votacao.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.lucas.votacao.exception.EntidadeEmUsoException;
import br.com.lucas.votacao.exception.EntidadeNaoEncontradaException;
import br.com.lucas.votacao.model.Sessao;
import br.com.lucas.votacao.repository.SessaoRepository;

@Service
public class SessaoService {
	
	@Autowired
	private SessaoRepository sessaoRepository;
	
	public Sessao salvar(Sessao sessao) {
		return sessaoRepository.save(sessao);
	}
	
	public void excluir(Long idSessao) {
		try {
			sessaoRepository.deleteById(idSessao);
			
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
				String.format("Não existe uma Sessao de associado com código %d", idSessao));
		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format("Sessao de código %d não pode ser removida, pois está em uso", idSessao));
		}
	}
	
	public String resumoVotacao(Sessao sessao, int qtdVotos) {
		
		StringBuilder mensagem = new StringBuilder();
		
		mensagem.append("------------------" + "\n");
		mensagem.append("Votação encerrada no dia: " +  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "\n");
		mensagem.append("Com pauta em: " + sessao.getPauta().get(0).getTema()+ "\n") ;
		mensagem.append("Na sessão: " + sessao.getNome() + "\n");
		mensagem.append("Com total de: " + qtdVotos + " votos." + "\n");
		mensagem.append("------------------" + "\n");
		
		System.out.println(mensagem);
		
		return mensagem.toString();
	}
}
