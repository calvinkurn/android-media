package com.tokopedia.transaction.checkout.data.entity.response.couponlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class CouponDataResponse {

    @SerializedName("coupons")
    @Expose
    private List<Coupon> coupons = new ArrayList<>();

    public List<Coupon> getCoupons() {
        return coupons;
    }
}
