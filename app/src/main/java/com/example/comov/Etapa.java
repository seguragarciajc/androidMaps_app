package com.example.comov;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

public class Etapa {

    private List<Integer> listaLevel = new LinkedList<Integer>();
    private List<Integer> listaDbm = new LinkedList<Integer>();
    private List<Integer> listaAsu = new LinkedList<Integer>();
    @SerializedName("marcas")
    @Expose
    private List<Marca> marcas;
    @SerializedName("Level maximo")
    @Expose
    private int maximoLevel;
    @SerializedName("DBM maximo")
    @Expose
    private int maximoDbm;
    @SerializedName("ASU maximo")
    @Expose
    private int maximoAsu;
    @SerializedName("Media Level")
    @Expose
    private float mediaLevel;
    @SerializedName("Media DBM")
    @Expose
    private float mediaDbm;
    @SerializedName("Media ASU")
    @Expose
    private float mediaAsu;

    private static Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

    /**
     * No args constructor for use in serialization
     *
     */
    public Etapa(){
        marcas = new LinkedList<>();

    }

    public void addLevel(int numero){
        listaLevel.add(numero);
    }

    public void addDbm(int numero){
        listaDbm.add(numero);
    }

    public void addAsu(int numero){
        listaAsu.add(numero);
    }

    public void calcularMediasYMax(){
        int level = 0;
        int dbm = 0;
        int asu = 0;
        int levelMax = 0;
        int dbmMax = -999999;
        int asuMax = 0;
        for(int i = 0; i < marcas.size(); i++){
            level += listaLevel.get(i);
            dbm += listaDbm.get(i);
            asu += listaAsu.get(i);
            if (listaLevel.get(i) > levelMax) levelMax = listaLevel.get(i);
            if (listaDbm.get(i) > dbmMax) dbmMax = listaDbm.get(i);
            if (listaAsu.get(i) > asuMax) asuMax = listaAsu.get(i);

        }
        System.out.println(listaLevel.get(0));
        mediaLevel = level / listaLevel.size();
        mediaDbm = dbm / listaDbm.size();
        mediaAsu = asu / listaAsu.size();
        maximoLevel = levelMax;
        maximoDbm = dbmMax;
        maximoAsu = asuMax;
    }


    public Etapa(List<Marca> marcas){
        this.marcas =  new LinkedList<>(marcas);
    }

    public void addMarca(Marca marca){
        marcas.add(marca);
    }

    public List<Marca> getMarcas() {
        return new LinkedList<Marca>(marcas);
    }


    public JsonElement toJson(){
        return gson.toJsonTree(this);
    }
}
