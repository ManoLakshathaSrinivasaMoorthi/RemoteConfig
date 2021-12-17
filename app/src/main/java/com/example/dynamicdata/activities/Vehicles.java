package com.example.dynamicdata.activities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vehicles {

    @SerializedName("nome")
    @Expose
    private final String nome;
    @SerializedName("codigo")
    @Expose
    private final String codigo;


    public Vehicles(String nome, String codigo) {
        super();
        this.nome = nome;
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }

}
