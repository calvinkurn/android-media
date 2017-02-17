package com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.inputmodel.checkout.GmCheckoutInputModel;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.inputmodel.voucher.VoucherCodeInputModel;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public interface GmSubscribeCartApi {
    @POST(TkpdBaseURL.TkpdCart.CHECK_VOUCHER)
    Observable<String> checkVoucher(@Body VoucherCodeInputModel inputModel);

    @POST(TkpdBaseURL.TkpdCart.CHECKOUT_ORDER)
    Observable<String> checkoutGMSubscribe(@Body GmCheckoutInputModel bodyModel);
}
