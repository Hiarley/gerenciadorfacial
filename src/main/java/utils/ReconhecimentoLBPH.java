package utils;

import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;


import java.io.File;
import java.nio.IntBuffer;
import java.util.List;

import javax.faces.context.FacesContext;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_face.*;

import dominio.Pessoa;

public class ReconhecimentoLBPH {
	
	
	public Pessoa reconhecer(File imagem, List<Pessoa> pessoas) {
        int totalAcertos = 0;
        double percentualAcerto = 0;
        double totalConfianca = 0;
        
        FaceRecognizer reconhecedor = LBPHFaceRecognizer.create();

        reconhecedor.read("/home/ftp/classificadorLBPHYale.yml");

        
        Mat foto = imread(imagem.getAbsolutePath(), IMREAD_GRAYSCALE);
        int classe = 4;         
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
        
        percentualAcerto = (totalAcertos / 30.0) * 100;
        totalConfianca = totalConfianca / totalAcertos;
        System.out.println("Percentual de acerto: " + percentualAcerto);
        System.out.println("Total confiança: " + totalConfianca);
        return null;
	}
	
	
	
    public static void main(String[] args) {

    }
}