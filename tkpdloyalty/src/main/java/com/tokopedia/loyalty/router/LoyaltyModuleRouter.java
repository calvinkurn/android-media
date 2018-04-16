package com.tokopedia.loyalty.router;

import com.tokopedia.loyalty.view.data.VoucherViewModel;

import rx.Observable;

public interface LoyaltyModuleRouter {

    Observable<VoucherViewModel> checkFlightVoucher(String voucherCode, String cartId, String isCoupon);
}
