package utils;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.bytedeco.opencv.opencv_core.Mat;

public class ArquivoUtils {

	private static String contextDirectory = FacesContext.getCurrentInstance().getExternalContext()
			.getRealPath(File.separator + "imagens");

	public static byte[] convertFileToByte(File file) {
		FileInputStream fis = null;
		byte[] bArray = new byte[(int) file.length()];
		try {
			fis = new FileInputStream(file);
			fis.read(bArray);
			fis.close();

		} catch (IOException ioExp) {
			ioExp.printStackTrace();
		}
		return bArray;
	}

	public static InputStream readFile(String nameFile) {
		try {

			File file = new File(contextDirectory + File.separator + nameFile);
			if (file.exists()) {
				return new FileInputStream(file);
			}else {
				return null;
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException();
		}
	}

	public static BufferedImage convertByteToImage(byte[] imageData) {
		ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
		try {
			return ImageIO.read(bais);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String[] listFilesDirectorie(String nameDirectorie) {
		File folder = new File(contextDirectory + File.separator + nameDirectorie);
		if (folder.exists()) {
			return folder.list();
		}
		return null;
	}

	public static boolean saveByteInFolder(byte[] bFile, String fileDest) {
		try (FileOutputStream fileOuputStream = new FileOutputStream(contextDirectory + File.separator + fileDest)) {
			fileOuputStream.write(bFile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean createDirectorie(String nameDirectorie) {
		Path path = Paths.get(contextDirectory + File.separator + nameDirectorie);
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
				return true;
			} catch (IOException e) {
				// fail to create directory
				e.printStackTrace();
				return false;
			}
		} else {
			return true;
		}
	}

	public static boolean deleteDirectorieAndFiles(String nameDirectorie) throws IOException {
		File dir = new File(contextDirectory +File.separator+ nameDirectorie);
		FileUtils.deleteDirectory(dir);
		FileUtils.forceDeleteOnExit(dir);

		return FileUtils.getFile(contextDirectory +File.separator+ nameDirectorie).exists();
	}
	
	public static boolean saveFace(String caminho, String nameFile, Mat face) {
		createDirectorie(caminho);
		return imwrite(contextDirectory+File.separator+ caminho+File.separator+nameFile, face);
	}
}
