package com.tokopedia.gm.subscribe.data.source.cart;

import com.tokopedia.gm.subscribe.data.mapper.cart.GmSubscribeVoucherMapper;
import com.tokopedia.gm.subscribe.data.source.cart.cloud.GmSubscribeCartCloud;
import com.tokopedia.gm.subscribe.data.source.cart.cloud.model.voucher.GmVoucherServiceModel;
import com.tokopedia.gm.subscribe.domain.cart.model.GmVoucherCheckDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GmSubscribeVoucherSource {
    private final GmSubscribeCartCloud cartCloud;
    private final GmSubscribeVoucherMapper voucherMapper;

    public GmSubscribeVoucherSource(GmSubscribeCartCloud cartCloud, GmSubscribeVoucherMapper voucherMapper) {
        this.cartCloud = cartCloud;
        this.voucherMapper = voucherMapper;
    }

    public Observable<GmVoucherCheckDomainModel> checkVoucher(Integer selectedProduct, String voucherCode) {
        return cartCloud.checkVoucher(selectedProduct, voucherCode)
                .flatMap(new ConvertToObject());
    }

    private class ConvertToObject implements Func1<GmVoucherServiceModel, Observable<GmVoucherCheckDomainModel>> {

        @Override
        public Observable<GmVoucherCheckDomainModel> call(GmVoucherServiceModel response) {
            return Observable.just(response).map(voucherMapper);
        }
    }
}
