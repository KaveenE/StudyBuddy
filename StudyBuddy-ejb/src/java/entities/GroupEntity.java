/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author SCXY
 */
@Entity
public class GroupEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(nullable = false)
    @Size(min = 3, max = 255)
    @NotNull
    private String groupName;
    @Column(nullable = false)
    @NotNull
    private String description;
    @Column(nullable = false)
    @NotNull
    private LocalDateTime dateTimeCreated;
    @Column(nullable = false)
    @NotNull
    private Boolean isOpen;
    @Column(nullable = false)
    @NotNull
    private Boolean isDeleted;

    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private ModuleEntity moduleEntity;

    @OneToMany(mappedBy = "group")
    private List<MessageEntity> messages;

    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private StudentEntity poster;

    @ManyToMany
    @JoinTable(name = "groupApplied_candidates", joinColumns = @JoinColumn(name = "candidate_studentid"), inverseJoinColumns = @JoinColumn(name = "groupApplied_groupid"))
    private List<StudentEntity> candidates;

    @ManyToMany
    @JoinTable(name = "groups_groupMembers", joinColumns = @JoinColumn(name = "groupMember_studentid"), inverseJoinColumns = @JoinColumn(name = "groups_groupid"))
    private List<StudentEntity> groupMembers;
    
    @OneToMany(mappedBy = "group")
    private List<KanbanBoard> kanbanBoard;

    public GroupEntity() {
        this.candidates = new ArrayList<>();
        this.groupMembers = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.isOpen = true;
        this.isDeleted = false;
        this.dateTimeCreated = LocalDateTime.now();
        this.kanbanBoard = new ArrayList<>();
    }

    public GroupEntity(String groupName, String description) {
        this();
        this.groupName = groupName;
        this.description = description;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public StudentEntity getPoster() {
        return poster;
    }

    public void setPoster(StudentEntity poster) {
        this.poster = poster;
    }

    public List<StudentEntity> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<StudentEntity> candidates) {
        this.candidates = candidates;
    }

    public List<StudentEntity> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<StudentEntity> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }

    /**
     * @return the kanbanBoard
     */
    public List<KanbanBoard> getKanbanBoard() {
        return kanbanBoard;
    }

    /**
     * @param kanbanBoard the kanbanBoard to set
     */
    public void setKanbanBoard(List<KanbanBoard> kanbanBoard) {
        this.kanbanBoard = kanbanBoard;
    }

    public String getGroupName() {
        return groupName;
    }

    public ModuleEntity getModuleEntity() {
        return moduleEntity;
    }

    public void setModuleEntity(ModuleEntity moduleEntity) {
        this.moduleEntity = moduleEntity;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    /**
     * @return the dateTimeCreated
     */
    public LocalDateTime getDateTimeCreated() {
        return dateTimeCreated;
    }

    /**
     * @param dateTimeCreated the dateTimeCreated to set
     */
    public void setDateTimeCreated(LocalDateTime dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupId != null ? groupId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the groupId fields are not set
        if (!(object instanceof GroupEntity)) {
            return false;
        }
        GroupEntity other = (GroupEntity) object;
        if ((this.groupId == null && other.groupId != null) || (this.groupId != null && !this.groupId.equals(other.groupId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.GroupEntity[ id=" + groupId + " ]";
    }

}
