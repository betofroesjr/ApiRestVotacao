package br.com.lucas.votacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.lucas.votacao.exception.EntidadeEmUsoException;
import br.com.lucas.votacao.exception.EntidadeNaoEncontradaException;
import br.com.lucas.votacao.exception.SessaoInvativaException;
import br.com.lucas.votacao.model.Votos;
import br.com.lucas.votacao.repository.VotosRepository;

@Service
public class VotacaoService {
	
	@Autowired
	private VotosRepository votosRepository;
	
	public Votos salvar(Votos votos) {
		
		Boolean isSessaoAtivo = votos.getSessoes().stream().anyMatch(sessao -> sessao.getAtivo());
			
		if (isSessaoAtivo == true) {
				
			return votosRepository.save(votos);
				
		} else {
				
			throw new SessaoInvativaException(
					String.format("A sessão de id %d está invativa.. voto não contabilizado", votos.getIdVoto()));
		}
	}
	
	public void excluir(Long idVoto) {
		try {
			votosRepository.deleteById(idVoto);
			
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
				String.format("Não existe um voto de associado com código %d", idVoto));
		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format("voto de código %d não pode ser removida, pois está em uso", idVoto));
		}
	}
	
	public int verificarSeAssociadoJaVotou(Long idAssociado, Long idPauta) {
		
		return votosRepository.verificarQtdVotosPorAssociado(idAssociado, idPauta);
	}
	
	public int retornarNumeroDeVotosPorPauta(Long idPauta) {
		
		return votosRepository.verificarQtdVotosPorPauta(idPauta);
	}
}
