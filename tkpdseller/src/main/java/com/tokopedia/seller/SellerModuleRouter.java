package com.tokopedia.seller;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.seller.common.featuredproduct.GMFeaturedProductDomainModel;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.seller.product.common.di.component.ProductComponent;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by normansyahputa on 12/14/16.
 */

public interface SellerModuleRouter {

    ProductComponent getProductComponent();

    void goToHome(Context context);
    void goToProductDetail(Context context, String productUrl);
    Observable<GMFeaturedProductDomainModel> getFeaturedProduct();
    void goMultipleInstagramAddProduct(Context context, ArrayList<InstagramMediaModel> instagramMediaModelList);
    void goToGMSubscribe(Activity activity);
}
