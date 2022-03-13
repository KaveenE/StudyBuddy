/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.SchoolEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author SCXY
 */
@Local
public interface SchoolSessionBeanLocal {

    public List<SchoolEntity> retrieveAllSchools();

    public SchoolEntity retrieveSchoolById(Long schoolId) throws DoesNotExistException, InputDataValidationException;

    public Long createNewSchool(SchoolEntity newSchoolEntity) throws AlreadyExistsException, InputDataValidationException, UnknownPersistenceException;
}
