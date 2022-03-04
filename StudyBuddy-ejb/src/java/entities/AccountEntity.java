/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author enkav
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AccountEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false, unique = true)
    @NotNull
    @Email
    private String email;
    @Column(nullable = false, unique = true, length = 32)
    @NotNull
    @Size(min = 4, max = 32)
    private String username;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 8, max = 255)
    //At least 1 digit,lowercase,uppercase,symbol,8 characters & no spaces
//    @Pattern(regexp = "^(?=.*[0-9]) (?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$){8,255}$")
    private String password;


    @OneToMany(mappedBy = "rater")
    private List<RatingEntity> ratingByOthers;

    @OneToMany(mappedBy = "ratee")
    private List<AccountEntity> ratingOthers;


    public AccountEntity() {
    }

    public AccountEntity(String email, String username, String password) {
        this.ratingByOthers = new ArrayList<>();
        this.ratingOthers = new ArrayList<>();
        this.email = email;
        this.username = username;
        this.password = password; 
    }
    
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountId != null ? accountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the accountId fields are not set
        if (!(object instanceof AccountEntity)) {
            return false;
        }
        AccountEntity other = (AccountEntity) object;
        if ((this.accountId == null && other.accountId != null) || (this.accountId != null && !this.accountId.equals(other.accountId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.AccountEntity[ id=" + accountId + " ]";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the ratingByOthers
     */
    public List<RatingEntity> getRatingByOthers() {
        return ratingByOthers;
    }

    /**
     * @param ratingByOthers the ratingByOthers to set
     */
    public void setRatingByOthers(List<RatingEntity> ratingByOthers) {
        this.ratingByOthers = ratingByOthers;
    }

    /**
     * @return the ratingOthers
     */
    public List<AccountEntity> getRatingOthers() {
        return ratingOthers;
    }

    /**
     * @param ratingOthers the ratingOthers to set
     */
    public void setRatingOthers(List<AccountEntity> ratingOthers) {
        this.ratingOthers = ratingOthers;
    }

}
