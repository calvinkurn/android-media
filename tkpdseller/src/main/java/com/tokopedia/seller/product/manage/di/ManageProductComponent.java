package com.tokopedia.seller.product.manage.di;

import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.manage.view.fragment.ManageProductFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

@ManageProductScope
@Component(modules = ManageProductModule.class, dependencies = ProductComponent.class)
public interface ManageProductComponent {
    void inject(ManageProductFragment manageProductFragment);
}
