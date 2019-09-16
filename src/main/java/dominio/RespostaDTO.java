package dominio;

public class RespostaDTO {

	private int percentualConfiabilidade;
	private Pessoa pessoa;

	
	public RespostaDTO() {
		// TODO Auto-generated constructor stub
	}


	public int getPercentualConfiabilidade() {
		return percentualConfiabilidade;
	}


	public void setPercentualConfiabilidade(int percentualConfiabilidade) {
		this.percentualConfiabilidade = percentualConfiabilidade;
	}


	public Pessoa getPessoa() {
		return pessoa;
	}


	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	
}
