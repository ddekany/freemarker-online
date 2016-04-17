package com.kenshoo.freemarker.model;

public class SelectionOption implements Comparable<SelectionOption> {
    
    private final String value;
    private final String label;
    
    public String getValue() {
        return value;
    }
    
    public String getLabel() {
        return label;
    }
    
    public SelectionOption(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SelectionOption other = (SelectionOption) obj;
        if (label == null) {
            if (other.label != null) {
                return false;
            }
        } else if (!label.equals(other.label)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(SelectionOption o) {
        int r = label.compareTo(o.label);
        if (r != 0) {
            return r;
        }
        
        return value.compareTo(o.value);
    }

}
