package com.tokopedia.loyalty.view.presenter;

import com.google.gson.JsonObject;
import com.tokopedia.loyalty.view.data.CouponData;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public interface IPromoCouponPresenter {

    String VOUCHER_CODE = "voucher_code";

    String CATEGORY_ID = "category_id";

    void processGetCouponList(String platform);

    void processGetEventCouponList(int categoryId, int productId);

    void processPostCouponValidateRedeem();

    void processPostCouponRedeem();

    void processGetPointRecentHistory();

    void processGetCatalogList();

    void processGetCatalogDetail();

    void processGetCatalogFilterCategory();

    void submitVoucher(CouponData couponData);

    void submitDigitalVoucher(CouponData couponData, String categoryId);

    void submitEventVoucher(CouponData couponData, JsonObject requestBody, boolean flag);

    void detachView();

}
