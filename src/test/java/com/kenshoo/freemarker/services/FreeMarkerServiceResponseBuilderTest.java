package com.kenshoo.freemarker.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/13/14
 * Time: 7:30 AM
 */
public class FreeMarkerServiceResponseBuilderTest {

    public FreeMarkerServiceResponse.Builder freeMarkerServiceResponseBuilder = new FreeMarkerServiceResponse.Builder();

    @Test
    public void testSuccessResult() {
        String resultText = "Result";
        FreeMarkerServiceResponse result = freeMarkerServiceResponseBuilder.successfulResponse(resultText, false);
        assertThat(result.getResult(), equalTo(resultText));
        assertThat(result.isResultTruncated(), is(false));
        assertThat(result.isSucceed(), is(true));
    }
        
    @Test
    public void testSuccessTruncatedResult() {
        String resultText = "Result";
        FreeMarkerServiceResponse result = freeMarkerServiceResponseBuilder.successfulResponse(resultText, true);
        assertThat(result.getResult(), equalTo(resultText));
        assertThat(result.isResultTruncated(), is(true));
        assertThat(result.isSucceed(), is(true));
    }

    @Test
    public void testErrorResult() {
        String error = "Error";
        FreeMarkerServiceResponse result = freeMarkerServiceResponseBuilder.errorResponse(error);
        assertThat(result.getErrorReason(), equalTo(error));
        assertThat(result.getResult(), equalTo(""));
        assertThat(result.isResultTruncated(), is(false));
        assertThat(result.isSucceed(), is(false));
    }
    
}
