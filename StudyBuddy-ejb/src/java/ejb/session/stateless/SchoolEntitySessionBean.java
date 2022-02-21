/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.SchoolEntity;
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
public class SchoolEntitySessionBean implements SchoolEntitySessionBeanLocal {

    @PersistenceContext(unitName = "StudyBuddy-ejbPU")
    private EntityManager em;

    @Override
    public List<SchoolEntity> retrieveAllSchools() {
        Query query = em.createQuery("SELECT s FROM SchoolEntity s");

        return query.getResultList();
    }

    @Override
    public SchoolEntity retrieveSchoolById(Long schoolId) {
        Query query = em.createQuery("SELECT s FROM SchoolEntity s WHERE s.schoolId = :schoolId");
        query.setParameter("schoolId", schoolId);

        return (SchoolEntity) query.getSingleResult();
    }

    public Long createNewSchool(SchoolEntity newSchoolEntity) {
        em.persist(newSchoolEntity);
        em.flush();

        return newSchoolEntity.getschoolId();
    }
}
