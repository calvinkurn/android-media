package com.tokopedia.transaction.checkout.domain.usecase;

import com.tokopedia.transaction.checkout.data.repository.PeopleAddressRepository;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.view.di.qualifier.PaginatedAddressQualifier;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * @author Aghny A. Putra on 21/02/18
 */

@PaginatedAddressQualifier
public class GetAllAddressUseCase extends UseCase<List<RecipientAddressModel>> {

    private final PeopleAddressRepository mPeopleAddressRepository;

    public GetAllAddressUseCase(PeopleAddressRepository peopleAddressRepository) {
        mPeopleAddressRepository = peopleAddressRepository;
    }

    @Override
    public Observable<List<RecipientAddressModel>> createObservable(RequestParams requestParams) {
        return mPeopleAddressRepository.getAllAddress(requestParams.getParamsAllValueInString());
    }

}
