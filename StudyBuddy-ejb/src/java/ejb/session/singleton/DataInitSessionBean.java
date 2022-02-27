/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminSessionBeanLocal;
import ejb.session.stateless.GroupEntitySessionBeanLocal;
import ejb.session.stateless.ModuleEntitySessionBeanLocal;
import ejb.session.stateless.SchoolEntitySessionBeanLocal;
import ejb.session.stateless.StudentEntitySessionBeanLocal;
import entities.AdminEntity;
import entities.GroupEntity;
import entities.ModuleEntity;
import entities.SchoolEntity;
import entities.StudentEntity;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author enkav
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private StudentEntitySessionBeanLocal studentEntitySessionBean;

    @EJB
    private AdminSessionBeanLocal adminSessionBean;

    @EJB
    private GroupEntitySessionBeanLocal groupEntitySessionBean;

    @EJB
    private ModuleEntitySessionBeanLocal moduleEntitySessionBean;

    @EJB
    private SchoolEntitySessionBeanLocal schoolEntitySessionBean;

    public DataInitSessionBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        System.out.println("Singleton postconstruct");
        try {
            adminSessionBean.retrieveAccountById(1l);
        } catch (DoesNotExistException ex) {
            initData();
        } catch (InputDataValidationException ex) {
            Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void initData() {
        try {
            System.out.println("Data Initialization Start");
            studentEntitySessionBean.createNewStudent(new StudentEntity("stud1@gmail.com", "stud1", "password", "Freshman"));
            studentEntitySessionBean.createNewStudent(new StudentEntity("stud2@gmail.com", "stud2", "password", "Freshman"));
            studentEntitySessionBean.createNewStudent(new StudentEntity("stud3@gmail.com", "stud3", "password", "Freshman"));
            
            adminSessionBean.createNewAdminEntity(new AdminEntity("admin@stubud.xyz", "admin", "password"));
            
            schoolEntitySessionBean.createNewSchool(new SchoolEntity("National University of Singapore"));
            
            moduleEntitySessionBean.createNewModule(new ModuleEntity("Programming Methodology", "CS1010", schoolEntitySessionBean.retrieveSchoolById(1l)));
            moduleEntitySessionBean.createNewModule(new ModuleEntity("Programming Methodology II", "CS2030", schoolEntitySessionBean.retrieveSchoolById(1l)));
            
            GroupEntity groupEntity = new GroupEntity();
            groupEntity.setGroupName("Programming Group Freshman");
            groupEntity.setDescription("A very detailed description");
            groupEntity.setModuleEntity(moduleEntitySessionBean.retrieveModuleById(1l));
            groupEntity.setIsOpen(Boolean.TRUE);
            groupEntity.setDateTimeCreated(LocalDateTime.now());
            groupEntity.setPoster(studentEntitySessionBean.retrieveStudentById(1l));
            studentEntitySessionBean.retrieveStudentById(1l).getGroupsPosted().add(groupEntity);
            groupEntitySessionBean.createNewGroupEntity(groupEntity);
            
            groupEntity.getCandidates().add(studentEntitySessionBean.retrieveStudentById(2l));
            studentEntitySessionBean.retrieveStudentById(2l).getGroupsApplied().add(groupEntity);
            groupEntity.getGroupMembers().add(studentEntitySessionBean.retrieveStudentById(3l));
            groupEntity.getGroupMembers().add(studentEntitySessionBean.retrieveStudentById(1l));
            studentEntitySessionBean.retrieveStudentById(1l).getGroups().add(groupEntity);
            studentEntitySessionBean.retrieveStudentById(3l).getGroups().add(groupEntity);
            
        } catch (AlreadyExistsException | UnknownPersistenceException | InputDataValidationException | DoesNotExistException ex) {
            Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
