package br.com.lucas.votacao.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TBSESSAO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sessao implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@ApiModelProperty(value = "Id Sessao", hidden = true)
	private Long idSessao;
	
	@Column(name = "TEMPO_DURACAO")
	@ApiModelProperty(value = "Tempo Duração Sessao em minutos")
	private long tempoDuracao;
	
	@Column(name = "NOME")
	@ApiModelProperty(value = "Nome da Pauta", required = true)
	@NotNull
	private String nome;
	
	@Column(nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@ApiModelProperty(value = "Ativo ou Inativo da Sessao", required = true)
	@NotNull
	private Boolean ativo;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@ApiModelProperty(value = "Objeto Pauta")
	private List<Pauta> pauta;
	
	
}
