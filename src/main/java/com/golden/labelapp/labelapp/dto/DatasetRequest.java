package com.golden.labelapp.labelapp.dto;

public class DatasetRequest {
    private String name;
    private StringBuilder content;
    public DatasetRequest(String name, StringBuilder content) {
        this.name = name;
        this.content = content;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public StringBuilder getContent() {
        return content;
    }
    public void setContent(StringBuilder content) {
        this.content = content;
    }
    
}
