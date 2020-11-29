package com.tokopedia.seller;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.seller.manageitem.di.component.DaggerProductComponent;
import com.tokopedia.seller.manageitem.di.component.ProductComponent;
import com.tokopedia.seller.manageitem.di.module.ProductModule;

/**
 * Created by nakama on 11/12/17.
 */

public class ProductEditItemComponentInstance {
    private static ProductComponent productEditItemComponent;

    public static ProductComponent getComponent(Application application) {
        if (productEditItemComponent == null) {
            productEditItemComponent = DaggerProductComponent.builder()
                    .productModule(new ProductModule())
                    .baseAppComponent(((BaseMainApplication) application).getBaseAppComponent()).build();
        }
        return productEditItemComponent;
    }
}
