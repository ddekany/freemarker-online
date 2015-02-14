package com.kenshoo.freemarker.util;

import freemarker.core.ParseException;
import freemarker.template.TemplateException;

public final class ExceptionUtils {

    private ExceptionUtils() {
        // Not meant to be instantiated
    }

    /**
     * The error message (and sometimes also the class), and then the same with the cause exception, and so on. Doesn't
     * contain the stack trace or other location information.
     */
    public static String getMessageWithCauses(final Throwable exc) {
        StringBuilder sb = new StringBuilder();
        
        Throwable curExc = exc;
        while (curExc != null) {
            if (curExc != exc) {
                sb.append("\n\nCaused by:\n");
            }
            String msg = curExc.getMessage();
            if (msg == null || !(curExc instanceof TemplateException || curExc instanceof ParseException)) {
                sb.append(curExc.getClass().getName()).append(": ");
            }
            sb.append(msg);
            curExc = curExc.getCause();
        }
        return sb.toString();
    }

}
