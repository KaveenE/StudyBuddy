/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author larby
 */
@Entity
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class, 
  property = "kanbanListId")
public class KanbanList implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kanbanListId;
    
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String heading;
    
    @JoinColumn(nullable = false)
    @ManyToOne
    private KanbanBoard kanbanBoard;
    
    @OneToMany(mappedBy = "kanbanList",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<KanbanCard> kanbanCards;

    public KanbanList() {
        this.kanbanCards = new ArrayList<>();
    }

    public KanbanList(String heading, KanbanBoard kanbanBoard) {
        this();
        this.heading = heading;
        this.kanbanBoard = kanbanBoard;
    }
    
    public Long getKanbanListId() {
        return kanbanListId;
    }

    public void setKanbanListId(Long kanbanListId) {
        this.kanbanListId = kanbanListId;
    }

    /**
     * @return the heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * @param heading the heading to set
     */
    public void setHeading(String heading) {
        this.heading = heading;
    }

    /**
     * @return the kanbanBoard
     */
    public KanbanBoard getKanbanBoard() {
        return kanbanBoard;
    }

    /**
     * @param kanbanBoard the kanbanBoard to set
     */
    public void setKanbanBoard(KanbanBoard kanbanBoard) {
        this.kanbanBoard = kanbanBoard;
    }

    /**
     * @return the kanbanCards
     */
    public List<KanbanCard> getKanbanCards() {
        return kanbanCards;
    }

    /**
     * @param kanbanCards the kanbanCards to set
     */
    public void setKanbanCards(List<KanbanCard> kanbanCards) {
        this.kanbanCards = kanbanCards;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kanbanListId != null ? kanbanListId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the kanbanListId fields are not set
        if (!(object instanceof KanbanList)) {
            return false;
        }
        KanbanList other = (KanbanList) object;
        if ((this.kanbanListId == null && other.kanbanListId != null) || (this.kanbanListId != null && !this.kanbanListId.equals(other.kanbanListId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.KanbanList[ id=" + kanbanListId + " ]";
    }
    
}
