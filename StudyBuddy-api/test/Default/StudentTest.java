/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Default;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ejb.session.stateless.StudentSessionBeanLocal;
import entities.StudentEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author larby
 */
public class StudentTest {

    StudentSessionBeanLocal studentSessionBean = lookupStudentSessionBeanLocal();
    
    public StudentTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Test
    public void serializeStudent() throws JsonProcessingException {
        try {
            StudentEntity student = studentSessionBean.studentLogin("stud1", "password");
            String studentJson = new ObjectMapper().writeValueAsString(student);
            System.out.println(studentJson);
        } catch (InvalidLoginCredentialException ex) {
            System.out.println("Login Credential");
        }
        
    }

    private StudentSessionBeanLocal lookupStudentSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (StudentSessionBeanLocal) c.lookup("java:global/StudyBuddy/StudyBuddy-ejb/StudentSessionBean!ejb.session.stateless.StudentSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
