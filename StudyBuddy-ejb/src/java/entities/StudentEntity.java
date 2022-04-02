/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author enkav
 */
@Entity
public class StudentEntity extends AccountEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    @NotNull
    private String yearOfStudy;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Long creditBalance;

    @Column(nullable = false)
    @NotNull
    private Boolean isPremium;

    @Column(nullable = false)
    @NotNull
    private Boolean isEnabled;

//no premium = default false
    @Column(nullable = false)
    @NotNull
    private Boolean optLocation;

    @OneToMany(mappedBy = "poster")
    @JsonIgnore
    private List<GroupEntity> groupsPosted;

    @ManyToMany(mappedBy = "candidates")
    @JsonIgnore
    private List<GroupEntity> groupsApplied;

    @ManyToMany(mappedBy = "groupMembers")
    @JsonIgnore
    private List<GroupEntity> groups;

    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private List<MessageEntity> messages;

    @OneToMany(mappedBy = "studentWhoReported")
    @JsonIgnore
    private List<ReportEntity> reportsSubmitted;

    @ManyToMany(mappedBy = "assignedStudents")
    @JsonIgnore
    private List<KanbanCard> assignedCards;

    @OneToMany(mappedBy = "reportedStudent")
    @JsonIgnore
    private List<ReportEntity> reportReceived;

    {
        this.groupsPosted = new ArrayList<>();
        this.groupsApplied = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.reportsSubmitted = new ArrayList<>();
        this.reportReceived = new ArrayList<>();
        this.assignedCards = new ArrayList<>();
        this.optLocation = false;
        this.isEnabled = true;
        this.isPremium = false;
        this.creditBalance = 0l;
    }

    public StudentEntity() {

    }

    public StudentEntity(String email, String username, String password, String yearOfStudy) {
        super(email, username, password);
        this.yearOfStudy = yearOfStudy;
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

    /**
     * @return the assignedCards
     */
    public List<KanbanCard> getAssignedCards() {
        return assignedCards;
    }

    /**
     * @param assignedCards the assignedCards to set
     */
    public void setAssignedCards(List<KanbanCard> assignedCards) {
        this.assignedCards = assignedCards;
    }

    public List<GroupEntity> getGroupsPosted() {
        return groupsPosted;
    }

    public void setGroupsPosted(List<GroupEntity> groupsPosted) {
        this.groupsPosted = groupsPosted;
    }

    public List<GroupEntity> getGroupsApplied() {
        return groupsApplied;
    }

    public void setGroupsApplied(List<GroupEntity> groupsApplied) {
        this.groupsApplied = groupsApplied;
    }

    public List<GroupEntity> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupEntity> groups) {
        this.groups = groups;
    }

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }

    public List<ReportEntity> getReports() {
        return reportsSubmitted;
    }

    public void setReports(List<ReportEntity> reports) {
        this.reportsSubmitted = reports;
    }

    /**
     * @return the reportReceived
     */
    public List<ReportEntity> getReportReceived() {
        return reportReceived;
    }

    /**
     * @param reportReceived the reportReceived to set
     */
    public void setReportReceived(List<ReportEntity> reportReceived) {
        this.reportReceived = reportReceived;
    }

}
