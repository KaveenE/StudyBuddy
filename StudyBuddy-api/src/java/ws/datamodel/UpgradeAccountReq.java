package ws.datamodel;

import entities.StudentEntity;
import static java.lang.Boolean.TRUE;
import java.util.List;



public class UpgradeAccountReq
{
    private String username;
    private Boolean  isPremium;
    private StudentEntity studentEntity;


    
    
    public UpgradeAccountReq()
    {        
    }

    
    
    public UpgradeAccountReq(String username, Boolean isPremium) 
    {
        this.username = username;
        this.isPremium = true;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the isPremium
     */
    public Boolean getIsPremium() {
        return isPremium;
    }

    /**
     * @param isPremium the isPremium to set
     */
    public void setIsPremium(Boolean isPremium) {
        this.isPremium = isPremium;
    }

    /**
     * @return the studentEntity
     */
    public StudentEntity getStudentEntity() {
        return studentEntity;
    }

    /**
     * @param studentEntity the studentEntity to set
     */
    public void setStudentEntity(StudentEntity studentEntity) {
        this.studentEntity = studentEntity;
    }
}