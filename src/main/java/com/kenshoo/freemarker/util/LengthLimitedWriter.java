package com.kenshoo.freemarker.util;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * A {@link StringWriter} that limits its buffer size, and throws {@link LengthLimitExceededException} when that's
 * exceeded.
 */
public class LengthLimitedWriter extends FilterWriter {
    
    private int lengthLeft;

    public LengthLimitedWriter(Writer writer, int lengthLimit) {
        super(writer);
        this.lengthLeft = lengthLimit;
    }

    @Override
    public void write(int c) throws IOException {
        if (lengthLeft < 1) {
            throw new LengthLimitExceededException();
        }
        
        super.write(c);
        
        lengthLeft--;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        final boolean lengthExceeded;
        if (lengthLeft < len) {
            len = lengthLeft;
            lengthExceeded = true;
        } else {
            lengthExceeded = false;
        }
        
        super.write(cbuf, off, len);
        lengthLeft -= len;
        
        if (lengthExceeded) {
            throw new LengthLimitExceededException();
        }
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        final boolean lengthExceeded;
        if (lengthLeft < len) {
            len = lengthLeft;
            lengthExceeded = true;
        } else {
            lengthExceeded = false;
        }
        
        super.write(str, off, len);
        lengthLeft -= len;
        
        if (lengthExceeded) {
            throw new LengthLimitExceededException();
        }
    }

}
