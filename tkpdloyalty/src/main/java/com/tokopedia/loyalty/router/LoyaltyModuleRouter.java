package com.tokopedia.loyalty.router;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.loyalty.view.data.VoucherViewModel;

import rx.Observable;

public interface LoyaltyModuleRouter {

    Observable<VoucherViewModel> checkFlightVoucher(String voucherCode, String cartId, String isCoupon);

    Observable<TKPDMapParam<String, Object>> verifyEventPromo(RequestParams requestParams);
}
