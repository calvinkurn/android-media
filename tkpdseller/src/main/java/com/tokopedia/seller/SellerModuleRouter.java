package com.tokopedia.seller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.seller.common.cashback.DataCashbackModel;
import com.tokopedia.seller.common.featuredproduct.GMFeaturedProductDomainModel;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.seller.shop.common.domain.interactor.GetShopInfoUseCase;

import java.util.ArrayList;
import java.util.List;

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

    Observable<Boolean> setCashBack(String productId, int cashback);

    Observable<List<DataCashbackModel>> getCashbackList(List<String> productIds);

    GetShopInfoUseCase getShopInfo();

    ShopComponent getShopComponent();

    Intent getInboxReputationIntent(Context context);

    void sendEventTracking(String event, String category, String action, String label);

    Intent getLoginIntent(Context context);

    Intent getPhoneVerificationActivityIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);

    Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId);
}
