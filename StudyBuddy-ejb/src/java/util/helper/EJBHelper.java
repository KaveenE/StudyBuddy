/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.helper;

import java.util.Objects;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;

/**
 *
 * @author enkav
 */
public class EJBHelper {
   

    //Ease of bean validation
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static EJBHelper singleton;

    private EJBHelper() {
    }

    public static EJBHelper getSingleton() {
        if (singleton == null) {
            singleton = new EJBHelper();
        }

        return singleton;

    }
    
    //Meant to throw specific exceptions for null objects rather than NPE
    //Typically specific exception is related to entity not existing
    //Didnt create a DoesNotExist exception since I lazy refactor Prof's code to extend 
    public static <T> T requireNonNull(T obj, DoesNotExistException ex) throws DoesNotExistException {
        T returnObj;
        try {
            returnObj = Objects.requireNonNull(obj, ex.getMessage());
        } catch (NullPointerException npe) {
            throw ex;
        }

        return returnObj;
    }
    
    public static <T> void throwValidationErrorsIfAny(T entity) throws InputDataValidationException {
        Set<ConstraintViolation<T>> errors = validator.validate(entity);
        StringBuilder sb = new StringBuilder();

        if (!errors.isEmpty()) {
            errors.forEach(error -> sb.append(String.format("In %s.%s: input %s %s\n",
                    error.getRootBeanClass(), error.getPropertyPath(), error.getInvalidValue(), error.getMessage())));
            throw new InputDataValidationException(sb.toString());
        }
    }
    
}