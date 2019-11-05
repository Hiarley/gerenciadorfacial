package bean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;

import dominio.Pessoa;
import service.PessoaService;
import utils.ArquivoUtils;
import utils.ReconhecimentoLBPH;
import utils.ValidatorUtil;

@Named
@SessionScoped
public class ReconhecedorMB implements Serializable {

	private static String contextDirectory = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
	private ReconhecimentoLBPH reconhecimento = new ReconhecimentoLBPH();
	private Pessoa pessoa;
	private File file;
	private String filename;
    
   

	@EJB
	private PessoaService pessoaService;

	public void reconhecer() {
		if(ValidatorUtil.isNotEmpty(file)) {
			System.out.println("Uploaded File Name Is :: "+file.getName()+" :: Uploaded File Size :: "+file.length());

			int idPessoa = reconhecimento.reconhecer(file, pessoaService.findAllCpf());
			System.out.println(idPessoa);
			pessoa = pessoaService.findById(idPessoa);
		}

		
	}
	
	public void handleFileUpload(FileUploadEvent event) throws IOException {
		String diretorioDestino = "tmp";
		ArquivoUtils.createDirectorie(diretorioDestino);
		boolean salvou = ArquivoUtils.saveByteInFolder(IOUtils.toByteArray(event.getFile().getInputstream()),
				diretorioDestino + File.separator + event.getFile().getFileName());
		if(salvou) {
			file = new File(FacesContext.getCurrentInstance().getExternalContext()
			.getRealPath(File.separator + "imagens")+File.separator+diretorioDestino+File.separator+event.getFile().getFileName());
		}
    }
	
	
    public void oncapture(CaptureEvent captureEvent) {
        filename = getRandomImageName();
        byte[] data = captureEvent.getData();
        String diretorioDestino = "tmp";
		ArquivoUtils.createDirectorie(diretorioDestino);
 
        String newFileName = FacesContext.getCurrentInstance().getExternalContext()
    			.getRealPath(File.separator + "imagens")+File.separator+diretorioDestino + File.separator + filename + ".png";
        
        
        
        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(newFileName));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
        }catch (Exception e) {
			// TODO: handle exception
		}
          
		/*
		 * boolean salvou = ArquivoUtils.saveByteInFolder(data, diretorioDestino +
		 * File.separator + filename + ".jpeg");
		 */
        file = new File(newFileName);
    }
    private String getRandomImageName() {
        int i = (int) (Math.random() * 10000000);
         
        return String.valueOf(i);
    }
 
    public String getFilename() {
        return filename;
    }

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
