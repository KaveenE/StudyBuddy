/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminSessionBeanLocal;
import ejb.session.stateless.AdvertisementSessionBeanLocal;
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
import util.helper.ModHelper;
import ejb.session.stateless.ModuleSessionBeanLocal;
import ejb.session.stateless.ReportSessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import ejb.session.stateless.SchoolSessionBeanLocal;
import entities.AdvertisementEntity;
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
    private AdvertisementSessionBeanLocal advertisementSessionBeanLocal;

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

            advertisementSessionBeanLocal.createNewAdvertisement(new AdvertisementEntity("Shopee", "https://i.imgur.com/wWSzJIx.png",2.5));
            advertisementSessionBeanLocal.createNewAdvertisement(new AdvertisementEntity("Indigo", "https://i.imgur.com/ISlyBFs.png",1.0));
            advertisementSessionBeanLocal.createNewAdvertisement(new AdvertisementEntity("Secret Lab", "https://i.imgur.com/HHDqj7P.png",3.3));
            advertisementSessionBeanLocal.createNewAdvertisement(new AdvertisementEntity("Grammarly", "https://i.imgur.com/znB8aI9.png",3.0));
            
            studentEntitySessionBean.createNewStudent(new StudentEntity("stud1@gmail.com", "stud1", "password", "Y1S1", "stud1name"));
            studentEntitySessionBean.createNewStudent(new StudentEntity("stud2@gmail.com", "stud2", "password", "Y1S2", "stud2name"));
            studentEntitySessionBean.createNewStudent(new StudentEntity("stud3@gmail.com", "stud3", "password", "Y1S1", "stud3name"));

            adminSessionBean.createNewAdminEntity(new AdminEntity("admin@stubud.xyz", "admin", "password"));

            SchoolEntity nus = new SchoolEntity("National University of Singapore (NUS)");
            SchoolEntity smu = new SchoolEntity("Singapore Management University (SMOO)");
            SchoolEntity ntu = new SchoolEntity("Nanyang Technological University (NTU)");
            Long nusSchoolId = schoolEntitySessionBean.createNewSchool(nus);
            Long smuSchoolId = schoolEntitySessionBean.createNewSchool(smu);
            Long ntuSchoolId = schoolEntitySessionBean.createNewSchool(ntu);

            initModules("moduleList", nus, nusSchoolId);
            initModules("ntuList", ntu, ntuSchoolId);
            initModules("smuList", smu, smuSchoolId);

            // Group Entity. Edited
            GroupEntity groupEntity = new GroupEntity();
            groupEntity.setGroupName("Programming Group Freshman");
            groupEntity.setDescription("A very detailed description");
            groupEntity.setModuleEntity(moduleEntitySessionBean.retrieveModuleById(1l));
            groupEntity.setIsOpen(Boolean.TRUE);
            groupEntity.setDateTimeCreated(LocalDateTime.now());
            groupEntity.setPoster(studentEntitySessionBean.retrieveStudentById(1l));
            studentEntitySessionBean.retrieveStudentById(1l).getGroupsPosted().add(groupEntity);
            groupEntitySessionBean.createNewGroupEntity(groupEntity, 1l, 1l);

            groupEntity.getCandidates().add(studentEntitySessionBean.retrieveStudentById(2l));
            studentEntitySessionBean.retrieveStudentById(2l).getGroupsApplied().add(groupEntity);
            groupEntity.getGroupMembers().add(studentEntitySessionBean.retrieveStudentById(3l));
            studentEntitySessionBean.retrieveStudentById(3l).getGroups().add(groupEntity);

            reportSessionBeanLocal.createNewReport(new ReportEntity("test"), 1L, 2L);

//          Kanban Board Entity
            kanbanSessionBean.createDefaultKanbanBoard(1l);
        } catch (AlreadyExistsException | UnknownPersistenceException | InputDataValidationException | DoesNotExistException ex) {
            Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initModules(String fileName, SchoolEntity someSch, Long someSchId) {
        try {
            Reader someSchoolModReader = ModHelper.getReader(fileName);
            if (someSchoolModReader != null) {
                System.out.println("Sucessfully retrieve reader");
                JSONTokener jsonTokener = new JSONTokener(someSchoolModReader);

                JSONArray jsonArray = new JSONArray(jsonTokener);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.getString(fileName.equals("ntuList") ? "name" : "title");
                    String code = jsonObject.getString(fileName.equals("ntuList") ? "code" : "moduleCode");
                    moduleEntitySessionBean.createNewModule(new ModuleEntity(name, code, someSch), someSchId);
                }
                someSchoolModReader.close();
            } else {
                System.out.println("Returned Reader is null");
            }
        } catch (AlreadyExistsException | NoClassDefFoundError
                | IOException | InputDataValidationException
                | UnknownPersistenceException | DoesNotExistException ex) {
            Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
