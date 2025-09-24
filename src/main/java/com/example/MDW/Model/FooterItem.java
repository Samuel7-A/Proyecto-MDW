package com.example.MDW.model;

public class FooterItem {
    private String texto;
    private String url;
    private String icono; // opcional, si es una red social
    private String imagen; // opcional, si es un QR u otra imagen

    public FooterItem(String texto, String url) {
        this.texto = texto;
        this.url = url;
    }

    public FooterItem(String texto, String url, String icono) {
        this.texto = texto;
        this.url = url;
        this.icono = icono;
    }

    public FooterItem(String texto, String imagen, boolean esImagen) {
        this.texto = texto;
        this.imagen = imagen;
    }

    public String getTexto() { return texto; }
    public String getUrl() { return url; }
    public String getIcono() { return icono; }
    public String getImagen() { return imagen; }
}
