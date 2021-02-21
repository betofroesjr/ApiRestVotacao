package br.com.lucas.votacao.exception;

public class SessaoInvativaException  extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public SessaoInvativaException(String mensagem) {
		super(mensagem);
	}

}
