package com.tokopedia.loyalty.view.view;

import android.content.Context;

import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.data.VoucherViewModel;

import java.util.List;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public interface IPromoCouponView extends IBaseView {
    void renderCouponListDataResult(List<CouponData> couponData);

    void receiveResult(CouponViewModel couponViewModel);

    void couponError();

    Context getContext();
}
