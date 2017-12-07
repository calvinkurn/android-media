package com.tokopedia.loyalty.view.view;

import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.data.VoucherViewModel;

import java.util.List;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public interface IPromoCodeView extends IBaseView {

    void renderPromoCodeResult(List<CouponData> couponDataList);

    void checkVoucherSuccessfull(VoucherViewModel voucherModel);
}
