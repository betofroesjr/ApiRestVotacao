package br.com.lucas.votacao.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.lucas.votacao.model.Associado;

@Repository
public interface AssociadoRepository  extends JpaRepository<Associado, Long>{
	
	
	@Query("SELECT count(*) FROM Associado associado WHERE associado.cpf = :cpf")
	int consultarCpf(String cpf);

}
