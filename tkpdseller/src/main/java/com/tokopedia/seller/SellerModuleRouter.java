package com.tokopedia.seller;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.product.common.di.component.ProductComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 12/14/16.
 */

public interface SellerModuleRouter {

    GoldMerchantComponent getGoldMerchantComponent();

    ProductComponent getProductComponent();

    void goToHome(Context context);
    void goToProductDetail(Context context, String productUrl);

    void goMultipleInstagramAddProduct(Context context, ArrayList<InstagramMediaModel> instagramMediaModelList);

    DataDeposit getDataDeposit(String shopId);

    void goToTopAdsDashboard(Activity activity);
}
