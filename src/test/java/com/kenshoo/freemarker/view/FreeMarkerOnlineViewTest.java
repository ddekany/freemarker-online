package com.kenshoo.freemarker.view;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/12/14
 * Time: 11:13 PM
 */
public class FreeMarkerOnlineViewTest {

    private static final String RESULT = "Result";
    private static final String TEMPLATE = "Template";
    private static final String DATA_MODEL = "DataModel";

    @Test
    public void testViewWhenDataModelIsWrong() {
        FreeMarkerOnlineView view = new FreeMarkerOnlineView(
                FreeMarkerOnlineViewResultType.DATA_MODEL_ERROR, RESULT, TEMPLATE, DATA_MODEL);
        assertEquals(view.getTemplate(), TEMPLATE);
        assertEquals(view.getDataModel(), DATA_MODEL);
        assertThat(view.getResult(), containsString(FreeMarkerOnlineView.DATA_MODEL_ERROR_MESSAGE_HEADING));
        assertThat(view.getResult(), containsString(RESULT));
        assertThat(view.getResult(), containsString(FreeMarkerOnlineView.DATA_MODEL_ERROR_MESSAGE_FOOTER));
    }
    
    @Test
    public void testViewWhenTemplateIsWrong() {
        FreeMarkerOnlineView freeMarkerOnlineView = new FreeMarkerOnlineView(
                FreeMarkerOnlineViewResultType.TEMPLATE_ERROR, RESULT, TEMPLATE, DATA_MODEL);
        String result = freeMarkerOnlineView.getResult();
        assertEquals(result, RESULT);
    }

    @Test
    public void testViewWhenAllOK() {
        FreeMarkerOnlineView view = new FreeMarkerOnlineView(
                FreeMarkerOnlineViewResultType.TEMPLATE_OUTPUT, RESULT, TEMPLATE, DATA_MODEL);
        assertEquals(view.getTemplate(), TEMPLATE);
        assertEquals(view.getDataModel(), DATA_MODEL);
        assertEquals(view.getResult(), RESULT);
    }
    
}
