package com.tokopedia.seller.gmsubscribe.data.source.cart.cloud;

import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.api.GmSubscribeCartApi;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.inputmodel.checkout.GmCheckoutInputModel;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.inputmodel.voucher.VoucherCodeInputModel;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.model.checkout.GmCheckoutServiceModel;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.model.voucher.GmVoucherServiceModel;
import com.tokopedia.seller.gmsubscribe.data.tools.GetResponse;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GmSubscribeCartCloud {
    private final GmSubscribeCartApi api;

    public GmSubscribeCartCloud(GmSubscribeCartApi api) {
        this.api = api;
    }

    public Observable<GmVoucherServiceModel> checkVoucher(Integer selectedProduct, String voucherCode) {
        return api
                .checkVoucher(
                        VoucherCodeInputModel
                                .buildInputModel(
                                        voucherCode,
                                        selectedProduct
                                )
                )
                .map(new GetResponse<GmVoucherServiceModel>());
    }

    public Observable<GmCheckoutServiceModel> checkoutGMSubscribe(
            Integer selectedProduct,
            Integer autoExtendSelectedProduct,
            String voucherCode
    ) {
        return api
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


}
