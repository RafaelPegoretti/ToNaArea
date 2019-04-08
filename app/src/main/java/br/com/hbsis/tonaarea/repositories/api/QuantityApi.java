package br.com.hbsis.tonaarea.repositories.api;

import com.google.gson.annotations.SerializedName;

public class QuantityApi {

    @SerializedName("pendente")
    public int pendentes;

    @SerializedName("aprovada")
    public int aprovada;

    @SerializedName("reprovada")
    public int reprovada;
}
