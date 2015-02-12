package com.kenshoo.freemarker.services;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import freemarker.core.ParseException;
import freemarker.template.TemplateException;

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
    public void testCalculationOfATemplateWithNoDataModel() {
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateTemplateOutput(
                "test", Collections.<String, Object>emptyMap());
        assertThat(serviceResponse.isSuccesful(), is(true));
        assertThat(serviceResponse.getTemplateOutput(), is("test"));
    }

    @Test
    public void testSimpleTemplate() {
        HashMap<String, Object> dataModel = new HashMap<>();
        dataModel.put("var1", "val1");
        String templateSourceCode = "${var1}";
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateTemplateOutput(
                templateSourceCode, dataModel);
        assertThat(serviceResponse.getTemplateOutput(), equalTo("val1"));
    }

    @Test
    public void testTemplateWithFewArgsAndOperators() {
        HashMap<String, Object> dataModel = new HashMap<>();
        dataModel.put("var1", "val1");
        dataModel.put("var2", "val2");
        String template = "${var1?capitalize} ${var2?cap_first}";
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateTemplateOutput(template, dataModel);
        assertThat(serviceResponse.getTemplateOutput(), equalTo("Val1 Val2"));
    }

    @Test
    public void testTemplateWithSyntaxError() {
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateTemplateOutput(
                "test ${xx", Collections.<String, Object>emptyMap());
        assertThat(serviceResponse.isSuccesful(), is(false));
        assertThat(serviceResponse.getFailureReason(), instanceOf(ParseException.class));
    }

    @Test
    public void testTemplateWithEvaluationError() {
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateTemplateOutput(
                "test ${x}", Collections.<String, Object>emptyMap());
        assertThat(serviceResponse.isSuccesful(), is(false));
        assertThat(serviceResponse.getFailureReason(), instanceOf(TemplateException.class));
    }

    @Test
    public void testResultAlmostTruncation() {
        freeMarkerService.setOutputLengthLimit(5);
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateTemplateOutput(
                TRUNCATION_TEST_TEMPLATE, Collections.<String, Object>emptyMap());
        assertThat(serviceResponse.isSuccesful(), is(true));
        assertThat(serviceResponse.isTemplateOutputTruncated(), is(false));
        assertThat(serviceResponse.getTemplateOutput(), equalTo(TRUNCATION_TEST_TEMPLATE));
    }

    @Test
    public void testResultTruncation() {
        freeMarkerService.setOutputLengthLimit(4);
        FreeMarkerServiceResponse serviceResponse = freeMarkerService.calculateTemplateOutput(
                TRUNCATION_TEST_TEMPLATE, Collections.<String, Object>emptyMap());
        assertThat(serviceResponse.isSuccesful(), is(true));
        assertThat(serviceResponse.isTemplateOutputTruncated(), is(true));
        assertThat(serviceResponse.getTemplateOutput(),
                startsWith(TRUNCATION_TEST_TEMPLATE.substring(0, freeMarkerService.getOutputLengthLimit())));
        assertThat(serviceResponse.getTemplateOutput().charAt(freeMarkerService.getOutputLengthLimit()),
                not(equalTo(TRUNCATION_TEST_TEMPLATE.charAt(freeMarkerService.getOutputLengthLimit()))));
    }
    
}
