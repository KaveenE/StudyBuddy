/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AccountEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author SCXY
 */
@Stateless
public class AccountSessionBean implements AccountSessionBeanLocal {

    @PersistenceContext(unitName = "StudyBuddy-ejbPU")
    private EntityManager em;

    @Override
    public List<AccountEntity> retrieveAllAccounts() {
        Query query = em.createQuery("SELECT a FROM AccountEntity a");

        return query.getResultList();
    }

    @Override
    public AccountEntity retrieveAccountById(Long accountId) {
        Query query = em.createQuery("SELECT a FROM AccountEntity a WHERE a.accountId = :accountId");
        query.setParameter("accountId", accountId);

        return (AccountEntity) query.getSingleResult();
    }

    @Override
    public Long createNewAccount(AccountEntity newAccountEntity) {
        em.persist(newAccountEntity);
        em.flush();

        return newAccountEntity.getAccountId();
    }
}
