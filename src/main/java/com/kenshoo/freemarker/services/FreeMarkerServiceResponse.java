package com.kenshoo.freemarker.services;

/**
 * Created with IntelliJ IDEA.
 * User: nir
 * Date: 4/12/14
 * Time: 11:28 AM
 */
public class FreeMarkerServiceResponse {
    private String result;
    private boolean succeed;
    private boolean resultTruncated;
    private String errorReason;

    private FreeMarkerServiceResponse(String result, boolean succeed, boolean resultTruncated, String errorReason) {
        this.result = result;
        this.succeed = succeed;
        this.resultTruncated = resultTruncated;
        this.errorReason = errorReason;
    }

    public String getResult() {
        return result;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public boolean isResultTruncated() {
        return resultTruncated;
    }

    public void setResultTruncated(boolean resultTruncated) {
        this.resultTruncated = resultTruncated;
    }

    public static class Builder {
        
        public FreeMarkerServiceResponse successfulResponse(String result, boolean resultTruncated){
            return new FreeMarkerServiceResponse(result, true, resultTruncated, "");
        }

        public FreeMarkerServiceResponse errorResponse(String errorReason){
            return new FreeMarkerServiceResponse("", false, false, errorReason);
        }
        
    }
}
