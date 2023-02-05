package com.example.comov;

import com.google.gson.JsonElement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Marca {

    @SerializedName("nivel")
    @Expose
    private int nivel;
    @SerializedName("dbm")
    @Expose
    private int dbm;
    @SerializedName("asu")
    @Expose
    private int asu;
    @SerializedName("timingAdvance")
    @Expose
    private int timingAdvance;
    @SerializedName("celda")
    @Expose
    private String celda;

    private static Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    /**
     * No args constructor for use in serialization
     *
     */
    public Marca(){
    }

    /**
     *
     * @param nivel
     * @param dbm
     * @param asu
     */
    public Marca(int nivel, int dbm, int asu, int timingAdvance, String celda){
        super();
        this.nivel = nivel;
        this.dbm = dbm;
        this.asu = asu;
        this.timingAdvance = timingAdvance;
        this.celda = celda;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getDbm() {
        return dbm;
    }

    public void setDbm(int dbm) {
        this.dbm = dbm;
    }

    public int getAsu() {
        return asu;
    }

    public void setAsu(int asu) {
        this.asu = asu;
    }

    public int getTimingAdvance() { return timingAdvance; }

    public void setTimingAdvance(int timingAdvance) {
        this.timingAdvance = timingAdvance;
    }

    public String getCelda() { return celda; }

    public void setCelda(String celda) { this.celda = celda; }

    public JsonElement toJson(){
        return gson.toJsonTree(this);
    }
}
