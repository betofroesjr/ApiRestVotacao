package br.com.lucas.votacao.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import br.com.lucas.votacao.model.VO.StatusCpf;
import br.com.lucas.votacao.model.types.StatusConsultaCpf;
import br.com.lucas.votacao.util.ValidarCpf;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TBASSOCIADO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Associado  implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@ApiModelProperty(value = "Id Associado", hidden = true)
	private Long idAssociado;
	
	@Column(name = "CPF")
	@ApiModelProperty(value = "Cpf Associado", required = true)
	@NotNull
	private String cpf;
	
	@Column(name = "NOME")
	@ApiModelProperty(value = "Nome Associado", required = true)
	@NotNull
	private String nome;
	
	@Column(name = "DATA_NASCIMENTO")
	@ApiModelProperty(value = "Data Nascimmento Associado", required = true)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd[.SSS][.SS][.S]")
	@NotNull
	private LocalDate dataNascimento;
	
	@Transient
	@ApiModelProperty(value = "", hidden = true)
	@JsonIgnore
	public Boolean isCpfValido() {
		
		StatusCpf isHabilitado = ValidarCpf.consultar(this.getCpf());
		
		if(isHabilitado != null ) {
			
			if (isHabilitado.getStatus().equals(StatusConsultaCpf.ABLE_TO_VOTE.getDescricao())) {
				
				return true;
			}
		}
		
		return false;
	}
	
}
