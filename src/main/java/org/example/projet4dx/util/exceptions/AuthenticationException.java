package org.example.projet4dx.util.exceptions;

/**
 * Custom exception class for authentication related errors.
 */
public class AuthenticationException extends Exception{
    public AuthenticationException(String message) {
        super(message);
    }
}
