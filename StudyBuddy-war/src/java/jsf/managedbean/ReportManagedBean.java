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
import javax.faces.event.ActionEvent;
import org.primefaces.PrimeFaces;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.helper.JSFHelper;

/**
 *
 * @author SCXY
 */
@Named(value = "reportManagedBean")
@ViewScoped
public class ReportManagedBean implements Serializable {

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

    public ReportManagedBean() {
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

    public void updateReport(ActionEvent event) {
        try {
            reportSessionBean.updateReport(selectedReportEntity);
            JSFHelper.addMessage(FacesMessage.SEVERITY_INFO, "Report Updated");
        } catch (DoesNotExistException | InputDataValidationException ex) {
            JSFHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Error while updating: " + ex);
        }
        PrimeFaces.current().ajax().update("growl", "form:dataTableAllReports");

    }

    public List<ReportEntity> retrieveAllReports() {
        return reportSessionBean.retrieveAllReports();
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
     * @return the sizeInString
     */
    public Long getUnsolvedReportsSize() {
        return reportEntities.stream().filter(report -> !report.getIsResolved()).count();
    }

}
