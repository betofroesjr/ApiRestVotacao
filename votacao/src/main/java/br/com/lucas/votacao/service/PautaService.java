package br.com.lucas.votacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.lucas.votacao.exception.EntidadeEmUsoException;
import br.com.lucas.votacao.exception.EntidadeNaoEncontradaException;
import br.com.lucas.votacao.model.Pauta;
import br.com.lucas.votacao.repository.PautaRepository;

@Service
public class PautaService {
	
	@Autowired
	private PautaRepository pautaRepository;
	
	public Pauta salvar(Pauta pauta) {
		return pautaRepository.save(pauta);
	}
	
	
	public void excluir(Long idPauta) {
		try {
			pautaRepository.deleteById(idPauta);
			
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
				String.format("Não existe uma pauta de associado com código %d", idPauta));
		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format("Pauta de código %d não pode ser removida, pois está em uso", idPauta));
		}
	}
}
