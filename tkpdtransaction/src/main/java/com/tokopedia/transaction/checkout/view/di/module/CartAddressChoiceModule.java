package com.tokopedia.transaction.checkout.view.di.module;

import com.tokopedia.transaction.checkout.data.repository.PeopleAddressRepository;
import com.tokopedia.transaction.checkout.domain.usecase.GetPeopleAddressUseCase;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.transaction.checkout.view.di.scope.CartAddressChoiceScope;
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
    CartAddressChoicePresenter provideCartAddressChoicePresenter(GetPeopleAddressUseCase getDefaultAddressUseCase) {
        return new CartAddressChoicePresenter(getDefaultAddressUseCase);
    }

    @Provides
    @CartAddressChoiceScope
    ShipmentAddressListAdapter provideCartAddressListAdapter() {
        return new ShipmentAddressListAdapter(actionListener);
    }

    @Provides
    @CartAddressChoiceScope
    GetPeopleAddressUseCase provideGetAddressListUseCase(PeopleAddressRepository peopleAddressRepository) {
        return new GetPeopleAddressUseCase(peopleAddressRepository);
    }

}