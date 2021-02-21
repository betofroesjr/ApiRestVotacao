package br.com.lucas.votacao.jms;

import javax.annotation.Resource;
import javax.jms.Topic;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.lucas.votacao.service.IDestination;

@Component
public class ProducerResultadoVotacaoService implements IDestination{
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private @Resource(name="resultadoVotacaoTopic") Topic pfcpTopic;
	private @Resource(name="templateToBrokerResultadoVotacao") JmsTemplate template;
	
	
	@Override
	public void notifica(String resultado) {
		
		try {
		
		template.convertAndSend(this.pfcpTopic, mapper.writeValueAsString(resultado));
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
