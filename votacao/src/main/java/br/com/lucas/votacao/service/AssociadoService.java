package br.com.lucas.votacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.lucas.votacao.exception.EntidadeEmUsoException;
import br.com.lucas.votacao.exception.EntidadeNaoEncontradaException;
import br.com.lucas.votacao.model.Associado;
import br.com.lucas.votacao.repository.AssociadoRepository;

@Service
public class AssociadoService {
	
	@Autowired
	private AssociadoRepository associadoRepository;
	
	public Associado salvar(Associado associado) {
		return associadoRepository.save(associado);
	}
	
	
	public int verificarCpfJaCadastrado(String cpf) {
		
		return associadoRepository.consultarCpf(cpf);
		
	}
	
	public void excluir(Long idAssociado) {
		try {
			associadoRepository.deleteById(idAssociado);
			
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
				String.format("Não existe um cadastro de associado com código %d", idAssociado));
		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format("Associado de código %d não pode ser removida, pois está em uso", idAssociado));
		}
	}
}
