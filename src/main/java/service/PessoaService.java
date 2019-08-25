package service;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.bytedeco.opencv.opencv_core.Mat;

import dao.ArquivoDao;
import dao.PessoaDao;
import dominio.Arquivo;
import dominio.Pessoa;
import dominio.RegraNegocio;
import dominio.lazy.PessoaLazyFilter;
import utils.ArquivoUtils;
import utils.DetectorFaceUtils;
import utils.FTPUtils;
import utils.ValidatorUtil;

@Stateless
public class PessoaService {

	@Inject
	private PessoaDao pessoaDao;

	@Inject
	private ArquivoDao arquivoDao;

	private FTPUtils ftpUtils;

	private DetectorFaceUtils detector;

	public PessoaService() {
		ftpUtils = new FTPUtils();
		detector = new DetectorFaceUtils();
	}

	public RegraNegocio validaNegocio(Pessoa pessoa) {
		RegraNegocio erro = new RegraNegocio();
		if (ValidatorUtil.isNotEmpty(pessoa)) {
			if (ValidatorUtil.isNotEmpty(pessoa.getCpf())) {
				Pessoa pessoaExiste = pessoaDao.findByCPF(pessoa.getCpf());
				if (ValidatorUtil.isNotEmpty(pessoaExiste)) {
					erro.addMessageErro("Usuário com cpf " + pessoa.getCpf() + " já cadastrado no sistema.");
				}
			}
		}
		return erro;
	}

	@Transactional
	public RegraNegocio cadastrarPessoa(Pessoa pessoa, List<Arquivo> fotosSalvas) {
		RegraNegocio validador = validaNegocio(pessoa);
		if (validador.hasError()) {
			return validador;
		}
		pessoa.setDataCadastro(new Date());
		validador = salvar(pessoa, fotosSalvas);
		return validador;
	}

	public boolean salvarFotos(List<Arquivo> arquivos) throws Exception {
		Boolean todosEnviados = true;
		for (Arquivo foto : arquivos) {
			String caminhoLocalAbsoluto = foto.getCaminhoArquivo() + File.separator + foto.getNomeArquivo();

			boolean enviado = ftpUtils.enviarArquivo(ArquivoUtils.readFile(caminhoLocalAbsoluto), foto);
			todosEnviados = todosEnviados && enviado;

			if (enviado && processarFotos(foto)) {

				arquivoDao.persist(foto);
			} else {
				break;
			}
		}
		return todosEnviados;
	}

	public boolean processarFotos(final Arquivo arquivo) throws Exception {
		String caminhoLocal =  "imagens"+File.separator+arquivo.getCaminhoArquivo();
		String caminhoLocalAbsoluto = caminhoLocal + File.separator + arquivo.getNomeArquivo();
		Mat faceCapturada = detector
				.tratarImagem(FacesContext.getCurrentInstance().getExternalContext().getRealPath(caminhoLocalAbsoluto));
		caminhoLocal.replace("Original", "Tratada").replace("imagens"+File.separator, "");
		ArquivoUtils.saveFace(caminhoLocal, arquivo.getNomeArquivo(), faceCapturada);
	
		Arquivo arquivoProcessado = new Arquivo();
		arquivoProcessado.setCaminhoArquivo(arquivo.getCaminhoArquivo().replace("Original", "Tratada"));
		arquivoProcessado.setNomeArquivo(arquivo.getNomeArquivo());
		arquivoProcessado.setPessoa(arquivo.getPessoa());

		return ftpUtils.enviarArquivo(ArquivoUtils.readFile(caminhoLocalAbsoluto), arquivoProcessado);
	}

	@Transactional
	public RegraNegocio salvar(Pessoa pessoa, List<Arquivo> fotosAdicionadas) {
		RegraNegocio validador = new RegraNegocio();
		try {
			if (salvarFotos(fotosAdicionadas)) {
				if (pessoa.getId() == 0) {
					pessoaDao.persist(pessoa);
					validador.addMessageInfo("Cadastro realizado com sucesso!");
				} else {
					pessoaDao.merge(pessoa);
					validador.addMessageInfo("Atualização realizada com sucesso!");
				}
			} else {
				validador.addMessageErro("Erro ao salvar as fotos!");
			}
			return validador;
		} catch (Exception e) {
			validador.addMessageErro(e.getMessage());
			return validador;
		}
	}

	public int quantidadeFiltrados(PessoaLazyFilter filtro) {
		// TODO Auto-generated method stub
		return this.pessoaDao.quantidadeFiltrados(filtro);
	}

	public List<Pessoa> filtrados(PessoaLazyFilter filtro) {
		// TODO Auto-generated method stub
		return this.pessoaDao.filtrados(filtro);
	}

	public Pessoa findById(int id) {
		// TODO Auto-generated method stub
		return this.pessoaDao.findById(id);
	}

}
