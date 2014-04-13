package com.kenshoo.freemarker.services;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/13/14
 * Time: 7:30 AM
 */
public class FreeMarkerServiceResponseBuilderTest {

    public FreeMarkerServiceResponseBuilder freeMarkerServiceResponseBuilder = new FreeMarkerServiceResponseBuilder();

    @Test
    public void testSuccessResult() {
        String resultText = "Result";
        FreeMarkerServiceResponse result = freeMarkerServiceResponseBuilder.successfulResponse(resultText);
        assertThat(result.getResult(), equalTo(resultText));
        assertThat(result.isSucceed(), is(true));
    }

    @Test
    public void testErrorResult() {
        String error = "Error";
        FreeMarkerServiceResponse result = freeMarkerServiceResponseBuilder.errorResponse(error);
        assertThat(result.getErrorReason(), equalTo(error));
        assertThat(result.isSucceed(), is(false));
    }
}
