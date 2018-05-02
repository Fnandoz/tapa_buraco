package lfernando.tapaburaco;

public class Buraco {
    private String id;
    private String descricao;
    private String imagem;
    private int impacto;
    private float lat;
    private float lon;


    public Buraco() {
    }

    public Buraco(String id, String descricao, String imagem, int impacto, float lat, float lon) {
        this.id = id;
        this.descricao = descricao;
        this.imagem = imagem;
        this.impacto = impacto;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public int getImpacto() {
        return impacto;
    }

    public void setImpacto(int impacto) {
        this.impacto = impacto;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }
}
