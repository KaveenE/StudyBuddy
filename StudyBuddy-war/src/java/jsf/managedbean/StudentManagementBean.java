/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.StudentSessionBeanLocal;
import entities.StudentEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.security.auth.login.AccountNotFoundException;
import org.primefaces.PrimeFaces;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.helper.JSFHelper;


/**
 *
 * @author wenting
 */
@Named(value = "studentManagementBean")
@ViewScoped

public class StudentManagementBean implements Serializable {

    @EJB
    private StudentSessionBeanLocal studentSessionBean;

    
    private List<StudentEntity> studentEntities;
    private List<StudentEntity> filteredStudentEntities;
    private List<StudentEntity> selectedStudentEntities;
    private StudentEntity selectedStudentEntity;
    
    public StudentManagementBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        studentEntities = studentSessionBean.retrieveAllStudents();
    }
    
    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (filterText == null || filterText.isEmpty()) {
            return true;
        }

        StudentEntity student = (StudentEntity) value;
        
        return student.getAccountId().toString().contains(filterText)
                || student.getUsername().toLowerCase().contains(filterText);     
    }
    
    public void createNew() {
        this.selectedStudentEntity = new StudentEntity();
    }
    
    public void saveStudent() {
        if (this.selectedStudentEntity.getAccountId()== null) {
           
            try {
                studentSessionBean.createNewStudent(selectedStudentEntity);
            } catch (AlreadyExistsException | InputDataValidationException | UnknownPersistenceException ex) {
                JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Error while creating the new student: "+ex);
            }
            
            this.studentEntities.add(this.selectedStudentEntity);
            JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Student Added");
        }
        else {
            try {
                studentSessionBean.updateAccountStudent(selectedStudentEntity);
            } catch (DoesNotExistException | AccountNotFoundException | InputDataValidationException ex) {
                JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Error while updating: "+ex);
            }
            JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Student Updated");
        }

        PrimeFaces.current().executeScript("PF('manageDialog').hide()");
        PrimeFaces.current().executeScript("PF('manageNewDialog').hide()");
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllUsers");
    }
    
    public void deleteSelectedUser() {
        //TODO: Only deletes from page, not DB!
        //Replace below line with method from EJB
        this.studentEntities.remove(this.selectedStudentEntity);
        this.selectedStudentEntity = null;
        JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Student Removed");
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllUsers","form:delete-multiple-button");
        
    }
    
    public void deleteSelectedUsers() {
        //TODO: Only deletes from page, not DB!
        //Replace below line with method from EJB
        this.studentEntities.removeAll(this.selectedStudentEntities);
        this.selectedStudentEntities = null;
        JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Students Removed");
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllUsers");
        PrimeFaces.current().executeScript("PF('dataTableAllUsers').clearFilters()");
    }
    
    public String getDeleteButtonMessage() {
        if (hasMultipleSelectedStudents()) {
            int size = this.selectedStudentEntities.size();
            return size > 1 ? size + " students selected" : "1 student selected";
        }
        
        return "Delete";
    }
    
    public boolean hasMultipleSelectedStudents() {
        return this.selectedStudentEntities != null && !this.selectedStudentEntities.isEmpty();
    }
    
    public List<StudentEntity> getStudentEntities() {
        return studentEntities;
    }

    public void setStudentEntities(List<StudentEntity> studentEntities) {
        this.studentEntities = studentEntities;
    }
    
    public List<StudentEntity> getFilteredStudentEntities() {
        return filteredStudentEntities;
    }

    public void setFilteredStudentEntities(List<StudentEntity> filteredStudentEntities) {
        this.filteredStudentEntities = filteredStudentEntities;
    }

    public List<StudentEntity> getSelectedStudensEntities() {
        return selectedStudentEntities;
    }

    public void setSelectedStudentEntities(List<StudentEntity> selectedStudentEntities) {
        this.selectedStudentEntities = selectedStudentEntities;
    }

    public StudentEntity getSelectedStudentEntity() {
        return selectedStudentEntity;
    }

    public void setSelectedStudentEntity(StudentEntity selectedStudentEntity) {  
        this.selectedStudentEntity = selectedStudentEntity;
    }
    
}
