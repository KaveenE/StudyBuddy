/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminSessionBeanLocal;
import ejb.session.stateless.GroupEntitySessionBeanLocal;
import ejb.session.stateless.KanbanSessionBeanLocal;
import entities.AdminEntity;
import entities.GroupEntity;
import entities.ModuleEntity;
import entities.SchoolEntity;
import entities.StudentEntity;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.helper.NusModHelper;
import ejb.session.stateless.ModuleSessionBeanLocal;
import ejb.session.stateless.ReportSessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import ejb.session.stateless.SchoolSessionBeanLocal;
import entities.ReportEntity;

/**
 *
 * @author enkav
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private ReportSessionBeanLocal reportSessionBeanLocal;

    @EJB
    private KanbanSessionBeanLocal kanbanSessionBean;

    @EJB
    private StudentSessionBeanLocal studentEntitySessionBean;

    @EJB
    private AdminSessionBeanLocal adminSessionBean;

    @EJB
    private GroupEntitySessionBeanLocal groupEntitySessionBean;

    @EJB
    private ModuleSessionBeanLocal moduleEntitySessionBean;

    @EJB
    private SchoolSessionBeanLocal schoolEntitySessionBean;
    

    public DataInitSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("Singleton postconstruct");
        try {
            adminSessionBean.retrieveAdminById(4l);
        } catch (DoesNotExistException ex) {
            initData();
        } catch (InputDataValidationException ex) {
            Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initData() {
        try {
            System.out.println("Data Initialization Start");
            studentEntitySessionBean.createNewStudent(new StudentEntity("stud1@gmail.com", "stud1", "password", "Y1S1"));
            studentEntitySessionBean.createNewStudent(new StudentEntity("stud2@gmail.com", "stud2", "password", "Y1S2"));
            studentEntitySessionBean.createNewStudent(new StudentEntity("stud3@gmail.com", "stud3", "password", "Y1S1"));

            adminSessionBean.createNewAdminEntity(new AdminEntity("admin@stubud.xyz", "admin", "password"));

            SchoolEntity nus = new SchoolEntity("National University of Singapore (NUS)");   
            SchoolEntity smu = new SchoolEntity("Singapore Management University (SMOO)"); 
            Long schoolId = schoolEntitySessionBean.createNewSchool(nus);
            schoolEntitySessionBean.createNewSchool(smu);
            Long moduleId = new Long(0);

            //Importing modules from NUSMod
            try {
                // Using HTTPS request to nusmod server, currently faulty

                Reader nusModReader = NusModHelper.getReader();
                if (nusModReader != null) {
                    System.out.println("Sucessfully retrieve reader");
                    JSONTokener jsonTokener = new JSONTokener(nusModReader);

                    JSONArray jsonArray = new JSONArray(jsonTokener);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("title");
                        String code = jsonObject.getString("moduleCode");
                        moduleId = moduleEntitySessionBean.createNewModule(new ModuleEntity(name, code, nus), schoolId);
                    }
                    nusModReader.close();
                } else {
                    System.out.println("Returned Reader is null");
                }
            } catch (AlreadyExistsException | NoClassDefFoundError | IOException ex) {
                Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Group Entity. Edited
            GroupEntity groupEntity = new GroupEntity();
            groupEntity.setGroupName("Programming Group Freshman");
            groupEntity.setDescription("A very detailed description");
            groupEntity.setModuleEntity(moduleEntitySessionBean.retrieveModuleById(1l));
            groupEntity.setIsOpen(Boolean.TRUE);
            groupEntity.setDateTimeCreated(LocalDateTime.now());
            groupEntity.setPoster(studentEntitySessionBean.retrieveStudentById(1l));
            studentEntitySessionBean.retrieveStudentById(1l).getGroupsPosted().add(groupEntity);
            groupEntitySessionBean.createNewGroupEntity(groupEntity, 1l);

            groupEntity.getCandidates().add(studentEntitySessionBean.retrieveStudentById(2l));
            studentEntitySessionBean.retrieveStudentById(2l).getGroupsApplied().add(groupEntity);
            groupEntity.getGroupMembers().add(studentEntitySessionBean.retrieveStudentById(3l));
            groupEntity.getGroupMembers().add(studentEntitySessionBean.retrieveStudentById(1l));
            studentEntitySessionBean.retrieveStudentById(1l).getGroups().add(groupEntity);
            studentEntitySessionBean.retrieveStudentById(3l).getGroups().add(groupEntity);
            
            reportSessionBeanLocal.createNewReport(new ReportEntity("test"), 1L, 2L);
            
//          Kanban Board Entity
            kanbanSessionBean.createDefaultKanbanBoard(1l);
        } catch (AlreadyExistsException | UnknownPersistenceException | InputDataValidationException | DoesNotExistException ex) {
            Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
