package com.tokopedia.loyalty.view.view;

import com.tokopedia.loyalty.view.data.VoucherViewModel;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public interface IPromoCodeView extends IBaseView {

    void checkVoucherSuccessfull(VoucherViewModel voucherModel);

    void promoCodeError(String errorMessage);
}
