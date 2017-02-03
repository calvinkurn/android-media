package com.tokopedia.seller.gmsubscribe.data.source.cart;

import com.google.gson.Gson;
import com.tokopedia.seller.gmsubscribe.data.mapper.cart.GMSubscribeVoucherMapper;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.GMSubscribeCartCloud;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.model.voucher.GMVoucherServiceModel;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMVoucherCheckDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GMSubscribeVoucherSource {
    private final GMSubscribeCartCloud cartCloud;
    private final Gson gson;
    private final GMSubscribeVoucherMapper voucherMapper;

    public GMSubscribeVoucherSource(GMSubscribeCartCloud cartCloud, Gson gson, GMSubscribeVoucherMapper voucherMapper) {
        this.cartCloud = cartCloud;
        this.gson = gson;
        this.voucherMapper = voucherMapper;
    }

    public Observable<GMVoucherCheckDomainModel> checkVoucher(Integer selectedProduct, String voucherCode) {
        return cartCloud.checkVoucher(selectedProduct, voucherCode)
                .flatMap(new ConvertToObject());
    }

    private class ConvertToObject implements Func1<String, Observable<GMVoucherCheckDomainModel>> {

        @Override
        public Observable<GMVoucherCheckDomainModel> call(String s) {
            return Observable.just(gson.fromJson(s, GMVoucherServiceModel.class)).map(voucherMapper);
        }
    }
}
