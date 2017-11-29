package com.tokopedia.loyalty.view.view;

import com.tokopedia.loyalty.view.data.CouponData;

import java.util.List;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public interface IPromoCouponView extends IBaseView {
    void renderCouponListDataResult(List<CouponData> couponData);
}
