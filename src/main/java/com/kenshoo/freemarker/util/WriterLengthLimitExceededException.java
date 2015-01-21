package com.kenshoo.freemarker.util;

import java.io.IOException;

/**
 * Thrown by {@link LengthLimitedWriter}.
 */
public class WriterLengthLimitExceededException extends IOException {

    private static final long serialVersionUID = 1L;
    
    public WriterLengthLimitExceededException() {
        super("The outout String length limit of the Writer was exceeded.");
    }

}
