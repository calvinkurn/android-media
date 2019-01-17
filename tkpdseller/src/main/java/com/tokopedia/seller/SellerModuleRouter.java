package com.tokopedia.seller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.seller.common.cashback.DataCashbackModel;
import com.tokopedia.seller.common.featuredproduct.GMFeaturedProductDomainModel;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.product.manage.item.common.domain.interactor.GetShopInfoUseCase;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by normansyahputa on 12/14/16.
 */

public interface SellerModuleRouter {

    ProductComponent getProductComponent();

    void goToProductDetail(Context context, String productUrl);

    Observable<GMFeaturedProductDomainModel> getFeaturedProduct();

    void goToGMSubscribe(Activity activity);

    Observable<List<DataCashbackModel>> getCashbackList(List<String> productIds);

    GetShopInfoUseCase getShopInfo();

    ShopComponent getShopComponent();

    Intent getInboxReputationIntent(Context context);

    void sendEventTracking(String event, String category, String action, String label);

    void sendEventTracking(Map<String, Object> eventTracking);

    void sendMoEngageOpenShopEventTracking(String screenName);

    Intent getLoginIntent(Context context);

    Intent getPhoneVerificationActivityIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);

    void startSaldoDepositIntent(Context context);

    boolean isSaldoNativeEnabled();

    Intent getTopProfileIntent(Context context, String userId);

    Intent getGMHomeIntent(Context context);

    void gotoTopAdsDashboard(Context context);

    Intent getInboxTalkCallingIntent(Context context);

    Intent getSaldoDepositIntent(Context context);
}

