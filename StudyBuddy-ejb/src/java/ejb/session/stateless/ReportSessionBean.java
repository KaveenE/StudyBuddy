/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.ReportEntity;
import entities.StudentEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.ReportAlreadyExistsException;
import util.exception.ReportDoesNotExistException;
import util.exception.StudentDoesNotExistException;
import util.exception.UnknownPersistenceException;
import util.helper.EJBHelper;

/**
 *
 * @author seanc
 */
@Stateless
public class ReportSessionBean implements ReportSessionBeanLocal {

    @EJB(name = "StudentSessionBeanLocal")
    private StudentSessionBeanLocal studentSessionBeanLocal;

    @PersistenceContext(unitName = "StudyBuddy-ejbPU")
    private EntityManager em;

    @Override
    public List<ReportEntity> retrieveAllReports() {
        Query query = em.createQuery("SELECT r FROM ReportEntity r");

        return query.getResultList();
    }
    
    @Override
    public ReportEntity retrieveReportById(Long reportId) throws InputDataValidationException, DoesNotExistException {
        ReportEntity report = em.find(ReportEntity.class, reportId);
        EJBHelper.requireNonNull(report, new ReportDoesNotExistException());
        EJBHelper.throwValidationErrorsIfAny(report);

        return report;
    }
    
    @Override
    public ReportEntity retrieveReportByReportedReporteeId(Long reportedId,Long reporterId) throws InputDataValidationException, DoesNotExistException {
         TypedQuery<ReportEntity> tq = em.createQuery("SELECT r FROM ReportEntity r "
                 + "WHERE r.reportedStudent.accountId = :reportedId AND "
                 + "r.studentWhoReported.accountId = :reporterId", ReportEntity.class)
                .setParameter("reportedId", reportedId)
                .setParameter("reporterId", reporterId);
         try {
            return tq.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ReportDoesNotExistException("Student has not reported this fella before");
        }

    }

    @Override
    public Long createNewReport(ReportEntity newReportEntity, Long reportedId, Long reporterId) throws InputDataValidationException, AlreadyExistsException, UnknownPersistenceException, DoesNotExistException {
        EJBHelper.throwValidationErrorsIfAny(newReportEntity);

        try {
            EJBHelper.requireNonNull(reportedId, new StudentDoesNotExistException("The new report must be associated with a reported student"));
            EJBHelper.requireNonNull(reporterId, new StudentDoesNotExistException("The new report must be associated with a reporter student"));

            StudentEntity reportedStudent = studentSessionBeanLocal.retrieveStudentById(reportedId);
            StudentEntity reporterStudent = studentSessionBeanLocal.retrieveStudentById(reporterId);
            em.persist(newReportEntity);
            reportedStudent.getReportReceived().add(newReportEntity);
            newReportEntity.setReportedStudent(reportedStudent);
            reporterStudent.getReportsSubmitted().add(newReportEntity);
            newReportEntity.setStudentWhoReported(reporterStudent);

            em.flush();
        } catch (PersistenceException ex) {
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new ReportAlreadyExistsException());
        }

        return newReportEntity.getReportId();
    }

    //only supports updating of the isResolved attribute currently
    @Override
    public void updateReport(ReportEntity reportEntity) throws InputDataValidationException, DoesNotExistException {
        ReportEntity reportEntityToUpdate = retrieveReportById(reportEntity.getReportId());
        reportEntityToUpdate.setIsResolved(true);
    }
}
