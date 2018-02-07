package com.tokopedia.loyalty.view.interactor;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.view.data.VoucherViewModel;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public interface IPromoCodeInteractor {

    void submitVoucher(
            String voucherCode,
            TKPDMapParam<String, String> param, Subscriber<VoucherViewModel> subscriber);

    void submitDigitalVoucher(
            String voucherCode,
            TKPDMapParam<String, String> param, Subscriber<VoucherViewModel> subscriber);

}
