package com.tokopedia.seller.shopsettings.edit.presenter;

import android.content.Context;

import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.seller.shopsettings.shipping.model.openshopshipping.OpenShopData;

/**
 * Created by Toped18 on 5/19/2016.
 */
public interface ShopCreateView extends BaseView {

    int REQUEST_EDIT_SHIPPING = 100;

    void showProgress(boolean showDialog);

    void setShopAvatar(String imagePath);

    void setShopDomain(String shopDomain);

    void setShopDomainResult(String message, boolean available);

    boolean checkDomainError();

    void setShopName(String shopName);

    void setShopNameResult(String message, boolean available);

    boolean checkNameError();

    void setShopTag(String shopTag);

    String getShopTag();

    boolean checkTagError();

    void setShopDesc(String shopDesc);

    String getShopDesc();

    boolean checkDescError();

    Context getMainContext();

    void showPhoneVerification(boolean needVerify);

    void saveShippingData(OpenShopData shippingData);

    void goToEditShipping(OpenShopData openShopData);

    void startOpenShopEditShippingActivity();

    void sendBroadcastToAppWidget();
}
