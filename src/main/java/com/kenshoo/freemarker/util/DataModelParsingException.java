package com.kenshoo.freemarker.util;

import java.util.TimeZone;

/**
 * Thrown by {@link DataModelParser#parse(String, TimeZone)}.
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
