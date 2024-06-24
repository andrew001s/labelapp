package com.golden.labelapp.labelapp.models.dtos;

public class DatasetRequestDto {
    private String name;
    private String url;
    private StringBuilder[] _box;
    /**
     * Constructor de la clase DatasetRequestDto.
     * 
     * @param name el nombre de la imagen asociada
     * @param url la URL del dataset de la imagen asociada
     * @param content el contenido del dataset de la imagen asociada{bbox}
     */
    public DatasetRequestDto(String name, String url, StringBuilder[] content) {
        this.name = name;
        this.url = url;
        this._box = content;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public StringBuilder[] get_box() {
        return _box;
    }
    public void set_box(StringBuilder[] content) {
        this._box = content;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String ruta) {
        this.url = ruta;
    }
    
}
