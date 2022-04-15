/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.CardType;

/**
 *
 * @author larby
 */
@Entity
public class KanbanCard implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kanbanCardId;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private CardType cardType;
    
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String heading;

    @Column(nullable = false, length = 10_000)
    @NotNull
    @Size(min = 0, max = 10_000)
    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime deadline;

    @JoinColumn(nullable = false)
    @ManyToOne
    private StudentEntity author;

    @ManyToMany
    @JsonManagedReference
    private List<StudentEntity> assignedStudents;

    @JoinColumn(nullable = true)
    @ManyToOne
    @JsonBackReference
    private KanbanBoard kanbanBoard;

    public KanbanCard() {
        this.assignedStudents = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.cardType = CardType.TASKS;
    }
    public KanbanCard(String heading, String description, StudentEntity author, CardType cardType) {
        this();
        this.heading = heading;
        this.description = description;
        this.author = author;
        this.cardType = cardType;
    }

    public KanbanCard(String heading, String description, StudentEntity author, CardType cardType, LocalDateTime deadline) {
        this(heading, description, author,cardType);
        this.deadline = deadline;
    }
    
    public KanbanCard(String heading, String description, StudentEntity author,CardType cardType,LocalDateTime deadline,KanbanBoard kanbanBoard) {
        this(heading, description, author, cardType,deadline);
        this.kanbanBoard = kanbanBoard;
    }
    
     @Override
    public int hashCode() {
        int hash = 0;
        hash += (kanbanCardId != null ? kanbanCardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
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
        return "entities.KanbanCard[ id=" + kanbanCardId + " ]";
    }
    public Long getKanbanCardId() {
        return kanbanCardId;
    }

    public void setKanbanCardId(Long kanbanCardId) {
        this.kanbanCardId = kanbanCardId;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public StudentEntity getAuthor() {
        return author;
    }

    public void setAuthor(StudentEntity author) {
        this.author = author;
    }

    public List<StudentEntity> getAssignedStudents() {
        return assignedStudents;
    }

    public void setAssignedStudents(List<StudentEntity> assignedStudents) {
        this.assignedStudents = assignedStudents;
    }

    public KanbanBoard getKanbanBoard() {
        return kanbanBoard;
    }

    public void setKanbanBoard(KanbanBoard kanbanBoard) {
        this.kanbanBoard = kanbanBoard;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }
}
