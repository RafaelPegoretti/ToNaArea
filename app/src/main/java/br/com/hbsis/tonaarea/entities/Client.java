package br.com.hbsis.tonaarea.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Client {
    private String clientId;
    @Expose
    @SerializedName("nomeCliente")
    private String clientName;
    @Expose
    @SerializedName("codigoCliente")
    private String clienteCode;
    @Expose
    @SerializedName("ativo")
    private boolean active;
    @Expose
    @SerializedName("revendaId")
    private String revendaId;

    private String RevendaName;
    private String date;


    public Client() {
    }

    public Client(String clientName, String clienteCode, boolean active, String revendaId) {
        this.clientName = clientName;
        this.clienteCode = clienteCode;
        this.active = active;
        this.revendaId = revendaId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClienteCode() {
        return clienteCode;
    }

    public void setClienteCode(String clienteCode) {
        this.clienteCode = clienteCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRevendaId() {
        return revendaId;
    }

    public void setRevendaId(String revendaId) {
        this.revendaId = revendaId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRevendaName() {
        return RevendaName;
    }

    public void setRevendaName(String revendaName) {
        RevendaName = revendaName;
    }
}
