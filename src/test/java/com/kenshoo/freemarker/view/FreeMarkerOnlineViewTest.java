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
