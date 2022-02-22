/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author enkav
 */
@Embeddable
public class RatingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(nullable = false,scale = 1, precision = 2)
    @NotNull
    private Double rating;
    @Column(nullable = false)
    private String ratingDescription;

    public RatingEntity() {
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getRatingDescription() {
        return ratingDescription;
    }

    public void setRatingDescription(String ratingDescription) {
        this.ratingDescription = ratingDescription;
    }
    
}
