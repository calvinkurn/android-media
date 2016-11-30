package com.tokopedia.transaction.addtocart.model.responseatcform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rpx {

    @SerializedName("indomaret_logo")
    @Expose
    private String indomaretLogo;

    /**
     * @return The indomaretLogo
     */
    public String getIndomaretLogo() {
        return indomaretLogo;
    }

    /**
     * @param indomaretLogo The indomaret_logo
     */
    public void setIndomaretLogo(String indomaretLogo) {
        this.indomaretLogo = indomaretLogo;
    }

}
