package com.kenshoo.freemarker.services;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/13/14
 * Time: 7:30 AM
 */
public class FreeMarkerServiceResponseBuilderTest {

    private static final String RESULT = "Result";
    
    private final FreeMarkerServiceResponse.Builder builder = new FreeMarkerServiceResponse.Builder();

    @Test
    public void testSuccessResult() {
        FreeMarkerServiceResponse result = builder.buildForSuccess(RESULT, false);
        assertThat(result.getTemplateOutput(), equalTo(RESULT));
        assertThat(result.isTemplateOutputTruncated(), is(false));
        assertThat(result.isSuccesful(), is(true));
        assertThat(result.getFailureReason(), nullValue());
    }
        
    @Test
    public void testSuccessTruncatedResult() {
        FreeMarkerServiceResponse result = builder.buildForSuccess(RESULT, true);
        assertThat(result.getTemplateOutput(), equalTo(RESULT));
        assertThat(result.isTemplateOutputTruncated(), is(true));
        assertThat(result.isSuccesful(), is(true));
        assertThat(result.getFailureReason(), nullValue());
    }

    @Test
    public void testErrorResult() {
        Throwable failureReason = new Exception();
        FreeMarkerServiceResponse result = builder.buildForFailure(failureReason);
        assertThat(result.getTemplateOutput(), nullValue());
        assertThat(result.isTemplateOutputTruncated(), is(false));
        assertThat(result.isSuccesful(), is(false));
        assertThat(result.getFailureReason(), sameInstance(failureReason));
    }
    
}
