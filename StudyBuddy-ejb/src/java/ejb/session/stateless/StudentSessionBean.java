/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AccountEntity;
import entities.StudentEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StudentDoesNotExistException;
import util.exception.UnknownPersistenceException;
import util.helper.EJBHelper;

/**
 *
 * @author enkav
 */
@Stateless
public class StudentSessionBean implements StudentSessionBeanLocal {

    @PersistenceContext(unitName = "StudyBuddy-ejbPU")
    private EntityManager em;
    @EJB
    private AccountSessionBeanLocal accountSessionBeanLocal;

    @Override
    public List<StudentEntity> retrieveAllStudents() {
        List<? extends AccountEntity> studentEntities = accountSessionBeanLocal.retrieveAllAccounts(StudentEntity.class);
        return (List<StudentEntity>) studentEntities;
    }

    @Override
    public StudentEntity retrieveStudentById(Long studentId) throws DoesNotExistException, InputDataValidationException {
        AccountEntity account = accountSessionBeanLocal.retrieveAccountById(studentId);
        if (!(account instanceof StudentEntity)) {
            throw new StudentDoesNotExistException();
        }
        return (StudentEntity) account;
    }

    @Override
    public StudentEntity retrieveStudentByUsername(String username) throws DoesNotExistException, InputDataValidationException {
        AccountEntity account = accountSessionBeanLocal.retrieveAccountByUsername(username);
        if (!(account instanceof StudentEntity)) {
            throw new StudentDoesNotExistException();
        }
        return (StudentEntity) account;
    }

    @Override
    public Long createNewStudent(StudentEntity newStudentEntity) throws AlreadyExistsException, InputDataValidationException, UnknownPersistenceException {
        return accountSessionBeanLocal.createNewAccount(newStudentEntity);
    }

    @Override
    public StudentEntity studentLogin(String username, String password) throws InvalidLoginCredentialException {
        AccountEntity account = accountSessionBeanLocal.login(username, password);
        if (!(account instanceof StudentEntity)) {
            throw new InvalidLoginCredentialException();
        }
        return (StudentEntity) account;
    }

    //Password deliberately not updated as only the account owner can change the password
    @Override
    public void updateAccountStudent(StudentEntity studentEntity) throws InputDataValidationException, DoesNotExistException {
        EJBHelper.throwValidationErrorsIfAny(studentEntity);

        StudentEntity accountToUpdateStudent = retrieveStudentById(studentEntity.getAccountId());

        accountToUpdateStudent.setEmail(studentEntity.getEmail());
        accountToUpdateStudent.setYearOfStudy(studentEntity.getYearOfStudy());
        accountToUpdateStudent.setOptLocation(studentEntity.getOptLocation());

        //not sure if the following attributes can be updated in this method (depends on the frontend implementation)
        accountToUpdateStudent.setIsEnabled(studentEntity.getIsEnabled());
        accountToUpdateStudent.setIsPremium(studentEntity.getIsPremium());
        accountToUpdateStudent.setCreditBalance(studentEntity.getCreditBalance());
    }
}
