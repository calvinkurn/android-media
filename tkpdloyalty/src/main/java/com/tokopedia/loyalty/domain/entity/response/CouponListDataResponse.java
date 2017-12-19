package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class CouponListDataResponse {
    @SerializedName("coupons")
    @Expose
    private List<Coupon> coupons = new ArrayList<>();

    public List<Coupon> getCoupons() {
        return coupons;
    }
}
