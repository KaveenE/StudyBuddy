/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.ReportEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.ReportAlreadyExistsException;
import util.exception.ReportDoesNotExistException;
import util.exception.UnknownPersistenceException;
import util.helper.EJBHelper;

/**
 *
 * @author seanc
 */
@Stateless
public class ReportSessionBean implements ReportSessionBeanLocal {

    @PersistenceContext(unitName = "StudyBuddy-ejbPU")
    private EntityManager em;

    @Override
    public List<ReportEntity> retrieveAllReports() {
        Query query = em.createQuery("SELECT r FROM ReportEntity r");

        return query.getResultList();
    }

    @Override
    public ReportEntity retrieveReporyById(Long reportId) throws InputDataValidationException, DoesNotExistException {
        ReportEntity report = em.find(ReportEntity.class, reportId);
        EJBHelper.requireNonNull(report, new ReportDoesNotExistException());
        EJBHelper.throwValidationErrorsIfAny(report);

        return report;
    }

    @Override
    public Long createNewReport(ReportEntity newReportEntity) throws InputDataValidationException, AlreadyExistsException, UnknownPersistenceException {
        EJBHelper.throwValidationErrorsIfAny(newReportEntity);

        try {
            em.persist(newReportEntity);
            em.flush();
        } catch (PersistenceException ex) {
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new ReportAlreadyExistsException());
        }

        return newReportEntity.getReportId();
    }

}
