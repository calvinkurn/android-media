package com.tokopedia.transaction.checkout.view.di.component;

import com.tokopedia.transaction.checkout.view.di.module.MultipleAddressModule;
import com.tokopedia.transaction.checkout.view.di.scope.MultipleAddressScope;
import com.tokopedia.transaction.checkout.view.view.multipleaddressform.MultipleAddressFragment;

import dagger.Component;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

@MultipleAddressScope
@Component(modules = MultipleAddressModule.class)
public interface MultipleAddressComponent {
    void inject(MultipleAddressFragment multipleAddressFragment);
}
