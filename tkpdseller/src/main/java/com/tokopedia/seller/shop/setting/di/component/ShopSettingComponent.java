package com.tokopedia.seller.shop.setting.di.component;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingModule;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingScope;
import com.tokopedia.seller.shop.setting.domain.DistrictLogisticDataRepository;
import com.tokopedia.seller.shop.setting.domain.ShopSettingSaveInfoRepository;

import dagger.Component;

/**
 * @author sebastianuskh on 3/17/17.
 */
@ShopSettingScope
@Component(modules = ShopSettingModule.class, dependencies = ShopComponent.class)
public interface ShopSettingComponent {

    DistrictLogisticDataRepository getDistrictLogisticDataRepository();

    ThreadExecutor getThreadExecutor();

    PostExecutionThread getPostExecutionThread();

    ShopSettingSaveInfoRepository getSaveInfoRepository();
}
