package br.com.lucas.votacao.jms;


import static br.com.lucas.votacao.jms.ActiveMQConfigClient.RESULTADO_TOPPIC;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class ConfigConsumer {

	
	@JmsListener(destination = RESULTADO_TOPPIC, containerFactory="jmsListenerContainerFactory")
	public void receiveMessageResultadoVotacao( String resultadoVotacao) throws Exception {

		if (resultadoVotacao != null) {
			
			System.out.println("Resultado da votação: " + resultadoVotacao);
			
			log.info("Recebendo Resultado da votação: " + resultadoVotacao);

		}
	}

}

