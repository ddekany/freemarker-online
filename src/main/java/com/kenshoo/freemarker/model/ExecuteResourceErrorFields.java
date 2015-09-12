package com.kenshoo.freemarker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by Pmuruge on 8/31/2015.
 */
public enum ExecuteResourceErrorFields {
    DATA_MODEL {
        public String toString() {
            return "dataModel";
        }
    },
    TEMPLATE {
        public String toString() {
            return "template";
        }
    };

    @JsonCreator
    public static ExecuteResourceErrorFields fromEnumString(String val) {
        for(ExecuteResourceErrorFields field : values()) {
            System.out.println("Checking the value for the field  " + field.toString() + " with the value " + val);
            if(field.toString().equals(val)) {
                System.out.println("Returning the value of " + field);
                return field;
            }
        }
        throw new IllegalArgumentException("invalid string value passed: " + val);
    }
}
