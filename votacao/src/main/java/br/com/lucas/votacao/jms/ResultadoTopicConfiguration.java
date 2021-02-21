package br.com.lucas.votacao.jms;

import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class ResultadoTopicConfiguration {
	
	private @Autowired ConfigurationBean configBean;

	@Bean(name="resultadoVotacaoTopic")
	public Topic createPFCPTopic() {
		ActiveMQTopic topic = new ActiveMQTopic(configBean.getResultadoVotacaoTopic());
		return topic;
	}

	@Bean(name="templateToBrokerResultadoVotacao")
	public JmsTemplate templateToBrokerPFCP() {
		JmsTemplate temp = new JmsTemplate(amqTopicPFCPConnectionFactory());
		return temp;
	}

	@Bean
	public ActiveMQConnectionFactory amqTopicPFCPConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(configBean.getUsername(), configBean.getPassword(), configBean.getUrlBrokerResultado());
		activeMQConnectionFactory.setAlwaysSyncSend(true);
		return activeMQConnectionFactory;
	}

}
