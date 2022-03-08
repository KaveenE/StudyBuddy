/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ReportSessionBeanLocal;
import entities.ReportEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;

/**
 *
 * @author SCXY
 */
@Named(value = "reportManagementManagedBean")
@ViewScoped
public class ReportManagementManagedBean implements Serializable {

    @EJB(name = "ReportSessionBeanLocal")
    private ReportSessionBeanLocal reportSessionBeanLocal;

    private List<ReportEntity> reportEntities;

    private ReportEntity selectedReportEntity;

    public ReportManagementManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        reportEntities = reportSessionBeanLocal.retrieveAllReports();
    }

    public void doUpdateReport(ActionEvent event) {
        selectedReportEntity = (ReportEntity) event.getComponent().getAttributes().get("selectedReportEntity");
    }

    //currently supports updating of the isResolved attribute 
    public void updateReport(ActionEvent event) {
        try {
            reportSessionBeanLocal.updateReport(selectedReportEntity);
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

}
