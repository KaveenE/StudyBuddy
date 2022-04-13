/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.AccountSessionBeanLocal;
import ejb.session.stateless.GroupEntitySessionBeanLocal;
import ejb.session.stateless.KanbanSessionBeanLocal;
import ejb.session.stateless.ModuleSessionBeanLocal;
import ejb.session.stateless.RatingSessionBeanLocal;
import ejb.session.stateless.ReportSessionBeanLocal;
import ejb.session.stateless.SchoolSessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author enkav
 */
public class SessionBeanLookup {

    private final String ejbModuleJndiPath = "java:global/StudyBuddy/StudyBuddy-ejb/";

    public SessionBeanLookup() {
    }

    public StudentSessionBeanLocal lookupStudentSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (StudentSessionBeanLocal) c.lookup(ejbModuleJndiPath + "StudentSessionBean!ejb.session.stateless.StudentSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public SchoolSessionBeanLocal lookupSchoolSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (SchoolSessionBeanLocal) c.lookup(ejbModuleJndiPath + "SchoolSessionBean!ejb.session.stateless.SchoolSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }

    }

    public ModuleSessionBeanLocal lookupModuleSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (ModuleSessionBeanLocal) c.lookup(ejbModuleJndiPath + "ModuleSessionBean!ejb.session.stateless.ModuleSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public GroupEntitySessionBeanLocal lookupGroupEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (GroupEntitySessionBeanLocal) c.lookup("java:global/StudyBuddy/StudyBuddy-ejb/GroupEntitySessionBean!ejb.session.stateless.GroupEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public RatingSessionBeanLocal lookupRatingSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (RatingSessionBeanLocal) c.lookup("java:global/StudyBuddy/StudyBuddy-ejb/RatingSessionBean!ejb.session.stateless.RatingSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public ReportSessionBeanLocal lookupReportSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (ReportSessionBeanLocal) c.lookup("java:global/StudyBuddy/StudyBuddy-ejb/ReportSessionBean!ejb.session.stateless.ReportSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public KanbanSessionBeanLocal lookupKanbanSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (KanbanSessionBeanLocal) c.lookup("java:global/StudyBuddy/StudyBuddy-ejb/KanbanSessionBean!ejb.session.stateless.KanbanSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public AccountSessionBeanLocal lookupAccountSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (AccountSessionBeanLocal) c.lookup("java:global/StudyBuddy/StudyBuddy-ejb/AccountSessionBean!ejb.session.stateless.AccountSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
