/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.ModuleEntity;
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
public class ModuleEntitySessionBean implements ModuleEntitySessionBeanLocal {

    @PersistenceContext(unitName = "StudyBuddy-ejbPU")
    private EntityManager em;

    @Override
    public List<ModuleEntity> retrieveAllModules() {
        Query query = em.createQuery("SELECT m FROM ModuleEntity m");

        return query.getResultList();
    }

    public ModuleEntity retrieveModuleById(Long moduleId) {
        Query query = em.createQuery("SELECT m FROM ModuleEntity m WHERE m.moduleId = :moduleId");
        query.setParameter("moduleId", moduleId);

        return (ModuleEntity) query.getSingleResult();
    }
}
