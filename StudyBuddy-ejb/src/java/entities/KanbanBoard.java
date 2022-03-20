/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
public class KanbanBoard implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kanbanBoardId;
    
    @Column(nullable = false)
    @Size(min = 1, max = 125)
    @NotNull
    private String heading;
    
    @JoinColumn() // nullable = true cos i saw u setting setGroup(true) in bean
    @ManyToOne(optional = false)
    private GroupEntity group;
    
    @OneToMany(mappedBy = "kanbanBoard",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<KanbanList> kanbanLists;

    public KanbanBoard() {
        this.kanbanLists = new ArrayList<>();
    }
    
    public KanbanBoard(String heading, GroupEntity group) {
        this();
        this.heading = heading;
        this.group = group;
    }

    public Long getKanbanBoardId() {
        return kanbanBoardId;
    }

    public void setKanbanBoardId(Long kanbanBoardId) {
        this.kanbanBoardId = kanbanBoardId;
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
     * @return the group
     */
    public GroupEntity getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    /**
     * @return the kanbanList
     */
    public List<KanbanList> getKanbanLists() {
        return kanbanLists;
    }

    /**
     * @param kanbanList the kanbanList to set
     */
    public void setKanbanLists(List<KanbanList> kanbanLists) {
        this.kanbanLists = kanbanLists;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kanbanBoardId != null ? kanbanBoardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the kanbanBoardId fields are not set
        if (!(object instanceof KanbanBoard)) {
            return false;
        }
        KanbanBoard other = (KanbanBoard) object;
        if ((this.kanbanBoardId == null && other.kanbanBoardId != null) || (this.kanbanBoardId != null && !this.kanbanBoardId.equals(other.kanbanBoardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.KanbanBoard[ id=" + kanbanBoardId + " ]";
    }
    
}
