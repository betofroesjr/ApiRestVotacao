package br.com.lucas.votacao.jms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActiveMQConfigClient {

	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;

	public static final String RESULTADO_TOPPIC = "topic/resultado";
	public static final String ERRO_RESULTADO_QUEUE = "queue/resultado-fail";
}