/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ModuleSessionBeanLocal;
import ejb.session.stateless.SchoolSessionBeanLocal;
import entities.ModuleEntity;
import entities.SchoolEntity;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
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
@Named(value = "moduleManagedBean")
@ViewScoped
public class ModuleManagedBean implements Serializable {

    @EJB
    private ModuleSessionBeanLocal moduleSessionBean;
    @EJB
    private SchoolSessionBeanLocal schoolSessionBean;

    private List<ModuleEntity> moduleEntities;
    private List<ModuleEntity> filteredModuleEntities;
    private List<ModuleEntity> selectedModuleEntities;
    private ModuleEntity selectedModuleEntity;

    private List<SchoolEntity> schoolEntities;
    private SchoolEntity selectedSchoolEntity;

    public ModuleManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        moduleEntities = moduleSessionBean.retrieveAllModules();
        schoolEntities = schoolSessionBean.retrieveAllSchools();
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (filterText == null || filterText.isEmpty()) {
            return true;
        }

        ModuleEntity module = (ModuleEntity) value;

        return module.getModuleId().toString().contains(filterText)
                || module.getName().toLowerCase().contains(filterText)
                || module.getSchool().getName().toLowerCase().contains(filterText);
    }

    public void createNew() {
        this.selectedSchoolEntity = new SchoolEntity();
        this.selectedModuleEntity = new ModuleEntity();
    }

    public void createNewModule(ActionEvent event) {
        try {
            moduleSessionBean.createNewModule(selectedModuleEntity, selectedSchoolEntity.getSchoolId());
            this.moduleEntities.add(this.selectedModuleEntity);
            JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Module Added");
            PrimeFaces.current().executeScript("PF('manageNewDialog').hide()");
        } catch (DoesNotExistException | AlreadyExistsException | InputDataValidationException | UnknownPersistenceException ex) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Error while creating the new module: " + ex.getMessage());
        }
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllModules");
    }

    public void updateModule(ActionEvent event) {
        try {
            moduleSessionBean.updateModule(selectedModuleEntity);
            JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Module Updated");
            PrimeFaces.current().executeScript("PF('manageDialog').hide()");
        } catch (DoesNotExistException | InputDataValidationException ex) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Error while updating: " + ex);
        }
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllModules");
    }

    public void deleteSelectedModule(ActionEvent event) {
        try {
            selectedModuleEntity = (ModuleEntity) event.getComponent().getAttributes().get("moduleEntityToDisable");
            selectedModuleEntity.setIsDeleted(!selectedModuleEntity.getIsDeleted());
            moduleSessionBean.updateModule(selectedModuleEntity);
            JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Module Updated");
        } catch (DoesNotExistException | InputDataValidationException ex) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Error while deleting: " + ex);
        }

        PrimeFaces.current().ajax().update("growl", "form:dataTableAllModules", "form:delete-multiple-button");
    }

    public void deleteSelectedModules() {
        //TODO: Only deletes from page, not DB!
        //Replace below line with method from EJB
        selectedModuleEntities.forEach(mod -> {
            try {
                moduleSessionBean.updateModule(selectedModuleEntity);
                moduleEntities.remove(mod);
            } catch (DoesNotExistException | InputDataValidationException ex) {
                JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Error while deleting: " + ex);
            }
        });

        this.selectedModuleEntities = null;
        JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Modules Removed");
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllModules");
        PrimeFaces.current().executeScript("PF('dataTableAllModules').clearFilters()");
    }

    public String getDeleteButtonMessage() {
        if (hasMultipleSelectedModules()) {
            int size = this.selectedModuleEntities.size();
            return size > 1 ? size + " modules selected" : "1 module selected";
        }

        return "Delete";
    }

    public boolean hasMultipleSelectedModules() {
        return this.selectedModuleEntities != null && !this.selectedModuleEntities.isEmpty();
    }

    public List<ModuleEntity> getModuleEntities() {
        return moduleEntities;
    }

    public void setModuleEntities(List<ModuleEntity> moduleEntities) {
        this.moduleEntities = moduleEntities;
    }

    public List<ModuleEntity> getFilteredModuleEntities() {
        return filteredModuleEntities;
    }

    public void setFilteredModuleEntities(List<ModuleEntity> filteredModuleEntities) {
        this.filteredModuleEntities = filteredModuleEntities;
    }

    public List<ModuleEntity> getSelectedModuleEntities() {
        return selectedModuleEntities;
    }

    public void setSelectedModuleEntities(List<ModuleEntity> selectedModuleEntities) {
        this.selectedModuleEntities = selectedModuleEntities;
    }

    public ModuleEntity getSelectedModuleEntity() {
        return selectedModuleEntity;
    }

    public void setSelectedModuleEntity(ModuleEntity selectedModuleEntity) {
        this.selectedModuleEntity = selectedModuleEntity;
    }

    public List<SchoolEntity> getSchoolEntities() {
        return schoolEntities;
    }

    public void setSchoolEntities(List<SchoolEntity> schoolEntities) {
        this.schoolEntities = schoolEntities;
    }

    public SchoolEntity getSelectedSchoolEntity() {
        return selectedSchoolEntity;
    }

    public void setSelectedSchoolEntity(SchoolEntity selectedSchoolEntity) {
        this.selectedSchoolEntity = selectedSchoolEntity;
    }

}
