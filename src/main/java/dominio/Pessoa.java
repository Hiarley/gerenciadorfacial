package dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Pessoa {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;
	@Column(name = "nome", nullable = false)
	private String nome;
	@Column(name = "cpf", nullable = false, unique = true)
	private String cpf;
	@Column(name = "dataNascimento", nullable = false)
	private Date dataNascimento;

	@Column(name = "dataCadastro", nullable = false)
	private Date dataCadastro;

	@OneToMany(mappedBy = "pessoa", fetch = FetchType.EAGER)
	private List<Arquivo> fotos;
	@Transient
	private String diretorioFotos;

	public Pessoa() {
		fotos = new ArrayList<>();
	}
	public Pessoa(int id, String cpf) {
		fotos = new ArrayList<>();
		this.id = id;
		this.cpf = cpf;
	}
	
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getDiretorioFotos() {
		return diretorioFotos;
	}

	public void setDiretorioFotos(String diretorioFotos) {
		this.diretorioFotos = diretorioFotos;
	}

	public List<Arquivo> getFotos() {
		return fotos;
	}

	public void setFotos(List<Arquivo> fotos) {
		this.fotos = fotos;
	}

	public String getCpfSemFormato() {
		return this.cpf.replace(".", "").replace("-", "");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
