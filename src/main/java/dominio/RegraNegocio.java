package dominio;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;

import utils.ValidatorUtil;

public class RegraNegocio {

	private List<Mensagem> mensagens;

	public RegraNegocio() {
		mensagens = new ArrayList<>();
	}

	public List<Mensagem> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<Mensagem> mensagens) {
		this.mensagens = mensagens;
	}

	public void addMessageInfo(String stMensagem) {
		addMessage(stMensagem, FacesMessage.SEVERITY_INFO);
	}

	public void addMessageErro(String stMensagem) {
		addMessage(stMensagem, FacesMessage.SEVERITY_ERROR);
	}

	public void addMessageWarm(String stMensagem) {
		addMessage(stMensagem, FacesMessage.SEVERITY_WARN);
	}

	public void addMessageFatal(String stMensagem) {
		addMessage(stMensagem, FacesMessage.SEVERITY_FATAL);
	}

	public void addMessage(String stMensagem, FacesMessage.Severity severityInfo) {
		Mensagem mensagem = new Mensagem();
		mensagem.setMensagem(stMensagem);
		mensagem.setFacesMessage(severityInfo);
		mensagens.add(mensagem);
	}

	public boolean hasError() {
		return ValidatorUtil.isNotEmpty(this.mensagens);
	}

}
