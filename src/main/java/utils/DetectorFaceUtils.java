package utils;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;

import java.io.File;

import javax.faces.context.FacesContext;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

public class DetectorFaceUtils {

	private final CascadeClassifier classificadorDeteccao = new CascadeClassifier(FacesContext.getCurrentInstance().getExternalContext()
			.getRealPath(File.separator+"WEB-INF"+File.separator+"classes"+File.separator+"recursos"+File.separator+"haarcascade-frontalface-alt.xml"));
	
	private final FaceRecognizer classificadorReconhecimento = LBPHFaceRecognizer.create();
			
	
	public void reconhecer(Mat faceCapturada) {
		IntPointer rotulo = new IntPointer(1);
        DoublePointer confianca = new DoublePointer(1);
		classificadorReconhecimento.read(FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath(File.separator+"WEB-INF"+File.separator+"classes"+File.separator+"recursos"+File.separator+"classificadorLBPHYale.yml"));
		classificadorReconhecimento.predict(faceCapturada, rotulo, confianca);
		String nome;
		int predicao = rotulo.get(0);
        if (predicao == -1) {
            nome = "Desconhecido";
        } else {
            System.err.println("Encontrou");
        }
	}

	public DetectorFaceUtils() {
		// TODO Auto-generated constructor stub
	}
	

	public Mat imagemParaMatriz(Frame imagem) {
		OpenCVFrameConverter.ToMat conversorMatriz = new OpenCVFrameConverter.ToMat();
		return conversorMatriz.convert(imagem);
	}

	public RectVector detectaFaces(Mat imagemColorida) {
		Mat imagemCinza = new Mat();
		cvtColor(imagemColorida, imagemCinza, COLOR_BGRA2GRAY);
		RectVector facesDetectadas = new RectVector();
 		
		classificadorDeteccao.detectMultiScale(imagemCinza, facesDetectadas, 1.1, 1, 0, new Size(150, 150), new Size(500, 500));
		return facesDetectadas;
	}

	public Rect dadosFace(Rect dadosFace, Mat imagemColorida) {
		rectangle(imagemColorida, dadosFace, new Scalar(0, 0, 255, 0));
		return dadosFace;
	}

	public Mat detectaFacesRetangulo(Mat imagemColorida) {
		RectVector facesDetectadas = detectaFaces(imagemColorida);
		for (int i = 0; i < facesDetectadas.size(); i++) {
			Rect dadosFace = dadosFace(facesDetectadas.get(i), imagemColorida);
		}
		return imagemColorida;
	}


	public Mat tratarImagem(final String path) throws Exception {
		File imagem = new File(path);
		Mat imagemColorida = imread(imagem.getAbsolutePath());
		RectVector facesDetectadas = detectaFaces(imagemColorida);

		Mat imagemCinza = new Mat();
		cvtColor(imagemColorida, imagemCinza, COLOR_BGRA2GRAY);
		Mat faceCapturada = null;
		if (facesDetectadas.size() == 1) {
			for (int i = 0; i < facesDetectadas.size(); i++) {
				Rect dadosFace = dadosFace(facesDetectadas.get(i), imagemColorida);
				faceCapturada = new Mat(imagemCinza, dadosFace);
				resize(faceCapturada, faceCapturada, new Size(160, 160));
			}
		} else {
			throw new Exception("A foto contem mais de um rosto.");
		}
		return faceCapturada;

	}

}
