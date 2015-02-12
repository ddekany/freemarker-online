package com.kenshoo.freemarker.services;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/12/14
 * Time: 11:28 AM
 */
public class FreeMarkerServiceResponse {
    
    private final String templateOutput;
    private final boolean templateOutputTruncated;
    private final Throwable failureReason;

    FreeMarkerServiceResponse(String templateOutput, boolean templateOutputTruncated) {
        this.templateOutput = templateOutput;
        this.templateOutputTruncated = templateOutputTruncated;
        this.failureReason = null;
    }

    FreeMarkerServiceResponse(Throwable failureReason) {
        this.templateOutput = null;
        this.templateOutputTruncated = false;
        this.failureReason = failureReason;
    }
    
    public String getTemplateOutput() {
        return templateOutput;
    }

    public boolean isTemplateOutputTruncated() {
        return templateOutputTruncated;
    }

    public boolean isSuccesful() {
        return failureReason == null;
    }

    public Throwable getFailureReason() {
        return failureReason;
    }

    public static class Builder {
        
        public FreeMarkerServiceResponse buildForSuccess(String result, boolean resultTruncated){
            return new FreeMarkerServiceResponse(result, resultTruncated);
        }

        public FreeMarkerServiceResponse buildForFailure(Throwable failureReason){
            return new FreeMarkerServiceResponse(failureReason);
        }
        
    }
    
}
