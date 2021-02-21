package br.com.lucas.votacao.jms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class ConfigurationBean {

	
	@Value("${votacao.broker.publisher.username}")
	private @Getter String username;
	@Value("${votacao.broker.publisher.password}")
	private @Getter String password;

	@Value("${spring.activemq.broker-url}")
	private @Getter String urlBrokerResultado;
	@Value("${votacao.broker.topic.resultadoVotacao}")
	private @Getter String resultadoVotacaoTopic;

}
