package com.tokopedia.seller.manageitem.di.component;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.seller.manageitem.di.module.ProductModule;
import com.tokopedia.seller.manageitem.di.scope.ProductScope;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ProductScope
@Component(modules = ProductModule.class, dependencies = BaseAppComponent.class)
public interface ProductComponent {
    Retrofit.Builder retrofitBuilder();

    Gson gson();
}