/*
 * Copyright 2014 Kenshoo.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import com.kenshoo.freemarker.view.FreeMarkerOnlineViewResultType;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/12/14
 * Time: 11:23 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class FreeMarkerOnlineResourceTest {

    private static final String ERROR_DESCRIPTION = "Error description";
    private static final String DATA_MODEL = "x=1";
    private static final String MALFORMED_DATA_MODEL_VARIABLE_NAME = "problematicVariable";
    private static final String MALFORMED_DATA_MODEL = MALFORMED_DATA_MODEL_VARIABLE_NAME + "=[";
    private static final String TEMPLATE = "Template";
    private static final String RESULT = "Result";

    @InjectMocks
    FreeMarkerOnlineResource freeMarkerOnlineResultResource;

    @Mock
    FreeMarkerService freeMarkerService;

    @Test
    public void testInitialForm() {
        when(freeMarkerService.calculateTemplateOutput(anyString(), anyMap())).thenThrow(new AssertionError());
        FreeMarkerOnlineView view = freeMarkerOnlineResultResource.blankForm();
        assertEquals(view.getTemplate(), "");
        assertEquals(view.getDataModel(), "");
        assertEquals(view.getResultType(), FreeMarkerOnlineViewResultType.BLANK);
        assertNull(view.getResult());
    }
    
    @Test
    public void testPostedBlankForm() {
        when(freeMarkerService.calculateTemplateOutput(anyString(), anyMap())).thenThrow(new AssertionError());
        FreeMarkerOnlineView view = freeMarkerOnlineResultResource.formResult(null, null);
        assertEquals(view.getTemplate(), "");
        assertEquals(view.getDataModel(), "");
        assertEquals(view.getResultType(), FreeMarkerOnlineViewResultType.BLANK);
        assertNull(view.getResult());
    }
    
    @Test
    public void testGoodResult() {
        when(freeMarkerService.calculateTemplateOutput(anyString(), anyMap())).thenReturn(
                new FreeMarkerServiceResponse.Builder().buildForSuccess(RESULT, false));
        FreeMarkerOnlineView view = freeMarkerOnlineResultResource.formResult(TEMPLATE, DATA_MODEL);
        assertEquals(view.getTemplate(), TEMPLATE);
        assertEquals(view.getDataModel(), DATA_MODEL);
        assertEquals(view.getResultType(), FreeMarkerOnlineViewResultType.TEMPLATE_OUTPUT);
        assertEquals(view.getResult(), RESULT);
    }

    @Test
    public void testWrongTemplate() {
        when(freeMarkerService.calculateTemplateOutput(anyString(), anyMap())).thenReturn(
                new FreeMarkerServiceResponse.Builder().buildForFailure(new Exception(ERROR_DESCRIPTION)));
        FreeMarkerOnlineView view = freeMarkerOnlineResultResource.formResult(TEMPLATE, DATA_MODEL);
        assertEquals(view.getTemplate(), TEMPLATE);
        assertEquals(view.getDataModel(), DATA_MODEL);
        assertEquals(view.getResultType(), FreeMarkerOnlineViewResultType.TEMPLATE_ERROR);
        assertThat(view.getResult(), containsString(ERROR_DESCRIPTION));
    }
    
    @Test
    public void testTooLongTemplate() {
        final String veryLongTemplate = create30KString();
        
        FreeMarkerOnlineView view = freeMarkerOnlineResultResource.formResult(veryLongTemplate, DATA_MODEL);
        assertEquals(view.getTemplate(), veryLongTemplate);
        assertEquals(view.getDataModel(), DATA_MODEL);
        assertEquals(view.getResultType(), FreeMarkerOnlineViewResultType.TEMPLATE_ERROR);
        assertThat(view.getResult(), containsString("template"));
        assertThat(view.getResult(), containsString("limit"));
    }

    @Test
    public void testMalformedDataModel() {
        FreeMarkerOnlineView view = freeMarkerOnlineResultResource.formResult(TEMPLATE, MALFORMED_DATA_MODEL);
        assertEquals(view.getTemplate(), TEMPLATE);
        assertEquals(view.getDataModel(), MALFORMED_DATA_MODEL);
        assertEquals(view.getResultType(), FreeMarkerOnlineViewResultType.DATA_MODEL_ERROR);
        assertThat(view.getResult(), containsString("data model"));
        assertThat(view.getResult(), containsString(MALFORMED_DATA_MODEL_VARIABLE_NAME));
    }

    @Test
    public void testTooLongDataModel() {
        final String veryLongDataModel = create30KString();
        
        FreeMarkerOnlineView view = freeMarkerOnlineResultResource.formResult(TEMPLATE, veryLongDataModel);
        assertEquals(view.getTemplate(), TEMPLATE);
        assertEquals(view.getDataModel(), veryLongDataModel);
        assertEquals(view.getResultType(), FreeMarkerOnlineViewResultType.DATA_MODEL_ERROR);
        assertThat(view.getResult(), containsString("data model"));
        assertThat(view.getResult(), containsString("limit"));
    }

    private String create30KString() {
        final String veryLongDataModel;
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 30000 / 10; i++) {
                sb.append("0123456789");
            }
            veryLongDataModel = sb.toString();
        }
        return veryLongDataModel;
    }
    
}
