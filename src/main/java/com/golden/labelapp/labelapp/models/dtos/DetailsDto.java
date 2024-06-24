package com.golden.labelapp.labelapp.models.dtos;

import java.util.Map;
/**
 * Clase que representa los detalles de una etiqueta.
 */
public class DetailsDto {
    private String categoria;
    private Map<String,Integer> subCategoria;
    private Map<String,Integer> logo;
    private Integer total;
    /**
     * Constructor de la clase DetailsDto.
     * 
     * @param categoria la categoría de la etiqueta
     * @param subCategoria las subcategorías de la etiqueta
     * @param logo los logos de la etiqueta
     * @param total el total de la etiqueta
     */
    public DetailsDto(String categoria, Map<String,Integer> subCategoria, Map<String,Integer> logo, Integer total) {
        this.categoria = categoria;
        this.subCategoria = subCategoria;
        this.logo = logo;
        this.total = total;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Map<String,Integer> getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(Map<String,Integer> subCategoria) {
        this.subCategoria = subCategoria;
    }

    public Map<String, Integer> getLogo() {
        return logo;
    }

    public void setLogo(Map<String, Integer> logo) {
        this.logo = logo;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
