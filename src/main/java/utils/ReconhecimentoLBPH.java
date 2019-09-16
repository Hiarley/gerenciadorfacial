package utils;

import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;


import java.io.File;
import java.nio.IntBuffer;

import javax.faces.context.FacesContext;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_face.*;

public class ReconhecimentoLBPH {
	
	
	public void treinar() {
		
	}
	
	
	
    public static void main(String[] args) {
        int totalAcertos = 0;
        double percentualAcerto = 0;
        double totalConfianca = 0;
        
        FaceRecognizer reconhecedor = LBPHFaceRecognizer.create();

        reconhecedor.read(FacesContext.getCurrentInstance().getExternalContext()
    			.getRealPath(File.separator+"WEB-INF"+File.separator+"classes"+File.separator+"recursos"
    						+File.separator+"classificadorLBPHYale.yml"));
        
        

        File diretorio = new File("/codigos/teste");
        File[] arquivos = diretorio.listFiles();
        
        for (File imagem : arquivos) {           
            Mat foto = imread(imagem.getAbsolutePath(), IMREAD_GRAYSCALE);
            int classe = Integer.parseInt(imagem.getName().substring(7, 9));          
            resize(foto, foto, new Size(160, 160));

            IntPointer rotulo = new IntPointer(1);
            DoublePointer confianca = new DoublePointer(1);
            reconhecedor.predict(foto, rotulo, confianca);
            int predicao = rotulo.get(0);
            System.out.println(classe + " foi reconhecido como " + predicao + " - " + confianca.get(0));
            if (classe == predicao) {
                totalAcertos++;
                totalConfianca += confianca.get(0);
            }
        }
        
        percentualAcerto = (totalAcertos / 30.0) * 100;
        totalConfianca = totalConfianca / totalAcertos;
        System.out.println("Percentual de acerto: " + percentualAcerto);
        System.out.println("Total confiança: " + totalConfianca);
    }
}