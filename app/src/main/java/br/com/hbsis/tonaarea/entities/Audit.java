package br.com.hbsis.tonaarea.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import br.com.hbsis.tonaarea.util.Constants;

public class Audit implements Serializable {


    private String id;
    private String status;
    private String nameRevenda;
    private String name;
    private String product;
    private String instant;

    @Expose
    @SerializedName("valorTtvAuditado")
    private String TTVConcorrente;

    @Expose
    @SerializedName("valorTtvRevenda")
    private String TTVRevenda;

    @Expose
    @SerializedName("valorTtcAuditado")
    private String TTCConcorrente;

    @Expose
    @SerializedName("valorTtcRevenda")
    private String TTCRevenda;

    @Expose
    @SerializedName("descricao")
    private String description;

    @Expose
    @SerializedName("coordenadas")
    private String coodinates ;

    private String nameAuditor;
    private String nameUserWorkFlow;
    private String dateWorkFlow;

    @Expose
    @SerializedName("revendaId")
    private String revendaId;

    @Expose
    @SerializedName("produtoId")
    private String productId;

    @Expose
    @SerializedName("usuarioAuditorId")
    private String AuditorUserId;

    @Expose
    @SerializedName("clienteId")
    private String clientId;

    @Expose
    @SerializedName("imagem")
    private Imagem imagem = new Imagem();


    public Audit(){}

    public Audit(String id, String status, String nameRevenda, String name, String product, String instant, String TTVConcorrente, String TTVRevenda, String TTCConcorrente, String TTCRevenda, String description, String coodinates, String nameAuditor, String nameUserWorkFlow, String dateWorkFlow, Imagem imagem) {
        this.id = id;
        this.status = status;
        this.nameRevenda = nameRevenda;
        this.name = name;
        this.product = product;
        this.instant = instant;
        this.TTVConcorrente = TTVConcorrente;
        this.TTVRevenda = TTVRevenda;
        this.TTCConcorrente = TTCConcorrente;
        this.TTCRevenda = TTCRevenda;
        this.description = description;
        this.coodinates = coodinates;
        this.nameAuditor = nameAuditor;
        this.nameUserWorkFlow = nameUserWorkFlow;
        this.dateWorkFlow = dateWorkFlow;
        this.imagem = imagem;
    }

    public Audit(String status, String name, String product, String instant, String TTVConcorrente, String TTVRevenda, String TTCConcorrente, String TTCRevenda, String imageIrregularPrice, String imageLotNumber) {
        this.status = status;
        this.name = name;
        this.product = product;
        this.instant = instant;
        this.TTVConcorrente = TTVConcorrente;
        this.TTVRevenda = TTVRevenda;
        this.TTCConcorrente = TTCConcorrente;
        this.TTCRevenda = TTCRevenda;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getInstant() {
        return instant;
    }

    public void setInstant(String instant) {
        this.instant = instant;
    }

    public String getTTVConcorrente() {
        return TTVConcorrente;
    }

    public void setTTVConcorrente(String TTVConcorrente) {
        this.TTVConcorrente = TTVConcorrente;
    }

    public String getTTVRevenda() {
        return TTVRevenda;
    }

    public void setTTVRevenda(String TTVRevenda) {
        this.TTVRevenda = TTVRevenda;
    }

    public String getTTCConcorrente() {
        return TTCConcorrente;
    }

    public void setTTCConcorrente(String TTCConcorrente) {
        this.TTCConcorrente = TTCConcorrente;
    }

    public String getTTCRevenda() {
        return TTCRevenda;
    }

    public void setTTCRevenda(String TTCRevenda) {
        this.TTCRevenda = TTCRevenda;
    }

    public String getNameRevenda() {
        return nameRevenda;
    }

    public void setNameRevenda(String nameRevenda) {
        this.nameRevenda = nameRevenda;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCoodinates() {
        return coodinates;
    }

    public void setCoodinates(String coodinates) {
        this.coodinates = coodinates;
    }

    public String getNameAuditor() {
        return nameAuditor;
    }

    public void setNameAuditor(String nameAuditor) {
        this.nameAuditor = nameAuditor;
    }

    public String getNameUserWorkFlow() {
        return nameUserWorkFlow;
    }

    public void setNameUserWorkFlow(String nameUserWorkFlow) {
        this.nameUserWorkFlow = nameUserWorkFlow;
    }

    public String getDateWorkFlow() {
        return dateWorkFlow;
    }

    public void setDateWorkFlow(String dateWorkFlow) {
        this.dateWorkFlow = dateWorkFlow;
    }

    public String getRevendaId() {
        return revendaId;
    }

    public void setRevendaId(String revendaId) {
        this.revendaId = revendaId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAuditorUserId() {
        return AuditorUserId;
    }

    public void setAuditorUserId(String auditorUserId) {
        AuditorUserId = auditorUserId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Imagem getImagem() {
        return imagem;
    }

    public void setImagem(Imagem imagem) {
        this.imagem = imagem;
    }
}
