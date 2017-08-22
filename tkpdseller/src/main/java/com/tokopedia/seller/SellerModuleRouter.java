package com.tokopedia.seller;

import com.tokopedia.core.base.di.module.ActivityModule;
import com.tokopedia.seller.common.datepicker.view.model.PeriodRangeModel;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.product.common.di.component.ProductComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 12/14/16.
 */

public interface SellerModuleRouter extends com.tokopedia.core.router.digitalmodule.sellermodule.SellerModuleRouter {

    GoldMerchantComponent getGoldMerchantComponent();

    ProductComponent getProductComponent();

    void goToHome(Context context);
    void goToProductDetail(Context context, String productUrl);

    void goMultipleInstagramAddProduct(Context context, ArrayList<InstagramMediaModel> instagramMediaModelList);
    void goToDatePicker(Activity activity, List<PeriodRangeModel> periodRangeModels);
    GoldMerchantComponent getGoldMerchantComponent(ActivityModule activityModule);
}
