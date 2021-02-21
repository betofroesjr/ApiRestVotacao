package br.com.lucas.votacao.model.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;


@AllArgsConstructor
public enum StatusConsultaCpf {
	
	ABLE_TO_VOTE (0L, "ABLE_TO_VOTE"),
	UNABLE_TO_VOTE (1L, "UNABLE_TO_VOTE");
	
    private @Getter @NonNull Long id;
    private @Getter @NonNull String descricao;

}
