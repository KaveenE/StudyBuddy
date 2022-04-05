/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author larby
 */
@Entity
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class, 
  property = "kanbanCardId")
public class KanbanCard implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kanbanCardId;

    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String title;

    @Column(nullable = false, length = 10_000)
    @NotNull
    @Size(min = 0, max = 10_000)
    private String description;

    private LocalDateTime deadlineStart;

    private LocalDateTime deadlineEnd;

    @JoinColumn(nullable = false)
    @ManyToOne
    private StudentEntity author;

    @ManyToMany
    private List<StudentEntity> assignedStudents;

    @JoinColumn(nullable = false)
    @ManyToOne
    private KanbanList kanbanList;

    public KanbanCard() {
        this.assignedStudents = new ArrayList<>();
    }

    public KanbanCard(String title, String description, LocalDateTime deadlineStart, LocalDateTime deadlineEnd, StudentEntity author) {
        this();
        this.title = title;
        this.description = description;
        this.deadlineStart = deadlineStart;
        this.deadlineEnd = deadlineEnd;
        this.author = author;
    }

    public KanbanCard(String title, String description, LocalDateTime deadlineStart, LocalDateTime deadlineEnd, StudentEntity author, KanbanList kanbanList) {
        this(title, description, deadlineStart, deadlineEnd, author);
        this.kanbanList = kanbanList;
    }

    public Long getKanbanCardId() {
        return kanbanCardId;
    }

    public void setKanbanCardId(Long kanbanCardId) {
        this.kanbanCardId = kanbanCardId;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the deadlineStart
     */
    public LocalDateTime getDeadlineStart() {
        return deadlineStart;
    }

    /**
     * @param deadlineStart the deadlineStart to set
     */
    public void setDeadlineStart(LocalDateTime deadlineStart) {
        this.deadlineStart = deadlineStart;
    }

    /**
     * @return the deadlineEnd
     */
    public LocalDateTime getDeadlineEnd() {
        return deadlineEnd;
    }

    /**
     * @param deadlineEnd the deadlineEnd to set
     */
    public void setDeadlineEnd(LocalDateTime deadlineEnd) {
        this.deadlineEnd = deadlineEnd;
    }

    /**
     * @return the author
     */
    public StudentEntity getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(StudentEntity author) {
        this.author = author;
    }

    /**
     * @return the assignedStudents
     */
    public List<StudentEntity> getAssignedStudents() {
        return assignedStudents;
    }

    /**
     * @param assignedStudents the assignedStudents to set
     */
    public void setAssignedStudents(List<StudentEntity> assignedStudents) {
        this.assignedStudents = assignedStudents;
    }

    /**
     * @return the kanbanList
     */
    public KanbanList getKanbanList() {
        return kanbanList;
    }

    /**
     * @param kanbanList the kanbanList to set
     */
    public void setKanbanList(KanbanList kanbanList) {
        this.kanbanList = kanbanList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kanbanCardId != null ? kanbanCardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the kanbanCardId fields are not set
        if (!(object instanceof KanbanCard)) {
            return false;
        }
        KanbanCard other = (KanbanCard) object;
        if ((this.kanbanCardId == null && other.kanbanCardId != null) || (this.kanbanCardId != null && !this.kanbanCardId.equals(other.kanbanCardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.kanbanCard[ id=" + kanbanCardId + " ]";
    }

}
