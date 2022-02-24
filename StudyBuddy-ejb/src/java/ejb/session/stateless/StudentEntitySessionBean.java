/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;


import entities.StudentEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.StudentAlreadyExistsException;
import util.exception.StudentDoesNotExistException;
import util.exception.UnknownPersistenceException;
import util.helper.EJBHelper;

/**
 *
 * @author enkav
 */
@Stateless
public class StudentEntitySessionBean implements StudentEntitySessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @PersistenceContext(unitName = "StudyBuddy-ejbPU")
    private EntityManager em;

    @Override
    public List<StudentEntity> retrieveAllStudents() {
        Query query = em.createQuery("SELECT s FROM StudentEntity s");

        return query.getResultList();
    }

    @Override
    public StudentEntity retrieveStudentById(Long studentId) throws DoesNotExistException, InputDataValidationException {
        StudentEntity student = em.find(StudentEntity.class, studentId);
        EJBHelper.requireNonNull(student, new StudentDoesNotExistException());
        EJBHelper.throwValidationErrorsIfAny(student);

        return student;
    }

    public Long createNewStudent(StudentEntity newStudentEntity) throws AlreadyExistsException, InputDataValidationException, UnknownPersistenceException {
        EJBHelper.throwValidationErrorsIfAny(newStudentEntity);
        
        try {
        em.persist(newStudentEntity);
        em.flush();
        }
        catch(PersistenceException ex) {
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new StudentAlreadyExistsException());
        }
        
        return newStudentEntity.getStudentId();
    }

}
