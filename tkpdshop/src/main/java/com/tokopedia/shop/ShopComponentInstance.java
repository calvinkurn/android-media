package com.tokopedia.shop;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent;
import com.tokopedia.shop.info.di.component.ShopInfoComponent;

/**
 * Created by nakama on 11/12/17.
 */

public class ShopComponentInstance {
    private static ShopInfoComponent shopInfoComponent;

    public static ShopInfoComponent getComponent(Application application) {
        if (shopInfoComponent == null) {
            shopInfoComponent = DaggerShopInfoComponent.builder().baseAppComponent(
                    ((BaseMainApplication)application).getBaseAppComponent()).build();
        }
        return shopInfoComponent;
    }
}
