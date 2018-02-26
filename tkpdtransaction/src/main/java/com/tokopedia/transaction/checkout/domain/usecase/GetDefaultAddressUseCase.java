package com.tokopedia.transaction.checkout.domain.usecase;

import com.tokopedia.transaction.checkout.di.qualifier.NonPaginatedAddressQualifier;
import com.tokopedia.transaction.checkout.domain.repository.PeopleAddressRepository;
import com.tokopedia.transaction.checkout.view.data.RecipientAddressModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * @author Aghny A. Putra on 21/02/18
 */

@NonPaginatedAddressQualifier
public class GetDefaultAddressUseCase extends UseCase<List<RecipientAddressModel>> {

    private final PeopleAddressRepository mPeopleAddressRepository;

    public GetDefaultAddressUseCase(PeopleAddressRepository peopleAddressRepository) {
        mPeopleAddressRepository = peopleAddressRepository;
    }

    @Override
    public Observable<List<RecipientAddressModel>> createObservable(RequestParams requestParams) {
        return mPeopleAddressRepository.getAllAddress(requestParams.getParamsAllValueInString());
    }

}