/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author enkav
 */
@Entity
public abstract class AccountEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    
    @Column(nullable = false,unique = true)
    @NotNull
    @Email
    private String email;
    @Column(nullable = false,unique = true)
    @NotNull
    private String username;
    @Column(nullable = false, length = 8)
    @NotNull
    //Atleast 1 digit,lowercase,uppercase,symbol,8 characters & no spaces
    @Pattern(regexp = "^(?=.*[0-9]) (?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$){8,}$")
    private String password;
    
    @Embedded
    @Column(nullable = false)
    private RatingEntity ratingEntity;
    
    @OneToMany(mappedBy = "rater")
    private List<AccountEntity> ratingToEntities; 
    @OneToMany(mappedBy = "ratee")
    private List<AccountEntity> ratingFromEntities;

    public AccountEntity() {
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

    public RatingEntity getRatingEntity() {
        return ratingEntity;
    }

    public void setRatingEntity(RatingEntity ratingEntity) {
        this.ratingEntity = ratingEntity;
    }

    public List<AccountEntity> getRatingToEntities() {
        return ratingToEntities;
    }

    public void setRatingToEntities(List<AccountEntity> ratingToEntities) {
        this.ratingToEntities = ratingToEntities;
    }

    public List<AccountEntity> getRatingFromEntities() {
        return ratingFromEntities;
    }

    public void setRatingFromEntities(List<AccountEntity> ratingFromEntities) {
        this.ratingFromEntities = ratingFromEntities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
