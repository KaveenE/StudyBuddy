/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author enkav
 */
@Entity
public class StudentEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;
    
    @Column(nullable = false)
    @NotNull
    private String yearOfStudy;
    //can be null?
    private Long creditBalance; 
    
    @Column(nullable = false)
    @NotNull
    private Boolean isPremium;
    
    @Column(nullable = false)
    @NotNull
    private Boolean isEnabled;
    //can be null?
    private Boolean optLocation;
        
        

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (studentId != null ? studentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the studentId fields are not set
        if (!(object instanceof StudentEntity)) {
            return false;
        }
        StudentEntity other = (StudentEntity) object;
        if ((this.studentId == null && other.studentId != null) || (this.studentId != null && !this.studentId.equals(other.studentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.StudentEntity[ id=" + studentId + " ]";
    }

    /**
     * @return the yearOfStudy
     */
    public String getYearOfStudy() {
        return yearOfStudy;
    }

    /**
     * @param yearOfStudy the yearOfStudy to set
     */
    public void setYearOfStudy(String yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    /**
     * @return the creditBalance
     */
    public Long getCreditBalance() {
        return creditBalance;
    }

    /**
     * @param creditBalance the creditBalance to set
     */
    public void setCreditBalance(Long creditBalance) {
        this.creditBalance = creditBalance;
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
     * @return the isEnabled
     */
    public Boolean getIsEnabled() {
        return isEnabled;
    }

    /**
     * @param isEnabled the isEnabled to set
     */
    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * @return the optLocation
     */
    public Boolean getOptLocation() {
        return optLocation;
    }

    /**
     * @param optLocation the optLocation to set
     */
    public void setOptLocation(Boolean optLocation) {
        this.optLocation = optLocation;
    }
    
}
