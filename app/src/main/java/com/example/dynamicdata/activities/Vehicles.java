package com.example.dynamicdata.activities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vehicles {

    @SerializedName("nome")
    @Expose
    private String nome;
    @SerializedName("codigo")
    @Expose
    private String codigo;




    public Vehicles(String nome, String codigo) {
        super();
        this.nome = nome;
        this.codigo = codigo;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
