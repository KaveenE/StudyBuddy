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
import javax.validation.constraints.Size;
import util.helper.EJBHelper;

/**
 *
 * @author enkav
 */
@Entity
public class AdvertisementEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long advertisementId;
    
    @Column(nullable = false,unique = true, length = EJBHelper.NAME_LENGTH_MIN)
    @Size(min = EJBHelper.NAME_LENGTH_MIN , max = EJBHelper.NAME_LENGTH_MAX )
    @NotNull
    private String companyName;
    @Column(nullable = false)
    @NotNull
    private String imageUrl;

    public Long getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(Long advertisementId) {
        this.advertisementId = advertisementId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (advertisementId != null ? advertisementId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the advertisementId fields are not set
        if (!(object instanceof AdvertisementEntity)) {
            return false;
        }
        AdvertisementEntity other = (AdvertisementEntity) object;
        if ((this.advertisementId == null && other.advertisementId != null) || (this.advertisementId != null && !this.advertisementId.equals(other.advertisementId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Advertisement[ id=" + advertisementId + " ]";
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
}
