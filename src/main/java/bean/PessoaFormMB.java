package bean;

import static utils.Utils.addDetailMessage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import dominio.Arquivo;
import dominio.Pessoa;
import dominio.RegraNegocio;
import service.PessoaService;
import utils.ArquivoUtils;
import utils.FTPUtils;
import utils.ValidatorUtil;

@Named
@SessionScoped
public class PessoaFormMB implements Serializable {

	private Pessoa pessoa;
	private Map<UploadedFile, InputStream> fotos;
	private List<String> images;

	@EJB
	private PessoaService pessoaService;

	public PessoaFormMB() {
		init();
	}

	@PostConstruct
	public void init() {
		pessoa = new Pessoa();
		fotos = new HashMap<>();
		images = new ArrayList<>();
	}

	public String editar(Pessoa pessoa) {
		try {
			this.pessoa = pessoa;
			carregaFotos(pessoa);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return redirect();
	}
	
	public String novoCadastro() {
		init();
		return redirect();
	
	}

	public String redirect() {
		return "/pessoaForm?faces-redirect=true";
	}
	
	public String salvar() {
		try {
			RegraNegocio regraNegocio = null;
			if(ValidatorUtil.isNotEmpty(pessoa.getId())) {
				String diretorioDestino = pessoa.getCpf() + File.separator + "Original";
				List<Arquivo> fotosAdicionadas = salvarNovasFotosLocal(diretorioDestino);
				regraNegocio = pessoaService.salvar(pessoa,fotosAdicionadas);
				fotos = new HashMap<>();
				pessoa = new Pessoa();
			}
			addDetailMessage(regraNegocio.getMensagens());
			return "/pessoaList?faces-redirect=true";
		}catch (Exception e) {
			
			addDetailMessage("Erro:");
			return "";
		}
		
	}

	public void cadastrar() {
		try {
			RegraNegocio regraNegocio = null;
			if (ValidatorUtil.isNotEmpty(pessoa.getCpf())) {
				regraNegocio = pessoaService.validaNegocio(pessoa);
			}
			if (ValidatorUtil.isNotEmpty(regraNegocio) && !regraNegocio.hasError()) {
				String diretorioDestino = pessoa.getCpf() + File.separator + "Original";
				List<Arquivo> fotosSalvas = salvarNovasFotosLocal(diretorioDestino);
				regraNegocio = pessoaService.cadastrarPessoa(pessoa, fotosSalvas);
				fotos = new HashMap<>();
				pessoa = new Pessoa();
				

			}
			addDetailMessage(regraNegocio.getMensagens());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void carregaFotos(Pessoa pessoa) throws IOException {
		images = new ArrayList<>();
		FTPUtils ftp = new FTPUtils();
		String directoryRoot = null;
		if (ValidatorUtil.isNotEmpty(pessoa.getFotos())) {
			directoryRoot = pessoa.getFotos().get(0).getCaminhoArquivo();
			ArquivoUtils.deleteDirectorieAndFiles(directoryRoot);
			ArquivoUtils.createDirectorie(directoryRoot);
			String path;
			ftp.receberTodosArquivosPasta(directoryRoot);
			for (Arquivo a : pessoa.getFotos()) {
				String foto = File.separator+"imagens"+File.separator + a.getCaminhoArquivo() + File.separator + a.getNomeArquivo();
				images.add(foto.replace(File.separator, "/"));
			}
		}
		
	}

	public List<Arquivo> salvarNovasFotosLocal(final String diretorioDestino) throws IOException {
		String[] arquivos = ArquivoUtils.listFilesDirectorie(diretorioDestino);
		List<Arquivo> fotosSalvas = new ArrayList<>();
		int sequencialFoto;
		if (ValidatorUtil.isNotEmpty(arquivos)) {
			sequencialFoto = Integer.parseInt((arquivos[arquivos.length - 1]).split("\\.")[0])+1;
		} else {
			sequencialFoto = 1;
		}
		ArquivoUtils.createDirectorie(diretorioDestino);
		for (UploadedFile foto : fotos.keySet()) {
			Arquivo arquivo = new Arquivo();
			arquivo.setNomeArquivo(sequencialFoto + ".jpeg");
			arquivo.setCaminhoArquivo(diretorioDestino);
			arquivo.setPessoa(pessoa);
			ArquivoUtils.saveByteInFolder(IOUtils.toByteArray(fotos.get(foto)),
					diretorioDestino + File.separator + arquivo.getNomeArquivo());
			pessoa.getFotos().add(arquivo);
			fotosSalvas.add(arquivo);
			sequencialFoto++;
		}
		return fotosSalvas;
	}

	public void upload(FileUploadEvent event) throws IOException {
		if (event != null) {
			fotos.put(event.getFile(), event.getFile().getInputstream());
		}
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Map<UploadedFile, InputStream> getFotos() {
		return fotos;
	}

	public void setFotos(Map<UploadedFile, InputStream> fotos) {
		this.fotos = fotos;
	}

	public List<String> getImages() {
		return images;
	}

}
