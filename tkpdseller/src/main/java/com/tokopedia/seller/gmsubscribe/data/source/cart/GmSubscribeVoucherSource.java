package com.tokopedia.seller.gmsubscribe.data.source.cart;

import com.google.gson.Gson;
import com.tokopedia.seller.gmsubscribe.data.mapper.cart.GmSubscribeVoucherMapper;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.GmSubscribeCartCloud;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.model.voucher.GmVoucherServiceModel;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GmVoucherCheckDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GmSubscribeVoucherSource {
    private final GmSubscribeCartCloud cartCloud;
    private final Gson gson;
    private final GmSubscribeVoucherMapper voucherMapper;

    public GmSubscribeVoucherSource(GmSubscribeCartCloud cartCloud, Gson gson, GmSubscribeVoucherMapper voucherMapper) {
        this.cartCloud = cartCloud;
        this.gson = gson;
        this.voucherMapper = voucherMapper;
    }

    public Observable<GmVoucherCheckDomainModel> checkVoucher(Integer selectedProduct, String voucherCode) {
        return cartCloud.checkVoucher(selectedProduct, voucherCode)
                .flatMap(new ConvertToObject());
    }

    private class ConvertToObject implements Func1<String, Observable<GmVoucherCheckDomainModel>> {

        @Override
        public Observable<GmVoucherCheckDomainModel> call(String s) {
            return Observable.just(gson.fromJson(s, GmVoucherServiceModel.class)).map(voucherMapper);
        }
    }
}
