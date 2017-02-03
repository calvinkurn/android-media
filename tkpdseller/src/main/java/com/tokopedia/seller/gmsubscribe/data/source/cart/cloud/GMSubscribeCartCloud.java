package com.tokopedia.seller.gmsubscribe.data.source.cart.cloud;

import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.api.GMSubscribeCartApi;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.inputmodel.checkout.GMCheckoutInputModel;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.inputmodel.voucher.VoucherCodeInputModel;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMCheckoutDomainModel;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GMSubscribeCartCloud {
    private final Retrofit retrofit;

    public GMSubscribeCartCloud(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public Observable<String> checkVoucher(Integer selectedProduct, String voucherCode) {
        return getApi().checkVoucher(VoucherCodeInputModel.buildInputModel(voucherCode, selectedProduct));
    }

    public GMSubscribeCartApi getApi() {
        return retrofit.create(GMSubscribeCartApi.class);
    }

    public Observable<String> checkoutGMSubscribe(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode) {
        return getApi().checkoutGMSubscribe(GMCheckoutInputModel.getBodyModel(selectedProduct,autoExtendSelectedProduct, voucherCode));
    }
}
