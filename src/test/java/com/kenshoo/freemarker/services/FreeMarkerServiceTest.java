package com.kenshoo.freemarker.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/12/14
 * Time: 11:15 AM
 */
@RunWith(MockitoJUnitRunner.class)
public class FreeMarkerServiceTest {

    private static final String TRUNCATION_TEST_TEMPLATE = "12345";
    @InjectMocks
    FreeMarkerService freeMarkerService;

    @Test
    public void testCalculationOfATemplateWithNoParams() {
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateFreeMarkerTemplate("test", Collections.<String,String>emptyMap());
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
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateFreeMarkerTemplate("test ${xx", Collections.<String,String>emptyMap());
        assertThat(serviceResponse.isSucceed(), is(false));
        assertNotEquals("", serviceResponse.getErrorReason());
    }

    @Test
    public void testEvaluationError() {
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateFreeMarkerTemplate("test ${x}", Collections.<String,String>emptyMap());
        assertThat(serviceResponse.isSucceed(), is(false));
    }

    @Test
    public void testResultAlmostTruncation() {
        freeMarkerService.setOutputLengthLimit(5);
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateFreeMarkerTemplate(
                TRUNCATION_TEST_TEMPLATE, Collections.<String,String>emptyMap());
        assertThat(serviceResponse.isSucceed(), is(true));
        assertThat(serviceResponse.isResultTruncated(), is(false));
        assertThat(serviceResponse.getResult(), equalTo(TRUNCATION_TEST_TEMPLATE));
    }

    @Test
    public void testResultTruncation() {
        freeMarkerService.setOutputLengthLimit(4);
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateFreeMarkerTemplate(
                TRUNCATION_TEST_TEMPLATE, Collections.<String,String>emptyMap());
        assertThat(serviceResponse.isSucceed(), is(true));
        assertThat(serviceResponse.isResultTruncated(), is(true));
        assertThat(serviceResponse.getResult().substring(0, freeMarkerService.getOutputLengthLimit()),
                equalTo(TRUNCATION_TEST_TEMPLATE.substring(0, freeMarkerService.getOutputLengthLimit())));
        assertThat(serviceResponse.getResult().charAt(freeMarkerService.getOutputLengthLimit()),
                not(equalTo(TRUNCATION_TEST_TEMPLATE.charAt(freeMarkerService.getOutputLengthLimit()))));
    }
    
}
