package com.kenshoo.freemarker.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class LengthLimitedWriterTest {

    private StringWriter wrappedW = new StringWriter();
    private LengthLimitedWriter w = new LengthLimitedWriter(wrappedW, 5);
    
    @Test
    public void testLimitNotExceeded() throws IOException {
        w.write("123");
        w.write("45");
    }

    @Test
    public void testLimitExceededWithString() throws IOException {
        w.write("123");
        try {
            w.write("456");
            fail();
        } catch (LengthLimitExceededException e) {
            assertEquals("12345", wrappedW.toString());
        }
    }

    @Test
    public void testLimitExceededWithCharArray() throws IOException {
        w.write(new char[] { '1', '2', '3' });
        try {
            w.write(new char[] { '4', '5', '6' });
            fail();
        } catch (LengthLimitExceededException e) {
            assertEquals("12345", wrappedW.toString());
        }
    }

    @Test
    public void testLimitExceededWithChar() throws IOException {
        w.write('1');
        w.write('2');
        w.write('3');
        w.write('4');
        w.write('5');
        try {
            w.write('6');
            fail();
        } catch (LengthLimitExceededException e) {
            assertEquals("12345", wrappedW.toString());
        }
    }
    
}
