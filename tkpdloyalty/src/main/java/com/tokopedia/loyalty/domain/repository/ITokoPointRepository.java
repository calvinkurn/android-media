package com.tokopedia.loyalty.domain.repository;


import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.domain.entity.request.RequestBodyCouponRedeem;
import com.tokopedia.loyalty.domain.entity.request.RequestBodyValidateRedeem;
import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.data.VoucherViewModel;

import java.util.List;

import rx.Observable;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public interface ITokoPointRepository {
    Observable<List<CouponData>> getCouponList(TKPDMapParam<String, String> param);

    Observable<String> postCouponValidateRedeem(RequestBodyValidateRedeem requestBodyValidateRedeem);

    Observable<String> postCouponRedeem(RequestBodyCouponRedeem requestBodyCouponRedeem);

    Observable<String> getPointRecentHistory(TKPDMapParam<String, String> param);

    Observable<String> getPointMain(TKPDMapParam<String, String> param);

    Observable<TokoPointDrawerData> getPointDrawer(TKPDMapParam<String, String> param);

    Observable<String> getPointStatus(TKPDMapParam<String, String> param);

    Observable<String> getCatalogList(TKPDMapParam<String, String> param);

    Observable<String> getCatalogDetail(TKPDMapParam<String, String> param);

    Observable<String> getCatalogFilterCategory(TKPDMapParam<String, String> param);

    Observable<VoucherViewModel> checkVoucherValidity(
            TKPDMapParam<String, String> param, String voucherCode
    );

    Observable<CouponViewModel> checkCouponValidity(
            TKPDMapParam<String, String> param, String voucherCode, String couponTitle
    );

    Observable<VoucherViewModel> checkDigitalVoucherValidity(
            TKPDMapParam<String, String> param, String voucherCode
    );

    Observable<CouponViewModel> checkDigitalCouponValidity(
            TKPDMapParam<String, String> param, String voucherCode, String couponTitle
    );

}
