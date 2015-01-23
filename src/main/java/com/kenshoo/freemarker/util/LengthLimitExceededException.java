package com.kenshoo.freemarker.util;

import java.io.IOException;

/**
 * Thrown by {@link LengthLimitedWriter}.
 */
public class LengthLimitExceededException extends IOException {

    private static final long serialVersionUID = 1L;
    
    public LengthLimitExceededException() {
        super("The outout String length limit of the Writer was exceeded.");
    }

}
