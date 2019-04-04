package com.tokopedia.seller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.seller.common.featuredproduct.GMFeaturedProductDomainModel;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;

import java.util.Map;

import rx.Observable;

/**
 * Created by normansyahputa on 12/14/16.
 */

public interface SellerModuleRouter {

    ProductComponent getProductComponent();

    void goToGMSubscribe(Activity activity);

    GetShopInfoUseCase getShopInfo();

    ShopComponent getShopComponent();

    Intent getInboxReputationIntent(Context context);

    Intent getLoginIntent(Context context);

    Intent getPhoneVerificationActivityIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);

    void startSaldoDepositIntent(Context context);

    Intent getTopProfileIntent(Context context, String userId);

    Intent getGMHomeIntent(Context context);

    Intent getInboxTalkCallingIntent(Context context);

}

