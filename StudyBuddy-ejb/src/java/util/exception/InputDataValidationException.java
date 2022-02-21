package util.exception;



// Newly added in v4.2

public class InputDataValidationException extends Exception
{
    public InputDataValidationException() 
    {
    }

    
    
    public InputDataValidationException(String msg) 
    {
        super(msg);
    }
}
