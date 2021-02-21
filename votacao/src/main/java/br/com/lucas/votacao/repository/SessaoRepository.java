package br.com.lucas.votacao.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.lucas.votacao.model.Sessao;

@Repository
public interface SessaoRepository  extends JpaRepository<Sessao, Long>{
	
	
	@Query("SELECT sessao FROM Sessao sessao WHERE sessao.ativo = true ")
	public List<Sessao> retornarSessoesAbertas();
	

}
