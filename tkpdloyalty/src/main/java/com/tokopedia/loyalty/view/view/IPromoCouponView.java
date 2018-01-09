package com.tokopedia.loyalty.view.view;

import android.content.Context;

import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.data.CouponViewModel;

import java.util.List;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public interface IPromoCouponView extends IBaseView {
    void renderCouponListDataResult(List<CouponData> couponData);

    void renderErrorGetCouponList(String message);

    void renderErrorHttpGetCouponList(String message);

    void renderErrorNoConnectionGetCouponList(String message);

    void renderErrorTimeoutConnectionGetCouponList(String message);

    void couponDataNoResult();

    void receiveResult(CouponViewModel couponViewModel);

    void receiveDigitalResult(CouponViewModel couponViewModel);

    void onErrorFetchCouponList(String errorMessage);

    void couponError();

    void showSnackbarError(String message);

    Context getContext();

    void disableSwipeRefresh();

    void enableSwipeRefresh();
}
