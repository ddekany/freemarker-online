package com.kenshoo.freemarker.resources;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.kenshoo.freemarker.services.FreeMarkerService;
import com.kenshoo.freemarker.services.FreeMarkerServiceResponse;
import com.kenshoo.freemarker.view.FreeMarkerOnlineView;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/12/14
 * Time: 11:23 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class FreeMarkerOnlineResultResourceTest {

    private static final String ERROR_DESCRIPTION = "Error description";
    private static final String DATA_MODEL = "x=1";
    private static final String WRONG_DATA_MODEL = "x=[";
    private static final String TEMPLATE = "Template";
    private static final String RESULT = "Result";

    @InjectMocks
    FreeMarkerOnlineResultResource freeMarkerOnlineResultResource;

    @Mock
    FreeMarkerService freeMarkerService;

    @Test
    public void testGoodResult() {
        when(freeMarkerService.calculateTemplateOutput(anyString(), anyMap())).thenReturn(
                new FreeMarkerServiceResponse.Builder().buildForSuccess(RESULT, false));
        FreeMarkerOnlineView view = freeMarkerOnlineResultResource.formResult(TEMPLATE, DATA_MODEL);
        assertEquals(view.getTemplate(), TEMPLATE);
        assertEquals(view.getDataModel(), DATA_MODEL);
        assertEquals(view.getResult(), RESULT);
    }

    @Test
    public void testWrongTemplate() {
        when(freeMarkerService.calculateTemplateOutput(anyString(), anyMap())).thenReturn(
                new FreeMarkerServiceResponse.Builder().buildForFailure(new Exception(ERROR_DESCRIPTION)));
        FreeMarkerOnlineView view = freeMarkerOnlineResultResource.formResult(TEMPLATE, DATA_MODEL);
        assertEquals(view.getTemplate(), TEMPLATE);
        assertEquals(view.getDataModel(), DATA_MODEL);
        assertThat(view.getResult(), containsString(ERROR_DESCRIPTION));
    }

    @Test
    public void testWrongDataModel() {
        FreeMarkerOnlineView view = freeMarkerOnlineResultResource.formResult(TEMPLATE, WRONG_DATA_MODEL);
        assertEquals(view.getTemplate(), TEMPLATE);
        assertEquals(view.getDataModel(), WRONG_DATA_MODEL);
        assertThat(view.getResult(), containsString("data model"));
    }
    
}
