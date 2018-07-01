package com.sourceforge.fsmbuilder;

/**
 * Exception which is thrown when validation fails
 */
public class ValidationException extends Exception
{
    public ValidationException(String message)
    {
        super(message);
    }
}
