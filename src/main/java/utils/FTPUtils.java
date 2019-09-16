package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.faces.context.FacesContext;

import org.apache.commons.net.ftp.FTPClient;

import dominio.Arquivo;

public class FTPUtils {
	private FTPClient ftp;
	private Properties props;
	private Boolean ativo;

	public FTPUtils() {
		props = PropertiesLoader.propertiesFTPLoader();
		ftp = new FTPClient();

	}

	public void inciarFTP() {
		ativo = true;
		try {
			ftp.connect(props.getProperty("hostname"));
			ftp.login(props.getProperty("username"), props.getProperty("password"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Erro ao iniciar conex�o FTP: " + e.getMessage());
		}

	}

	public void encerraFTP() {
		ativo = false;
		try {
			ftp.logout();
			ftp.disconnect();
		} catch (IOException e) {
			System.err.println("Erro ao encerrar conex�o FTP: " + e.getMessage());
		}
	}

	public void criarDiretorioArquivo(Arquivo arquivo) throws IOException {
		String[] pastas = arquivo.getCaminhoArquivo().split(File.separator);
		ftp.changeWorkingDirectory("imagens");
		for (String pasta : pastas) {
			ftp.makeDirectory(pasta);
			ftp.changeWorkingDirectory(pasta);
		}
	}
	
	public void receberTodosArquivosPasta(String pasta) throws IOException {
		String[] pastas = pasta.split(File.separator);
		inciarFTP();
		for(String p : pastas) {
			ftp.changeWorkingDirectory(p);
		}
		String[] arquivos = ftp.listNames();
		for(String a : arquivos) {
			receberArquivo(a, File.separator+"imagens"+File.separator+pasta);
		}
		for(String a : arquivos) {
			receberArquivo(a, File.separator+"imagens"+File.separator+pasta.replace("Original", "Tratada"));
		}
		encerraFTP();

	}

	public boolean enviarArquivo(InputStream arqEnviar, Arquivo arquivo) {
		try {
			Boolean enviado = true;
			inciarFTP();
			criarDiretorioArquivo(arquivo);
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			enviado = enviado && ftp.storeFile(arquivo.getNomeArquivo(), arqEnviar);
			arqEnviar.close();
			encerraFTP();

			return enviado;
		} catch (IOException e) {
			System.err.println("Erro ao enviar arquivo: " + e.getMessage());
			throw new RuntimeException();
		}

	}

	public void receberArquivo(String nomeArquivo, String nomeCaminhoRecebido) {
		try {
			System.out.println(ftp.listNames());
			FileOutputStream fos = new FileOutputStream(
					FacesContext.getCurrentInstance().getExternalContext().getRealPath(nomeCaminhoRecebido+File.separator+nomeArquivo));
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.retrieveFile(nomeArquivo, fos);
		} catch (IOException e) {
			System.err.println("Erro ao receber arquivo: " + e.getMessage());
		}

	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getAtivo() {
		return ativo;
	}

}
