package com.tokopedia.seller.gmsubscribe.data.source.cart.cloud;

import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.api.GmSubscribeCartApi;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.inputmodel.checkout.GmCheckoutInputModel;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.inputmodel.voucher.VoucherCodeInputModel;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GmSubscribeCartCloud {
    private final Retrofit retrofit;

    public GmSubscribeCartCloud(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public Observable<String> checkVoucher(Integer selectedProduct, String voucherCode) {
        return getApi().checkVoucher(VoucherCodeInputModel.buildInputModel(voucherCode, selectedProduct));
    }

    public GmSubscribeCartApi getApi() {
        return retrofit.create(GmSubscribeCartApi.class);
    }

    public Observable<String> checkoutGMSubscribe(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode) {
        return getApi().checkoutGMSubscribe(GmCheckoutInputModel.getBodyModel(selectedProduct, autoExtendSelectedProduct, voucherCode));
    }
}
