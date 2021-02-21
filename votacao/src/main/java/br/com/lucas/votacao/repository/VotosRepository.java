package br.com.lucas.votacao.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.lucas.votacao.model.Votos;

@Repository
public interface VotosRepository  extends JpaRepository<Votos, Long>{
	
	
	@Query("SELECT count(*) FROM Votos voto LEFT JOIN  voto.associado asso LEFT JOIN voto.sessoes sessao LEFT JOIN  sessao.pauta pauta WHERE asso.idAssociado = :idAssociado AND pauta.idPauta = :idPauta")
	int verificarQtdVotosPorAssociado(Long idAssociado, Long idPauta);
	
	@Query("SELECT count(*) FROM Votos voto LEFT JOIN voto.sessoes sessao LEFT JOIN  sessao.pauta pauta WHERE pauta.idPauta = :idPauta")
	int verificarQtdVotosPorPauta(Long idPauta);


}
