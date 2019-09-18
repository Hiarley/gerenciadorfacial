package bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import dominio.Pessoa;
import dominio.lazy.PessoaLazyDataModel;
import service.PessoaService;
import utils.ReconhecimentoLBPH;
import utils.TreinamentoLBPH;

@Named
@SessionScoped
public class PessoaListMB implements Serializable {

	private LazyDataModel<Pessoa> pessoas;
	@EJB
	private PessoaService pessoaService;
	
	private TreinamentoLBPH treinamento = new TreinamentoLBPH();

	public PessoaListMB() {

	}

	@PostConstruct
	public void init() {
		pessoas = new PessoaLazyDataModel(pessoaService);
	}

	public LazyDataModel<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(LazyDataModel<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}
	
	public void treinar() {
		treinamento.treinar(pessoaService.findAllCpf());
	}
	
	

}
