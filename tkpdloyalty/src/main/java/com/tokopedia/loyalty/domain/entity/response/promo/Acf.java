package com.tokopedia.loyalty.domain.entity.response.promo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class Acf {
    @SerializedName("multiple_promo_code")
    @Expose
    private boolean multiplePromoCode;

    public boolean isMultiplePromoCode() {
        return multiplePromoCode;
    }
}
