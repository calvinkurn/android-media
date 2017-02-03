package com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.api;

import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.inputmodel.checkout.GMCheckoutInputModel;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.inputmodel.voucher.VoucherCodeInputModel;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public interface GMSubscribeCartApi {
    @POST("/v1/voucher/verify")
    Observable<String> checkVoucher(@Body VoucherCodeInputModel inputModel);

    @POST("v1/cart/order")
    Observable<String> checkoutGMSubscribe(@Body GMCheckoutInputModel bodyModel);
}
