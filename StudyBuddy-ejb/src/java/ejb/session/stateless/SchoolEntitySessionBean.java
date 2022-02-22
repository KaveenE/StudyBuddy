/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.ModuleEntity;
import entities.SchoolEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.ModuleAlreadyExistsException;
import util.exception.ModuleDoesNotExistException;
import util.exception.SchoolAlreadyExistsException;
import util.exception.SchoolDoesNotExistException;
import util.exception.UnknownPersistenceException;
import util.helper.EJBHelper;

/**
 *
 * @author SCXY
 */
@Stateless
public class SchoolEntitySessionBean implements SchoolEntitySessionBeanLocal {

    @PersistenceContext(unitName = "StudyBuddy-ejbPU")
    private EntityManager em;

    @Override
    public List<SchoolEntity> retrieveAllSchools() {
        Query query = em.createQuery("SELECT s FROM SchoolEntity s");

        return query.getResultList();
    }

    @Override
    public SchoolEntity retrieveSchoolById(Long schoolId) throws DoesNotExistException, InputDataValidationException {
        SchoolEntity school = em.find(SchoolEntity.class, schoolId);
        EJBHelper.requireNonNull(school, new SchoolDoesNotExistException());
        EJBHelper.throwValidationErrorsIfAny(school);

        return school;
    }

    public Long createNewSchool(SchoolEntity newSchoolEntity) throws AlreadyExistsException, InputDataValidationException, UnknownPersistenceException {
        EJBHelper.throwValidationErrorsIfAny(newSchoolEntity);
        
        try {
        em.persist(newSchoolEntity);
        em.flush();
        }
        catch(PersistenceException ex) {
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new SchoolAlreadyExistsException());
        }
        
        return newSchoolEntity.getschoolId();
    }
}
