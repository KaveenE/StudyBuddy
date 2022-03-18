/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.StudentEntity;
import java.util.List;
import javax.ejb.Local;
import javax.security.auth.login.AccountNotFoundException;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author enkav
 */
@Local
public interface StudentSessionBeanLocal {

    public List<StudentEntity> retrieveAllStudents();

    public StudentEntity retrieveStudentById(Long studentId) throws DoesNotExistException, InputDataValidationException;
    
    public Long createNewStudent(StudentEntity newStudentEntity) throws AlreadyExistsException, InputDataValidationException, UnknownPersistenceException;

    public StudentEntity retrieveStudentByUsername(String username) throws DoesNotExistException, InputDataValidationException;

    public StudentEntity studentLogin(String username, String password) throws InvalidLoginCredentialException;

    public void updateAccountStudent(StudentEntity studentEntity) throws InputDataValidationException, AccountNotFoundException, DoesNotExistException;

    
}
