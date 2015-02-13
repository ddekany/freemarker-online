package com.kenshoo.freemarker.view;

public enum FreeMarkerOnlineViewResultType {

    BLANK(false),
    TEMPLATE_OUTPUT(false),
    DATA_MODEL_ERROR(true),
    TEMPLATE_ERROR(true);
    
    private final boolean error;

    private FreeMarkerOnlineViewResultType(boolean error) {
        this.error = error;
    }

    public boolean isError() {
        return error;
    }
    
}
