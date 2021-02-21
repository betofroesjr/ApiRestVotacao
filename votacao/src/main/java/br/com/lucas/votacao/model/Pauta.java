package br.com.lucas.votacao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TBPAUTA")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pauta implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@ApiModelProperty(value = "Id Pauta", hidden = true)
	private Long idPauta;
	
	@Column(name = "TEMA")
	@ApiModelProperty(value = "Tema da Pauta", required = true)
	@NotNull
	private String tema;
	
	@Column(name = "DATA")
	@ApiModelProperty(value = "Data Criação da Pauta", hidden = true)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date data = new Date();
	
	@Column(name = "ABERTA")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@ApiModelProperty(value = "Pauta Aberta ou fechada",required = true)
	@NotNull
	private @Getter Boolean aberta;
	
	
	
	
}
