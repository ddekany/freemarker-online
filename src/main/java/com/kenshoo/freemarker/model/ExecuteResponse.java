package com.kenshoo.freemarker.model;

import java.util.Map;

/**
 * Created by Pmuruge on 8/29/2015.
 */
public class ExecuteResponse {
    private String result;
    private Map<ExecuteResourceErrorFields, String> problems;
    private boolean truncatedResult;

    public ExecuteResponse(String result, Map<ExecuteResourceErrorFields, String> problems, boolean truncatedResult) {
        this.result = result;
        this.problems = problems;
        this.truncatedResult = truncatedResult;
    }

    public ExecuteResponse() {

    }

    public Map<ExecuteResourceErrorFields, String> getProblems() {
        return problems;
    }

    public void setProblems(Map<ExecuteResourceErrorFields, String> problems) {
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
