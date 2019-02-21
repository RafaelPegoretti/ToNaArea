package br.com.hbsis.tonaarea.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Imagem {
    private String imageId;

    @Expose
    @SerializedName("precoIrregularBase64")
    private String imageIrregularPrice;

    @Expose
    @SerializedName("loteBase64")
    private String imageLotNumber;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageIrregularPrice() {
        return imageIrregularPrice;
    }

    public void setImageIrregularPrice(String imageIrregularPrice) {
        this.imageIrregularPrice = imageIrregularPrice;
    }

    public String getImageLotNumber() {
        return imageLotNumber;
    }

    public void setImageLotNumber(String imageLotNumber) {
        this.imageLotNumber = imageLotNumber;
    }
}
