/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.StudentEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;

/**
 *
 * @author enkav
 */
@Local
public interface StudentEntitySessionBeanLocal {

    public List<StudentEntity> retrieveAllStudents();

    public StudentEntity retrieveStudentById(Long studentId) throws DoesNotExistException, InputDataValidationException;
    
}
