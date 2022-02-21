/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

import java.sql.SQLIntegrityConstraintViolationException;
import javax.persistence.PersistenceException;

/**
 *
 * @author PP42
 */
public class AlreadyExistsException extends Exception {

    public AlreadyExistsException() {
    }

    public AlreadyExistsException(String msg) {
        super(msg);
    }

    public static void throwAlreadyExistsOrUnknownException(PersistenceException persistenceExceptionThrown, AlreadyExistsException alreadyExistsException) throws AlreadyExistsException, UnknownPersistenceException {
        if (isDatabaseExceptionFromEclipseLink(persistenceExceptionThrown) && isSQLIntegrityException(persistenceExceptionThrown)) {
            throw alreadyExistsException;
        } else {
            throw new UnknownPersistenceException(persistenceExceptionThrown.getMessage());
        }

    }

    private static boolean isDatabaseExceptionFromEclipseLink(PersistenceException persistenceExceptionThrown) {
        return persistenceExceptionThrown.getCause() != null && persistenceExceptionThrown.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException");
    }

    private static boolean isSQLIntegrityException(PersistenceException persistenceExceptionThrown) {
        return persistenceExceptionThrown.getCause() != null && persistenceExceptionThrown.getCause().getCause() instanceof SQLIntegrityConstraintViolationException;
    }
}
