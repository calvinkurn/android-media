package com.tokopedia.transaction.checkout.domain.usecase;

import com.tokopedia.transaction.checkout.domain.model.ShipmentAddressModel;
import com.tokopedia.transaction.checkout.domain.repository.PeopleAddressRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * @author Aghny A. Putra on 21/02/18
 */

public class GetAddressListUseCase extends UseCase<List<ShipmentAddressModel>> {

    private final PeopleAddressRepository mPeopleAddressRepository;

    public GetAddressListUseCase(PeopleAddressRepository peopleAddressRepository) {
        mPeopleAddressRepository = peopleAddressRepository;
    }

    @Override
    public Observable<List<ShipmentAddressModel>> createObservable(RequestParams requestParams) {
        return mPeopleAddressRepository.getAddress(requestParams.getParamsAllValueInString());
    }

}
