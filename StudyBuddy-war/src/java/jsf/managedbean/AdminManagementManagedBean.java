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

/**
 *
 * @author SCXY
 */
@Named(value = "adminManagementManagedBean")
@RequestScoped
public class AdminManagementManagedBean {

    @EJB(name = "AdminSessionBeanLocal")
    private AdminSessionBeanLocal adminSessionBeanLocal;

    private AdminEntity newAdminEntity;

    public AdminManagementManagedBean() {
        newAdminEntity = new AdminEntity();
    }

    public void createNewAdmin(ActionEvent event) {
        try {
            Long adminId = adminSessionBeanLocal.createNewAdminEntity(newAdminEntity);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New admin account created successfully (Admin Id: " + adminId + ")", null));
        } catch (AlreadyExistsException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new admin: The admin account already exist", null));
        } catch (UnknownPersistenceException | InputDataValidationException ex) {
            Logger.getLogger(AdminManagementManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public AdminEntity getNewAdminEntity() {
        return newAdminEntity;
    }

    public void setNewAdminEntity(AdminEntity newAdminEntity) {
        this.newAdminEntity = newAdminEntity;
    }

}
