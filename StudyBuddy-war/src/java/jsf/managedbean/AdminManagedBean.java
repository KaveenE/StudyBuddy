/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AdminSessionBeanLocal;
import entities.AdminEntity;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.AlreadyExistsException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.helper.JSFHelper;

/**
 *
 * @author SCXY
 */
@Named(value = "adminManagementManagedBean")
@RequestScoped
public class AdminManagedBean {

    @EJB(name = "AdminSessionBeanLocal")
    private AdminSessionBeanLocal adminSessionBeanLocal;

    private AdminEntity newAdminEntity;

    public AdminManagedBean() {
        newAdminEntity = new AdminEntity();
    }

    public void createNewAdmin(ActionEvent event) {
        try {
            Long adminId = adminSessionBeanLocal.createNewAdminEntity(newAdminEntity);
           JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "New admin account created successfully (Admin Id: " + adminId + ")");
        } catch (AlreadyExistsException | UnknownPersistenceException | InputDataValidationException ex) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "An error has occurred while creating the new admin: "+ex);
        } 
    }

    public AdminEntity getNewAdminEntity() {
        return newAdminEntity;
    }

    public void setNewAdminEntity(AdminEntity newAdminEntity) {
        this.newAdminEntity = newAdminEntity;
    }
    
    public Integer getActiveIdx() {
        String srcPage = JSFHelper.getSrcPage();
        if(srcPage.equalsIgnoreCase("index") ) {
            return 0;
        }
        else if(srcPage.equalsIgnoreCase("users") )
        {
            return 1;
        }
        else if(srcPage.equalsIgnoreCase("schools") )
        {
            return 2;
        }
        else if(srcPage.equalsIgnoreCase("modules") )
        {
            return 3;
        }
        else if(srcPage.equalsIgnoreCase("reports") )
        {
            return 4;
        }
        else {
            return 69;
        }
  
    }
}
