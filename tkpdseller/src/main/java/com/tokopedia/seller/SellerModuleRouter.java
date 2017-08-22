package com.tokopedia.seller;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.base.di.module.ActivityModule;
import com.tokopedia.seller.common.datepicker.view.model.PeriodRangeModel;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;

import java.util.List;

/**
 * Created by normansyahputa on 12/14/16.
 */

public interface SellerModuleRouter {

    GoldMerchantComponent getGoldMerchantComponent(ActivityModule activityModule);

    void goToHome(Context context);
    void goToProductDetail(Context context, String productUrl);
    void goToDatePicker(Activity activity, List<PeriodRangeModel> periodRangeModels);
}
