package com.tokopedia.seller.gmsubscribe.data.source.cart.cloud;

import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.api.GmSubscribeCartApi;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.inputmodel.checkout.GmCheckoutInputModel;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.inputmodel.voucher.VoucherCodeInputModel;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.model.checkout.GmCheckoutServiceModel;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.model.voucher.GmVoucherServiceModel;

import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GmSubscribeCartCloud {
    private final Retrofit retrofit;

    public GmSubscribeCartCloud(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public Observable<GmVoucherServiceModel> checkVoucher(Integer selectedProduct, String voucherCode) {
        return getApi()
                .checkVoucher(
                        VoucherCodeInputModel
                                .buildInputModel(
                                        voucherCode,
                                        selectedProduct
                                )
                )
                .map(new GetResponse<GmVoucherServiceModel>());
    }

    public GmSubscribeCartApi getApi() {
        return retrofit.create(GmSubscribeCartApi.class);
    }

    public Observable<GmCheckoutServiceModel> checkoutGMSubscribe(
            Integer selectedProduct,
            Integer autoExtendSelectedProduct,
            String voucherCode
    ) {
        return getApi()
                .checkoutGMSubscribe(
                        GmCheckoutInputModel
                                .getBodyModel(
                                        selectedProduct,
                                        autoExtendSelectedProduct,
                                        voucherCode
                                )
                )
                .map(new GetResponse<GmCheckoutServiceModel>());
    }

    private class GetResponse<T> implements Func1<Response<T>, T> {
        @Override
        public T call(Response<T> response) {
            return response.body();
        }
    }
}
