package com.kenshoo.freemarker.resources;

import com.kenshoo.freemarker.services.FreeMarkerService;
import com.kenshoo.freemarker.services.FreeMarkerServiceResponse;
import com.kenshoo.freemarker.view.FreeMarkerOnlineView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/12/14
 * Time: 11:23 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class FreeMarkerOnlineResultResourceTest {

    @InjectMocks
    FreeMarkerOnlineResultResource freeMarkerOnlineResultResource;

    @Mock
    FreeMarkerService freeMarkerService;

    @Test
    public void testGoodResult() {
        when(freeMarkerService.calculateFreeMarkerTemplate(anyString(), anyMap())).thenReturn(new FreeMarkerServiceResponse.Builder().successfulResponse("Result", false));
        FreeMarkerOnlineView view = freeMarkerOnlineResultResource.formResult("template", "params");
        assertEquals(view.getResult(), "Result");
        assertEquals(view.getTemplate(), "template");
        assertEquals(view.getParams(), "params");

    }

    @Test
    public void testWrongTemplate() {
        when(freeMarkerService.calculateFreeMarkerTemplate(anyString(), anyMap())).thenReturn(new FreeMarkerServiceResponse.Builder().errorResponse("Error"));
        FreeMarkerOnlineView view = freeMarkerOnlineResultResource.formResult("template", "params");
        assertEquals(view.getResult(), "Error");
        assertEquals(view.getTemplate(), "template");
        assertEquals(view.getParams(), "params");

    }
}
