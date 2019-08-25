package dominio;

import javax.faces.application.FacesMessage;

public class Mensagem {

	private String mensagem;
	private String tipo;
	private FacesMessage.Severity facesMessage;

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public FacesMessage.Severity getFacesMessage() {
		return facesMessage;
	}

	public void setFacesMessage(FacesMessage.Severity facesMessage) {
		this.facesMessage = facesMessage;
	}

}
