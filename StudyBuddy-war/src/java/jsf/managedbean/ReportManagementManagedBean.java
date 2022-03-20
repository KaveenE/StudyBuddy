/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ReportSessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entities.ReportEntity;
import entities.StudentEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.PrimeFaces;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.helper.JSFHelper;

/**
 *
 * @author SCXY
 */
@Named(value = "reportManagementManagedBean")
@ViewScoped
public class ReportManagementManagedBean implements Serializable {

    @EJB
    private ReportSessionBeanLocal reportSessionBean;

    @EJB
    private StudentSessionBeanLocal studentSessionBean;

    private List<ReportEntity> reportEntities;
    private List<ReportEntity> filteredReportEntities;
    private List<ReportEntity> selectedReportEntities;
    private ReportEntity selecteReportEntity;

    private ReportEntity selectedReportEntity;

    private List<StudentEntity> studentEntities;
    private StudentEntity reportedStudentEntity;
    private StudentEntity reportingStudentEntity;

    public ReportManagementManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        reportEntities = reportSessionBean.retrieveAllReports();
        studentEntities = studentSessionBean.retrieveAllStudents();
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (filterText == null || filterText.isEmpty()) {
            return true;
        }

        ReportEntity report = (ReportEntity) value;

        return report.getReportId().toString().contains(filterText)
                || report.getStudentWhoReported().getUsername().contains(filterText)
                || report.getReportedStudent().getUsername().toLowerCase().contains(filterText);
    }

    public void createNew() {
        this.selectedReportEntity = new ReportEntity();
        this.setReportedStudentEntity(new StudentEntity());
        this.setReportingStudentEntity(new StudentEntity());
    }

    public void saveReport() {

        if (this.selectedReportEntity.getReportId() == null) {

            try {
                reportSessionBean.createNewReport(selectedReportEntity, reportedStudentEntity.getAccountId(), reportingStudentEntity.getAccountId());

            } catch (DoesNotExistException | AlreadyExistsException | InputDataValidationException | UnknownPersistenceException ex) {
                JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Error while creating the new report: " + ex.getMessage());
                System.out.println(ex.getMessage());

            }

            System.out.println(selectedReportEntity.getReportId());

            this.reportEntities.add(this.selectedReportEntity);
            JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Report Added");
        } else {
            try {
                reportSessionBean.updateReport(selectedReportEntity);
            } catch (DoesNotExistException | InputDataValidationException ex) {
                JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Error while updating: " + ex);
            }
            JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Report Updated");
        }

        PrimeFaces.current().executeScript("PF('manageDialog').hide()");
        PrimeFaces.current().executeScript("PF('manageNewDialog').hide()");
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllReports");

    }
    
        public void deleteSelectedReport() {
        //TODO: Only deletes from page, not DB!
        //Replace below line with method from EJB
        this.reportEntities.remove(this.selectedReportEntity);
        this.selectedReportEntity = null;
        JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Report Removed");
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllReports", "form:delete-multiple-button");

    }

    public void deleteSelectedReports() {
        //TODO: Only deletes from page, not DB!
        //Replace below line with method from EJB
        this.reportEntities.removeAll(this.getSelectedReportEntities());
        this.setSelectedReportEntities(null);
        JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Reports Removed");
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllReports");
        PrimeFaces.current().executeScript("PF('dataTableAllReports').clearFilters()");
    }

    public String getDeleteButtonMessage() {
        if (hasMultipleSelectedReports()) {
            int size = this.getSelectedReportEntities().size();
            return size > 1 ? size + " reports selected" : "1 report selected";
        }

        return "Delete";
    }
    
    public boolean hasMultipleSelectedReports() {
        return this.getSelectedReportEntities() != null && !this.selectedReportEntities.isEmpty();
    }
    
    public void doUpdateReport(ActionEvent event) {
        selectedReportEntity = (ReportEntity) event.getComponent().getAttributes().get("selectedReportEntity");
    }

    //currently supports updating of the isResolved attribute 
    public void updateReport(ActionEvent event) {
        try {
            reportSessionBean.updateReport(selectedReportEntity);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Report updated successfully", null));
        } catch (DoesNotExistException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating report: " + ex.getMessage(), null));
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public List<ReportEntity> getReportEntities() {
        return reportEntities;
    }

    public void setReportEntities(List<ReportEntity> reportEntities) {
        this.reportEntities = reportEntities;
    }

    public ReportEntity getSelectedReportEntity() {
        return selectedReportEntity;
    }

    public void setSelectedReportEntity(ReportEntity selectedReportEntity) {
        this.selectedReportEntity = selectedReportEntity;
    }

    /**
     * @return the filteredReportEntities
     */
    public List<ReportEntity> getFilteredReportEntities() {
        return filteredReportEntities;
    }

    /**
     * @param filteredReportEntities the filteredReportEntities to set
     */
    public void setFilteredReportEntities(List<ReportEntity> filteredReportEntities) {
        this.filteredReportEntities = filteredReportEntities;
    }

    /**
     * @return the selectedReportEntities
     */
    public List<ReportEntity> getSelectedReportEntities() {
        return selectedReportEntities;
    }

    /**
     * @param selectedReportEntities the selectedReportEntities to set
     */
    public void setSelectedReportEntities(List<ReportEntity> selectedReportEntities) {
        this.selectedReportEntities = selectedReportEntities;
    }

    /**
     * @return the selecteReportEntity
     */
    public ReportEntity getSelecteReportEntity() {
        return selecteReportEntity;
    }

    /**
     * @param selecteReportEntity the selecteReportEntity to set
     */
    public void setSelecteReportEntity(ReportEntity selecteReportEntity) {
        this.selecteReportEntity = selecteReportEntity;
    }

    /**
     * @return the studentEntities
     */
    public List<StudentEntity> getStudentEntities() {
        return studentEntities;
    }

    /**
     * @param studentEntities the studentEntities to set
     */
    public void setStudentEntities(List<StudentEntity> studentEntities) {
        this.studentEntities = studentEntities;
    }

    /**
     * @return the reportedStudentEntity
     */
    public StudentEntity getReportedStudentEntity() {
        return reportedStudentEntity;
    }

    /**
     * @param reportedStudentEntity the reportedStudentEntity to set
     */
    public void setReportedStudentEntity(StudentEntity reportedStudentEntity) {
        this.reportedStudentEntity = reportedStudentEntity;
    }

    /**
     * @return the reportingStudentEntity
     */
    public StudentEntity getReportingStudentEntity() {
        return reportingStudentEntity;
    }

    /**
     * @param reportingStudentEntity the reportingStudentEntity to set
     */
    public void setReportingStudentEntity(StudentEntity reportingStudentEntity) {
        this.reportingStudentEntity = reportingStudentEntity;
    }

}
