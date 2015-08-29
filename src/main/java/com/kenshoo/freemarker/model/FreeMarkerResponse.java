package com.kenshoo.freemarker.model;

/**
 * Created by Pmuruge on 8/29/2015.
 */
public class FreeMarkerResponse {
    private String result;

    public FreeMarkerResponse(String result) {
        this.result = result;
    }

    public String getResult() {

        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
