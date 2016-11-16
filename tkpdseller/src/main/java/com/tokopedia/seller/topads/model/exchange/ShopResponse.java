package com.tokopedia.seller.topads.model.exchange;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.topads.model.data.Ad;

public class ShopResponse {

    @SerializedName("data")
    @Expose
    private Ad mAd;

    /**
     * 
     * @return
     *     The mAd
     */
    public Ad getAd() {
        return mAd;
    }

    /**
     * 
     * @param ad
     *     The mAd
     */
    public void setAd(Ad ad) {
        this.mAd = ad;
    }

}
