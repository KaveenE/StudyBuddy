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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    @Column(nullable = false, unique = true)
    @Size(min = 1, max = 255)
    @NotNull
    private String companyName;

    @Column(nullable = false)
    @NotNull
    private String imageUrl;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer numberOfClicks;

    public AdvertisementEntity() {
        this.numberOfClicks = 0;
    }

    public AdvertisementEntity(String companyName, String imageUrl) {
        this.companyName = companyName;
        this.imageUrl = imageUrl;
        this.numberOfClicks = 0;
    }

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

    /**
     * @return the numberOfClicks
     */
    public Integer getNumberOfClicks() {
        return numberOfClicks;
    }

    /**
     * @param numberOfClicks the numberOfClicks to set
     */
    public void setNumberOfClicks(Integer numberOfClicks) {
        this.numberOfClicks = numberOfClicks;
    }

}
