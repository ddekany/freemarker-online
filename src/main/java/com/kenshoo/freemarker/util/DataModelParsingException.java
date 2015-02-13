package com.kenshoo.freemarker.util;

/**
 * Thrown by {@link DataModelParser#parse(String)}.
 */
public class DataModelParsingException extends Exception {

    private static final long serialVersionUID = 1L;

    public DataModelParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataModelParsingException(String message) {
        super(message);
    }

}
