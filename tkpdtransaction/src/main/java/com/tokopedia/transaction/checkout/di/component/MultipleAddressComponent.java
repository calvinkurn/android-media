package com.tokopedia.transaction.checkout.di.component;

import com.tokopedia.transaction.checkout.di.module.MultipleAddressModule;
import com.tokopedia.transaction.checkout.di.scope.MultipleAddressScope;
import com.tokopedia.transaction.checkout.view.MultipleAddressFragment;

import dagger.Component;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

@MultipleAddressScope
@Component(modules = MultipleAddressModule.class)
public interface MultipleAddressComponent {
    void inject(MultipleAddressFragment multipleAddressFragment);
}
