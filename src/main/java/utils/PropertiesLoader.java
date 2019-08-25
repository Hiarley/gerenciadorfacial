package utils;


import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.faces.context.FacesContext;

public class PropertiesLoader {

	// Crio uma instancia da classe properties
	private static Properties prop = new Properties();


	public static Properties propertiesFTPLoader() {
		// Informo o caminho onde se encontra meu arquivo properties de forma
		// din�mica
		// Poderia fazer isso de forma est�tica passando o diret�rio completo
		// mas obviamente isso n�o � muito interessante.
		String atualDir = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/")
				+ "/WEB-INF/dadosFTP.properties";
		try { // Tento recuperar as informa��es do arquivo de propriedades

			// Crio uma inst�ncia de File passando o meu arquivo .properties via
			// construtor
			File file = new File(atualDir);
			// Agora crio uma inst�ncia de FileInputStream passando via
			// construtor o objeto file instanciado acima
			FileInputStream fileInputStream = new FileInputStream(file);
			// Leio o fileInputStream recuperando assim o mapa contendo chaves e
			// valores
			prop.load(fileInputStream);
			// Fecho o fileInputStream
			fileInputStream.close();
		} catch (Exception e) {
			System.err.println("Erro ao abrir propertis dados FTP: "+ e.getMessage());
		}
		return prop;
		// Retorno um objeto prop com o mapa correspondente ao meu arquivo
		// properties
	}

}
