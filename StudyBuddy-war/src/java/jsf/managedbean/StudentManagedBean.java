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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import org.primefaces.PrimeFaces;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.helper.JSFHelper;

/**
 *
 * @author wenting
 */
@Named(value = "studentManagedBean")
@ViewScoped

public class StudentManagedBean implements Serializable {

    @EJB
    private StudentSessionBeanLocal studentSessionBean;

    private List<StudentEntity> studentEntities;
    private StudentEntity selectedStudentEntity;

    public StudentManagedBean() {
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

    public void updateStudent(ActionEvent event) {
        try {
            studentSessionBean.updateAccountStudent(selectedStudentEntity);
            JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Student Updated");
        } catch (DoesNotExistException | InputDataValidationException ex) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the student details: " + ex.getMessage());
        }
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllUsers");

    }

    public void disableStudent(ActionEvent event) {
        try {
            selectedStudentEntity = (StudentEntity) event.getComponent().getAttributes().get("studentEntityToDisable");
            selectedStudentEntity.setIsEnabled(!selectedStudentEntity.getIsEnabled());
            studentSessionBean.updateAccountStudent(selectedStudentEntity);
            JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Student Disabled");
        } catch (InputDataValidationException | DoesNotExistException ex) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the student details: " + ex.getMessage());
        }
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllUsers");

    }

    public List<StudentEntity> getStudentEntities() {
        return studentEntities;
    }

    public void setStudentEntities(List<StudentEntity> studentEntities) {
        this.studentEntities = studentEntities;
    }

    public StudentEntity getSelectedStudentEntity() {
        return selectedStudentEntity;
    }

    public void setSelectedStudentEntity(StudentEntity selectedStudentEntity) {
        this.selectedStudentEntity = selectedStudentEntity;
    }

}
