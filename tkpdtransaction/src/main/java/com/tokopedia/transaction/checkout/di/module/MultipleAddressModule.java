package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.transaction.checkout.di.scope.MultipleAddressScope;
import com.tokopedia.transaction.checkout.domain.IMultipleAddressInteractor;
import com.tokopedia.transaction.checkout.domain.IMultipleAddressRepository;
import com.tokopedia.transaction.checkout.domain.MultipleAddressInteractor;
import com.tokopedia.transaction.checkout.view.view.multipleaddressform.IMultipleAddressPresenter;
import com.tokopedia.transaction.checkout.view.view.multipleaddressform.MultipleAddressPresenter;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

@Module(includes = {DataModule.class})
public class MultipleAddressModule {

    @MultipleAddressScope
    @Provides
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @MultipleAddressScope
    @Provides
    IMultipleAddressPresenter providePresenter(IMultipleAddressInteractor interactor) {
        return new MultipleAddressPresenter(interactor);
    }

    @MultipleAddressScope
    @Provides
    IMultipleAddressInteractor provideInteractor(IMultipleAddressRepository repository) {
        return new MultipleAddressInteractor(provideCompositeSubscription(), repository);
    }

}
