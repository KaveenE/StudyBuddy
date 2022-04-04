/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import java.util.Set;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

/**
 *
 * @author larby
 */
@javax.ws.rs.ApplicationPath("Resources")
public class ApplicationConfig extends javax.ws.rs.core.Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        resources.add(MultiPartFeature.class);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ws.restful.GroupResource.class);
        resources.add(ws.restful.KanbanResource.class);
        resources.add(ws.restful.ModuleResource.class);
        resources.add(ws.restful.RatingResource.class);
        resources.add(ws.restful.ReportResource.class);
        resources.add(ws.restful.SchoolResource.class);
        resources.add(ws.restful.StudentResource.class);
    }
    
}
