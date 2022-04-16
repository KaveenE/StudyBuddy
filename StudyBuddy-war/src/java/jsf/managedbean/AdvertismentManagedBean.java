/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AdvertisementSessionBeanLocal;
import entities.AdvertisementEntity;
import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import org.primefaces.PrimeFaces;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.helper.JSFHelper;

/**
 *
 * @author enkav
 */
@Named(value = "advManagedBean")
@ViewScoped
public class AdvertismentManagedBean implements Serializable {

    @EJB(name = "advertisementSessionBeanLocal")
    private AdvertisementSessionBeanLocal advertisementSessionBeanLocal;

    private List<AdvertisementEntity> advEntities;
    private List<AdvertisementEntity> filteredAdvEntities;
    private List<AdvertisementEntity> selectedAdvEntities;
    private AdvertisementEntity selectedAdvEntity;
    
    public AdvertismentManagedBean() {
        
    }
    
    @PostConstruct
    public void postConstruct() {
        this.advEntities = advertisementSessionBeanLocal.retrieveAllAdvertisements();
    }
    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (filterText == null || filterText.isEmpty()) {
            return true;
        }

        AdvertisementEntity adv = (AdvertisementEntity) value;

        return adv.getAdvertisementId().toString().contains(filterText)
                || adv.getImageUrl().toLowerCase().contains(filterText)
                || adv.getCompanyName().toLowerCase().contains(filterText);
    }
    
    public void createNew() {
        this.selectedAdvEntity = new AdvertisementEntity();
    }
    
     public void createNewAdv (ActionEvent event) {
        try {
            advertisementSessionBeanLocal.createNewAdvertisement(selectedAdvEntity);
            this.advEntities.add(this.selectedAdvEntity);
            JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Advertisement Added");
            PrimeFaces.current().executeScript("PF('manageNewDialog').hide()");
        } catch (AlreadyExistsException | InputDataValidationException | UnknownPersistenceException ex) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Error while creating the new advertisement: " + ex.getMessage());
        }
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllAdvs");
    }
    
    public void updateAdv(ActionEvent event) {
        try {
            advertisementSessionBeanLocal.updateAdvertisement(selectedAdvEntity);
            JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Advertisement Updated");
            PrimeFaces.current().executeScript("PF('manageDialog').hide()");
            
        } catch (DoesNotExistException | InputDataValidationException ex) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Error while updating: " + ex);
        }
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllAdvs");

    }
    
    public void deleteSelectedAdv() {
        try {
            System.out.println(selectedAdvEntity.getCompanyName());
            advertisementSessionBeanLocal.deleteAdvertismentById(selectedAdvEntity.getAdvertisementId());
            this.advEntities.remove(this.selectedAdvEntity);
            this.selectedAdvEntity = null;
            JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Advertisement Removed");
            PrimeFaces.current().ajax().update("growl", "form:dataTableAllAdvs", "form:delete-multiple-button");
        } catch (DoesNotExistException | InputDataValidationException ex) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Error while deleting: " + ex);
        } 
    }

    public void deleteSelectedAdvs() {
        
        selectedAdvEntities.forEach(adv -> {
            try{
                advertisementSessionBeanLocal.deleteAdvertismentById((adv.getAdvertisementId()));
            }
            catch (DoesNotExistException | InputDataValidationException ex) {
                JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Error while deleting: " + ex);
            }
        });
        this.advEntities.removeAll(this.advEntities);
        this.advEntities = null;
        JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Advertisement Removed");
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllAdvs");
        PrimeFaces.current().executeScript("PF('dataTableAllAdvs').clearFilters()");
    }
    
    public String getDeleteButtonMessage() {
        if (hasMultipleSelectedAdvs()) {
            int size = this.selectedAdvEntities.size();
            return size > 1 ? size + " advertisements selected" : "1 advertisment selected";
        }

        return "Delete";
    }

    public boolean hasMultipleSelectedAdvs() {
        return this.selectedAdvEntities != null && !this.selectedAdvEntities.isEmpty();
    }

    public List<AdvertisementEntity> getAdvEntities() {
        return advEntities;
    }

    public void setAdvEntities(List<AdvertisementEntity> advEntities) {
        this.advEntities = advEntities;
    }

    public List<AdvertisementEntity> getFilteredAdvEntities() {
        return filteredAdvEntities;
    }

    public void setFilteredAdvEntities(List<AdvertisementEntity> filteredAdvEntities) {
        this.filteredAdvEntities = filteredAdvEntities;
    }

    public List<AdvertisementEntity> getSelectedAdvEntities() {
        return selectedAdvEntities;
    }

    public void setSelectedAdvEntities(List<AdvertisementEntity> selectedAdvEntities) {
        this.selectedAdvEntities = selectedAdvEntities;
    }

    public AdvertisementEntity getSelectedAdvEntity() {
        return selectedAdvEntity;
    }

    public void setSelectedAdvEntity(AdvertisementEntity selectedAdvEntity) {
        this.selectedAdvEntity = selectedAdvEntity;
    }
    
    public void seeImage(String link) {
        try {
            System.out.println(link);
            JSFHelper.getExtCtx().redirect(link);
        } catch (IOException ex) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Unable to redirect to "+link);
        }
    }
}
