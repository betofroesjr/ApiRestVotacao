package br.com.lucas.votacao.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lucas.votacao.model.Pauta;

@Repository
public interface PautaRepository  extends JpaRepository<Pauta, Long>{

}
