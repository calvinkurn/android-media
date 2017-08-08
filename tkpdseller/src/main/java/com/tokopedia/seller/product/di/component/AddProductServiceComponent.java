package com.tokopedia.seller.product.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.product.di.module.AddProductserviceModule;
import com.tokopedia.seller.product.di.scope.AddProductServiceScope;
import com.tokopedia.seller.product.edit.view.service.UploadProductService;

import dagger.Component;

/**
 * @author sebastianuskh on 4/20/17.
 */
@AddProductServiceScope
@Component(modules = AddProductserviceModule.class, dependencies = AppComponent.class)
public interface AddProductServiceComponent {
    void inject(UploadProductService uploadProductService);
}
