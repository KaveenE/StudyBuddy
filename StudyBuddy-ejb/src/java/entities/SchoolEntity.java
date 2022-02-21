/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.helper.EJBHelper;

/**
 *
 * @author SCXY
 */
@Entity
public class SchoolEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schoolId;
    
    @Column(nullable = false, unique = true, length = EJBHelper.NAME_LENGTH_MAX)
    @Size(min = EJBHelper.NAME_LENGTH_MIN, max = EJBHelper.NAME_LENGTH_MAX)
    @NotNull 
    private String name;

    @OneToMany(mappedBy = "school")
    private List<ModuleEntity> moduleEntities;

    public SchoolEntity() {
    }

    public SchoolEntity(String name) {
        this.name = name;
    }

    public List<ModuleEntity> getModuleEntities() {
        return moduleEntities;
    }

    public void setModuleEntities(List<ModuleEntity> moduleEntities) {
        this.moduleEntities = moduleEntities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getschoolId() {
        return schoolId;
    }

    public void setschoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (schoolId != null ? schoolId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the schoolId fields are not set
        if (!(object instanceof SchoolEntity)) {
            return false;
        }
        SchoolEntity other = (SchoolEntity) object;
        if ((this.schoolId == null && other.schoolId != null) || (this.schoolId != null && !this.schoolId.equals(other.schoolId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.SchoolEntity[ id=" + schoolId + " ]";
    }

}
