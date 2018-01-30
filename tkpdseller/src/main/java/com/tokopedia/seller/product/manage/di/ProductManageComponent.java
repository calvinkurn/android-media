package com.tokopedia.seller.product.manage.di;

import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.manage.view.fragment.ProductManageFragment;
import com.tokopedia.seller.product.manage.view.fragment.ProductManageSortFragment;
import com.tokopedia.seller.shop.common.domain.interactor.GetShopInfoUseCase;

import dagger.Component;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

@ProductManageScope
@Component(modules = ProductManageModule.class, dependencies = ProductComponent.class)
public interface ProductManageComponent {
    void inject(ProductManageFragment productManageFragment);

    void inject(ProductManageSortFragment productManageSortFragment);

    GetShopInfoUseCase getShopInfoUseCase();

    SellerModuleRouter getSellerModuleRouter();
}
