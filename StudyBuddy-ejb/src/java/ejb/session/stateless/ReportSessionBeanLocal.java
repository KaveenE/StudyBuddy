/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.ReportEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author seanc
 */
@Local
public interface ReportSessionBeanLocal {

    public List<ReportEntity> retrieveAllReports();

    public Long createNewReport(ReportEntity newReportEntity, Long reportedId, Long reporterId) throws InputDataValidationException, AlreadyExistsException, UnknownPersistenceException, DoesNotExistException;

    public void updateReport(ReportEntity reportEntity) throws InputDataValidationException, DoesNotExistException;

    public ReportEntity retrieveReportById(Long reportId) throws InputDataValidationException, DoesNotExistException;

    public ReportEntity retrieveReportByReportedReporteeId(Long reportedId, Long reporterId) throws InputDataValidationException, DoesNotExistException;

}
