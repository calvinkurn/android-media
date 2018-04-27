package com.tokopedia.transaction.addtocart.model.kero;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sachinbansal on 4/6/18.
 */

public class OngkirData {

    @SerializedName("ongkir")
    @Expose
    private Ongkir ongkir;

    public Ongkir getOngkir() {
        return ongkir;
    }

    public void setOngkir(Ongkir ongkir) {
        this.ongkir = ongkir;
    }
}
