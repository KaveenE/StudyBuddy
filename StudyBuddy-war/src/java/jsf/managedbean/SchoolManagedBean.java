/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.SchoolSessionBeanLocal;
import entities.SchoolEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
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
@Named(value = "schoolManagedBean")
@ViewScoped
public class SchoolManagedBean implements Serializable {

    @EJB
    private SchoolSessionBeanLocal schoolSessionBean;

    private List<SchoolEntity> schoolEntities;
    private List<SchoolEntity> filteredSchoolEntities;
    private List<SchoolEntity> selectedSchoolEntities;
    private SchoolEntity selectedSchoolEntity;

    public SchoolManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        schoolEntities = schoolSessionBean.retrieveAllSchools();
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (filterText == null || filterText.isEmpty()) {
            return true;
        }

        SchoolEntity school = (SchoolEntity) value;

        return school.getSchoolId().toString().contains(filterText)
                || school.getName().toLowerCase().contains(filterText);
    }

    public void createNew() {
        this.selectedSchoolEntity = new SchoolEntity();
    }

    public void createNewSchool(ActionEvent event) {
        try {
            schoolSessionBean.createNewSchool(selectedSchoolEntity);
            this.schoolEntities.add(this.selectedSchoolEntity);
            JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "School Added");
            PrimeFaces.current().executeScript("PF('manageNewDialog').hide()");

        } catch (AlreadyExistsException | InputDataValidationException | UnknownPersistenceException ex) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Error while creating the new school: " + ex);
        }
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllSchools");

    }

    public void updateSchool(ActionEvent event) {
        try {
            schoolSessionBean.updateSchool(selectedSchoolEntity);
            JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "School Updated");
            PrimeFaces.current().executeScript("PF('manageDialog').hide()");
            
        } catch (DoesNotExistException | InputDataValidationException ex) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Error while updating: " + ex);
        }
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllSchools");

    }

    public void deleteSelectedSchool() {
        //TODO: Only deletes from page, not DB!
        //Replace below line with method from EJB
        this.schoolEntities.remove(this.selectedSchoolEntity);
        this.selectedSchoolEntity = null;
        JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "School Removed");
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllSchools", "form:delete-multiple-button");

    }

    public void deleteSelectedSchools() {
        //TODO: Only deletes from page, not DB!
        //Replace below line with method from EJB
        this.schoolEntities.removeAll(this.selectedSchoolEntities);
        this.selectedSchoolEntities = null;
        JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Schools Removed");
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllSchools");
        PrimeFaces.current().executeScript("PF('dataTableAllSchools').clearFilters()");
    }

    public String getDeleteButtonMessage() {
        if (hasMultipleSelectedSchools()) {
            int size = this.selectedSchoolEntities.size();
            return size > 1 ? size + " schools selected" : "1 school selected";
        }

        return "Delete";
    }

    public boolean hasMultipleSelectedSchools() {
        return this.selectedSchoolEntities != null && !this.selectedSchoolEntities.isEmpty();
    }

    public List<SchoolEntity> getSchoolEntities() {
        return schoolEntities;
    }

    public void setSchoolEntities(List<SchoolEntity> schoolEntities) {
        this.schoolEntities = schoolEntities;
    }

    public List<SchoolEntity> getFilteredSchoolEntities() {
        return filteredSchoolEntities;
    }

    public void setFilteredSchoolEntities(List<SchoolEntity> filteredSchoolEntities) {
        this.filteredSchoolEntities = filteredSchoolEntities;
    }

    public List<SchoolEntity> getSelectedSchoolEntities() {
        return selectedSchoolEntities;
    }

    public void setSelectedSchoolEntities(List<SchoolEntity> selectedSchoolEntities) {
        this.selectedSchoolEntities = selectedSchoolEntities;
    }

    public SchoolEntity getSelectedSchoolEntity() {;
        return selectedSchoolEntity;
    }

    public void setSelectedSchoolEntity(SchoolEntity selectedSchoolEntity) {
        this.selectedSchoolEntity = selectedSchoolEntity;
    }

}
