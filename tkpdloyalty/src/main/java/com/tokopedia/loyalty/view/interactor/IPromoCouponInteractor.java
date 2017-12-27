package com.tokopedia.loyalty.view.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.domain.entity.request.RequestBodyCouponRedeem;
import com.tokopedia.loyalty.domain.entity.request.RequestBodyValidateRedeem;
import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.data.CouponViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public interface IPromoCouponInteractor {
    void getCouponList(TKPDMapParam<String, String> param, Subscriber<List<CouponData>> subscriber);

    void submitVoucher(String couponTitle,
                       String voucherCode,
                       TKPDMapParam<String, String> param,
                       Subscriber<CouponViewModel> subscriber);

    void submitDigitalVoucher(String couponTitle,
                              String voucherCode,
                              TKPDMapParam<String, String> param,
                              Subscriber<CouponViewModel> subscriber);

    void postCouponValidateRedeem(RequestBodyValidateRedeem requestBodyValidateRedeem,
                                  Subscriber<String> subscriber);

    void postCouponRedeem(RequestBodyCouponRedeem requestBodyCouponRedeem, Subscriber<String> subscriber);

    void getPointRecentHistory(TKPDMapParam<String, String> param, Subscriber<String> subscriber);

    void getPointMain(TKPDMapParam<String, String> param, Subscriber<String> subscriber);

    void getPointDrawer(TKPDMapParam<String, String> param, Subscriber<String> subscriber);

    void getPointStatus(TKPDMapParam<String, String> param, Subscriber<String> subscriber);

    void getCatalogList(TKPDMapParam<String, String> param, Subscriber<String> subscriber);

    void getCatalogDetail(TKPDMapParam<String, String> param, Subscriber<String> subscriber);

    void getCatalogFilterCategory(TKPDMapParam<String, String> param, Subscriber<String> subscriber);

}
