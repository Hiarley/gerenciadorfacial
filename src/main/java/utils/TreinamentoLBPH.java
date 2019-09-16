package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;

import java.nio.IntBuffer;

import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_face.*;

public class TreinamentoLBPH {
	public static void main(String[] args) {
		File diretorio = new File("/home/ftp/imagens");
		File[] pastas = diretorio.listFiles();
		Map<String, List<File>> fotosPessoas = new HashMap<>();
		List<File> arquivos = new ArrayList<>();

		for (File nomePasta : pastas) {
			List<File> fotosPessoa = Arrays
					.asList(new File(nomePasta.getAbsolutePath() + File.separator + "Original").listFiles());
			arquivos.addAll(fotosPessoa);
			fotosPessoas.put(nomePasta.getName(), fotosPessoa);
			
		}

		MatVector fotos = new MatVector(arquivos.size());
		Mat rotulos = new Mat(arquivos.size(), 1, CV_32SC1);
		IntBuffer rotulosBuffer = rotulos.createBuffer();
		int contador = 0;
		// Pegar todos as pessoas ativas no banco, setar o ID da pessoa na classe após pesquisar pelo map.
	

		for (File imagem : arquivos) {
			Mat foto = imread(imagem.getAbsolutePath(), IMREAD_GRAYSCALE);
			String[] caminhoAbsoluto = imagem.getAbsolutePath().split(File.separator);
			System.out.println(caminhoAbsoluto[4]+caminhoAbsoluto[6].replace(".jpeg", ""));
			int classe = Integer.parseInt(caminhoAbsoluto[4]+caminhoAbsoluto[6].replace(".jpeg", ""));
			resize(foto, foto, new Size(160, 160));
			fotos.put(contador, foto);
			rotulosBuffer.put(contador, classe);
			contador++;
		}

		FaceRecognizer lbph = LBPHFaceRecognizer.create();

		lbph.train(fotos, rotulos);
		lbph.save("/home/ftp/classificadorLBPHYale.yml");

	}
}