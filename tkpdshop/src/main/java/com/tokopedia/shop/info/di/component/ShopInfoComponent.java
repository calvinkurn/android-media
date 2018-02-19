package com.tokopedia.shop.info.di.component;

import com.tokopedia.shop.address.view.fragment.ShopAddressListFragment;
import com.tokopedia.shop.common.data.source.ShopCommonDataSource;
import com.tokopedia.shop.common.data.source.cloud.ShopCommonCloudDataSource;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository;
import com.tokopedia.shop.info.di.module.ShopInfoModule;
import com.tokopedia.shop.info.di.scope.ShopInfoScope;
import com.tokopedia.shop.info.view.activity.ShopPageActivity;
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment;

import dagger.Component;

/**
 * Created by hendry on 18/01/18.
 */
@ShopInfoScope
@Component(modules = ShopInfoModule.class, dependencies = ShopComponent.class)
public interface ShopInfoComponent {

    void inject(ShopPageActivity shopInfoActivity);

    void inject(ShopInfoFragment shopInfoDetailFragment);

    void inject(ShopAddressListFragment shopAddressListFragment);

}
