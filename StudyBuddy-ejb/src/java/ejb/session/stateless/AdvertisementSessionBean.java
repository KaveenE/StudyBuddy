/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AdvertisementEntity;
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
public class AdvertisementSessionBean implements AdvertisementSessionBeanLocal {

    @PersistenceContext(unitName = "StudyBuddy-ejbPU")
    private EntityManager em;

    @Override
    public List<AdvertisementEntity> retrieveAllAdvertisements() {
        Query query = em.createQuery("SELECT a FROM AdvertisementEntity a");

        return query.getResultList();
    }

    @Override
    public AdvertisementEntity retrieveAdvertisementById(Long advertisementId) {
        Query query = em.createQuery("SELECT a FROM AdvertisementEntity a WHERE a.advertisementId = :advertisementId");
        query.setParameter("advertisementId", advertisementId);

        return (AdvertisementEntity) query.getSingleResult();
    }

    @Override
    public Long createNewAdvertisement(AdvertisementEntity newAdvertisementEntity) {
        em.persist(newAdvertisementEntity);
        em.flush();

        return newAdvertisementEntity.getAdvertisementId();
    }
}
