/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.helper;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author enkav
 */
public class JSFHelper {
    private static FacesContext getCurrentInstance() {
        return FacesContext.getCurrentInstance();
    }
    
    public static ExternalContext getExtCtx() {
        return getCurrentInstance().getExternalContext();
    }
    
    public static void addMessage(FacesMessage.Severity severity, String summary) {
        addMessage(severity,summary,summary);
    }
    public static void addMessage(FacesMessage.Severity severity, String summary, String details) {
        addMessage(null,severity,summary,details);
    }
    public static void addMessage(String clientId, FacesMessage.Severity severity, String summary, String details) {
        getCurrentInstance().addMessage(clientId, new FacesMessage(severity,summary,details));
    }
}
