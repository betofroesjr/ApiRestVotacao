package br.com.lucas.votacao.util;

import org.springframework.web.client.RestTemplate;

import br.com.lucas.votacao.model.VO.StatusCpf;

public class ValidarCpf {
	
	
	public static StatusCpf consultar(String cpf) {
		
		try {
			
			final String uri = "https://user-info.herokuapp.com/users/"+removerCaracter(cpf);
			
			RestTemplate restTemplate = new RestTemplate();
			
			 StatusCpf result = restTemplate.getForObject(uri, StatusCpf.class);
			
			return result;
			
		} catch (Exception e) {
			return null;
		}
		
	}
	
	
	public static String removerCaracter(String cpf) {
		
		String caracterRemovidos = cpf.replaceAll("[^a-zA-Z0-9]", "");
		
		return caracterRemovidos;
	}

}
