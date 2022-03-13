/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AdminSessionBeanLocal;
import entities.AdminEntity;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import util.exception.InvalidLoginCredentialException;
import util.helper.JSFHelper;

/**
 *
 * @author SCXY
 */
@Named(value = "adminLoginManagedBean")
@RequestScoped
public class AdminLoginManagedBean implements Serializable {

    @EJB(name = "AdminSessionBeanLocal")
    private AdminSessionBeanLocal adminSessionBeanLocal;

    private String username;
    private String password;

    public AdminLoginManagedBean() {
    }

    public void login(ActionEvent event) throws IOException {
        try {
            AdminEntity currentAdminEntity = adminSessionBeanLocal.adminLogin(username, password);
            ExternalContext extCtx = JSFHelper.getExtCtx();

            extCtx.getSession(true);
            extCtx.getSessionMap().put("isLogin", true);
            extCtx.getSessionMap().put("currentAdminEntity", currentAdminEntity);;
            extCtx.redirect(extCtx.getRequestContextPath() + "/index.xhtml");

        } catch (InvalidLoginCredentialException ex) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credential: " + ex.getMessage());
        }
    }

    public void logout(ActionEvent event) throws IOException {
        ExternalContext extCtx = JSFHelper.getExtCtx();

        ((HttpSession) extCtx.getSession(true)).invalidate();
        extCtx.redirect(extCtx.getRequestContextPath() + "/index.xhtml");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public AdminEntity getCurrentAdmin() {
        return (AdminEntity)JSFHelper.getExtCtx().getSessionMap().get("currentAdminEntity");
    }

}
