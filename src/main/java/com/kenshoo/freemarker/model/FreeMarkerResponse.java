package com.kenshoo.freemarker.model;

import java.util.Map;

/**
 * Created by Pmuruge on 8/29/2015.
 */
public class FreeMarkerResponse {
    private String result;
    private Map<String, String> problems;
    private boolean truncatedResult;

    public FreeMarkerResponse(String result, Map<String, String> problems, boolean truncatedResult) {
        this.result = result;
        this.problems = problems;
        this.truncatedResult = truncatedResult;
    }

    public FreeMarkerResponse() {

    }

    public Map<String, String> getProblems() {
        return problems;
    }

    public void setProblems(Map<String, String> problems) {
        this.problems = problems;
    }

    public boolean isTruncatedResult() {
        return truncatedResult;
    }

    public void setTruncatedResult(boolean truncatedResult) {
        this.truncatedResult = truncatedResult;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
