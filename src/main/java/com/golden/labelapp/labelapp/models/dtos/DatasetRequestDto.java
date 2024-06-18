package com.golden.labelapp.labelapp.models.dtos;

public class DatasetRequestDto {
    private String name;
    private StringBuilder content;
    public DatasetRequestDto(String name, StringBuilder content) {
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
