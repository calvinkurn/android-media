package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.transaction.checkout.di.qualifier.NonPaginatedAddressQualifier;
import com.tokopedia.transaction.checkout.di.scope.CartAddressChoiceScope;
import com.tokopedia.transaction.checkout.di.scope.ShipmentAddressListScope;
import com.tokopedia.transaction.checkout.domain.repository.PeopleAddressRepository;
import com.tokopedia.transaction.checkout.domain.usecase.GetAllAddressUseCase;
import com.tokopedia.transaction.checkout.domain.usecase.GetDefaultAddressUseCase;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.transaction.checkout.view.presenter.ShipmentAddressListPresenter;
import com.tokopedia.transaction.checkout.view.view.addressoptions.CartAddressChoiceFragment;
import com.tokopedia.transaction.checkout.view.view.addressoptions.CartAddressChoicePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */

@Module(includes = {PeopleAddressModule.class})
public class CartAddressChoiceModule {

    private final ShipmentAddressListAdapter.ActionListener actionListener;


    public CartAddressChoiceModule(CartAddressChoiceFragment cartAddressChoiceFragment) {
        actionListener = cartAddressChoiceFragment;
    }

    @Provides
    @CartAddressChoiceScope
    CartAddressChoicePresenter provideCartAddressChoicePresenter(GetDefaultAddressUseCase getDefaultAddressUseCase) {
        return new CartAddressChoicePresenter(getDefaultAddressUseCase);
    }

    @Provides
    @CartAddressChoiceScope
    ShipmentAddressListAdapter provideCartAddressListAdapter() {
        return new ShipmentAddressListAdapter(actionListener);
    }

    @Provides
    @CartAddressChoiceScope
    GetDefaultAddressUseCase provideGetAddressListUseCase(@NonPaginatedAddressQualifier PeopleAddressRepository peopleAddressRepository) {
        return new GetDefaultAddressUseCase(peopleAddressRepository);
    }

}