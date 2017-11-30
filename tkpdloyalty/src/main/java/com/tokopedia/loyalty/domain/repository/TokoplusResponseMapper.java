package com.tokopedia.loyalty.domain.repository;

import com.tokopedia.loyalty.domain.entity.response.Coupon;
import com.tokopedia.loyalty.domain.entity.response.CouponListDataResponse;
import com.tokopedia.loyalty.domain.entity.response.TokoplusResponse;
import com.tokopedia.loyalty.view.data.CouponData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class TokoplusResponseMapper implements ITokoplusResponseMapper {

    @Inject
    public TokoplusResponseMapper() {
    }

    @Override
    public String sampleMapper(TokoplusResponse tokoplusResponse) {
        return tokoplusResponse.getStrResponse();
    }

    @Override
    public List<CouponData> convertCouponListData(CouponListDataResponse couponListDataResponse) {
        List<CouponData> couponDataList = new ArrayList<>();
        for (Coupon coupon : couponListDataResponse.getCoupons()) {
            couponDataList.add(
                    new CouponData.Builder()
                            .id(coupon.getId())
                            .promoId(coupon.getPromoId())
                            .code(coupon.getCode())
                            .title(coupon.getTitle())
                            .subTitle(coupon.getSubTitle())
                            .description(coupon.getDescription())
                            .expired(coupon.getExpired())
                            .imageUrl(coupon.getImageUrl())
                            .imageUrlMobile(coupon.getImageUrlMobile())
                            .build()
            );
        }
        return couponDataList;
    }
}
