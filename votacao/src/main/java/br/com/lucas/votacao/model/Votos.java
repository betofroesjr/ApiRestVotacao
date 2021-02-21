package br.com.lucas.votacao.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TBVOTOS")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Votos implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@ApiModelProperty(value = "Id Voto",hidden = true)
	private Long idVoto;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@ApiModelProperty(value = "Objeto Associado", required =  true)
	private Set<Associado> associado = new HashSet<Associado>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	@ApiModelProperty(value = "Objeto Pauta", required =  true)
	private List<Sessao> sessoes;
	
	@Column(name = "VOTO")
	@ApiModelProperty(value = "Voto")
	@NotNull
	private String voto;

}
