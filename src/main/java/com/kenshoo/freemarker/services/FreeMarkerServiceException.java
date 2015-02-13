package com.kenshoo.freemarker.services;

/**
 * When {@link FreeMarkerService} fails on an unexpected way (non-user error). 
 */
public class FreeMarkerServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FreeMarkerServiceException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public FreeMarkerServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
