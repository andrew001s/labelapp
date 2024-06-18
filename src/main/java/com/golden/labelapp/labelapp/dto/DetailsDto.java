package com.golden.labelapp.labelapp.dto;

import java.util.Map;

public class DetailsDto {
    private String categoria;
    private Map<String,Integer> subCategoria;
    private Map<String,Integer> logo;

    public DetailsDto(String categoria,Map<String,Integer> subCategoria,Map<String,Integer> logo) {
        this.categoria = categoria;
        this.subCategoria = subCategoria;
        this.logo = logo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Map<String,Integer>getsubCategoria() {
        return subCategoria;
    }

    public void setsubCategoria(Map<String,Integer>  subCategoria) {
        this.subCategoria = subCategoria;
    }

    public Map<String, Integer> getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(Map<String, Integer> subCategoria) {
        this.subCategoria = subCategoria;
    }

    public Map<String, Integer> getLogo() {
        return logo;
    }

    public void setLogo(Map<String, Integer> logo) {
        this.logo = logo;
    }
    
}
