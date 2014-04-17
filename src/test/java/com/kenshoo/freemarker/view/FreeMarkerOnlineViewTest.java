package com.kenshoo.freemarker.view;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/12/14
 * Time: 11:13 PM
 */
public class FreeMarkerOnlineViewTest {

    @Test
    public void testViewWithErrorInEval() {
        String error = "Error1";
        FreeMarkerOnlineView freeMarkerOnlineView = new FreeMarkerOnlineView(true, error, "", "");
        String result = freeMarkerOnlineView.getResult();
        assertEquals(result, FreeMarkerOnlineView.ERROR_IN_EVAL + error);
    }

    @Test
    public void testResultWhenAllOK() {
        String result = "Result";
        String template = "Template";
        String params = "Params";
        FreeMarkerOnlineView freeMarkerOnlineView = new FreeMarkerOnlineView(false, result, template, params);
        assertEquals(freeMarkerOnlineView.getResult(), result);
        assertEquals(freeMarkerOnlineView.getTemplate(), template);
        assertEquals(freeMarkerOnlineView.getParams(), params);

    }
}
