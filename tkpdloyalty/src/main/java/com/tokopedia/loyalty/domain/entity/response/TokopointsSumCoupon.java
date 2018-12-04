package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.SerializedName;

public class TokopointsSumCoupon {

    @SerializedName("sumCouponStr")
    private String sumCouponStr;

    @SerializedName("sumCoupon")
    private int sumCoupon;

    public String getSumCouponStr() {
        return sumCouponStr;
    }

    public void setSumCouponStr(String sumCouponStr) {
        this.sumCouponStr = sumCouponStr;
    }

    public int getSumCoupon() {
        return sumCoupon;
    }

    public void setSumCoupon(int sumCoupon) {
        this.sumCoupon = sumCoupon;
    }
}
