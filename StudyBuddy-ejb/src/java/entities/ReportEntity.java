/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author SCXY
 */
@Entity
public class ReportEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Column(nullable = false)
    @NotNull
    @Size(min = 1)
    private String description;
    
    @Column(nullable = false)
    @NotNull
    private LocalDateTime dateTimeCreated;
    
    @Column(nullable = false)
    @NotNull
    private Boolean isResolved;

    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    @JsonManagedReference
    private StudentEntity studentWhoReported;
    
    //Is this necessay? I dont have to report a student 
    //Can be like a report on UI
    //Even if it's a report on someone, we can always retrieveByUserName
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    @JsonManagedReference
    private StudentEntity reportedStudent;

    public ReportEntity() {
        this.dateTimeCreated = LocalDateTime.now();
        this.isResolved = false;
    }
    
    public ReportEntity(String description) {
        this();
        this.description = description;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsResolved() {
        return isResolved;
    }

    public void setIsResolved(Boolean isResolved) {
        this.isResolved = isResolved;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reportId != null ? reportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reportId fields are not set
        if (!(object instanceof ReportEntity)) {
            return false;
        }
        ReportEntity other = (ReportEntity) object;
        if ((this.reportId == null && other.reportId != null) || (this.reportId != null && !this.reportId.equals(other.reportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ReportEntity[ id=" + reportId + " ]";
    }

    public StudentEntity getStudentWhoReported() {
        return studentWhoReported;
    }

    public void setStudentWhoReported(StudentEntity studentWhoReported) {
        this.studentWhoReported = studentWhoReported;
    }

    /**
     * @return the reportedStudent
     */
    public StudentEntity getReportedStudent() {
        return reportedStudent;
    }

    /**
     * @param reportedStudent the reportedStudent to set
     */
    public void setReportedStudent(StudentEntity reportedStudent) {
        this.reportedStudent = reportedStudent;
    }

    public LocalDateTime getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(LocalDateTime dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }

}
