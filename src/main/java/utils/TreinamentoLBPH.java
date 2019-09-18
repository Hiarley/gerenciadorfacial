package utils;

import static org.bytedeco.opencv.global.opencv_core.CV_32SC1;
import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;

import java.io.File;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;

import dominio.Pessoa;
import service.PessoaService;

public class TreinamentoLBPH {
	
	@EJB
	PessoaService pessoaService;
	
	
	public void treinar(List<Pessoa> pessoas) {
		File diretorio = new File("/home/ftp/imagens");
		File[] pastas = diretorio.listFiles();
		Map<String, List<File>> pastasPessoa = new HashMap<>();
		Map<Pessoa, List<File>> pessoaArquivos = new HashMap<>();
		int qtdFotos = 0;

		
		
		for (File nomePasta : pastas) {
			List<File> fotosPessoa = Arrays
					.asList(new File(nomePasta.getAbsolutePath() + File.separator + "Original").listFiles());
			pastasPessoa.put(nomePasta.getName(), fotosPessoa);
		}
		for(Pessoa pessoa : pessoas) {
			if(pastasPessoa.containsKey(pessoa.getCpfSemFormato())) {
				pessoaArquivos.put(pessoa, pastasPessoa.get(pessoa.getCpfSemFormato()));
				qtdFotos += pastasPessoa.get(pessoa.getCpfSemFormato()).size();
			}
		}
		

		MatVector fotos = new MatVector(qtdFotos);
		Mat rotulos = new Mat(qtdFotos, 1, CV_32SC1);
		IntBuffer rotulosBuffer = rotulos.createBuffer();
		int contador = 0;
		// Pegar todos as pessoas ativas no banco, setar o ID da pessoa na classe após pesquisar pelo map.
	
		
		for(Pessoa pessoa : pessoas) {
			for(File imagem : pessoaArquivos.get(pessoa)) {
				Mat foto = imread(imagem.getAbsolutePath(), IMREAD_GRAYSCALE);
				resize(foto, foto, new Size(160, 160));
				fotos.put(contador, foto);
				rotulosBuffer.put(contador, pessoa.getId());
				contador++;
			}
		}

		

		FaceRecognizer lbph = LBPHFaceRecognizer.create();

		lbph.train(fotos, rotulos);
		lbph.save("/home/ftp/classificadorLBPHYale.yml");
	}
	
	
	public static void main(String[] args) {
		

	}
}