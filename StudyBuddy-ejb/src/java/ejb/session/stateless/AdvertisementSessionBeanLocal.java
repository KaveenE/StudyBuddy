/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AdvertisementEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author SCXY
 */
@Local
public interface AdvertisementSessionBeanLocal {

    public List<AdvertisementEntity> retrieveAllAdvertisements();

    public AdvertisementEntity retrieveAdvertisementById(Long advertisementId);

    public Long createNewAdvertisement(AdvertisementEntity newAdvertisementEntity);
    
}
