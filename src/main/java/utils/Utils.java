package utils;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;

import org.omnifaces.util.Messages;

import dominio.Mensagem;

/**
 * Created by rmpestano on 07/02/17.
 */
@ApplicationScoped
public class Utils implements Serializable {



    @PostConstruct
    public void init() {
    }
    
    public static void addDetailMessage(List<Mensagem> messages) {
    	for(Mensagem message : messages) {
    		addDetailMessage(message.getMensagem(), message.getFacesMessage());
    	}
    }

    public static void addDetailMessage(String message) {
        addDetailMessage(message, null);
    }

    public static void addDetailMessage(String message, FacesMessage.Severity severity) {

        FacesMessage facesMessage = Messages.create("").detail(message).get();
        if (severity != null && severity != FacesMessage.SEVERITY_INFO) {
            facesMessage.setSeverity(severity);
        }
        Messages.add(null, facesMessage);
    }


}
