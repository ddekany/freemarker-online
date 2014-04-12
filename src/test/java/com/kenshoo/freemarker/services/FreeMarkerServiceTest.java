package com.kenshoo.freemarker.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/12/14
 * Time: 11:15 AM
 */
@RunWith(MockitoJUnitRunner.class)
public class FreeMarkerServiceTest {

    @InjectMocks
    FreeMarkerService freeMarkerService;

    @Test
    public void testCalculationOfATemplateWithNoParams() {
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateFreeMarkerTemplate("test", new HashMap<String, String>());
        assertThat(serviceResponse.isSucceed(), is(true));
        assertThat(serviceResponse.getResult(), is("test"));
    }

    @Test
    public void testSimpleTemplate() {
        HashMap<String, String> params = new HashMap<>();
        params.put("var1", "val1");
        String template = "${var1}";
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateFreeMarkerTemplate(template, params);
        assertThat(serviceResponse.getResult(), equalTo("val1"));
    }

    @Test
    public void testTemplateWithFewArgsAndOperators() {
        HashMap<String, String> params = new HashMap<>();
        params.put("var1", "val1");
        params.put("var2", "val2");
        String template = "${var1?capitalize} ${var2?cap_first}";
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateFreeMarkerTemplate(template, params);
        assertThat(serviceResponse.getResult(), equalTo("Val1 Val2"));
    }

    @Test
    public void testCalculationOfAWrongTemplate() {
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateFreeMarkerTemplate("test ${xx", new HashMap<String, String>());
        assertThat(serviceResponse.isSucceed(), is(false));
        assertNotEquals("", serviceResponse.getErrorReason());
    }

    @Test
    public void testEvaluationError() {
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateFreeMarkerTemplate("test ${x}", new HashMap<String, String>());
        assertThat(serviceResponse.isSucceed(), is(false));
    }


}
