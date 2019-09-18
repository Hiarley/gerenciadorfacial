package dominio.lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import dominio.Pessoa;
import service.PessoaService;

public class PessoaLazyDataModel extends LazyDataModel<Pessoa> {
	
	List<Pessoa> pessoas;
	PessoaLazyFilter filtro;
	@EJB
	PessoaService pessoaService;
	
	public PessoaLazyDataModel(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
		this.filtro = new PessoaLazyFilter();
		pessoas = new ArrayList<>();
	}

	@Override
	public Object getRowKey(Pessoa tbl) {
		return tbl.getId();
	}

	@Override
	public List<Pessoa> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {

		filtro.setPrimeiroRegistro(first);
		filtro.setQuantidadeRegistros(pageSize);
		filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
		filtro.setPropriedadeOrdenacao(sortField);
		filtro.setFindBy(filters);
		
		
		setRowCount(pessoaService.quantidadeFiltrados(filtro));
		pessoas = pessoaService.filtrados(filtro);

		return pessoas;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}


	
}
