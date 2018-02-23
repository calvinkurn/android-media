package com.tokopedia.transaction.checkout.data.repository;

import com.tokopedia.core.manage.people.address.model.AddressModel;
import com.tokopedia.core.manage.people.address.model.GetPeopleAddress;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.transaction.checkout.data.mapper.PeopleAddressMapper;
import com.tokopedia.transaction.checkout.domain.model.ShipmentAddressModel;
import com.tokopedia.transaction.checkout.domain.repository.PeopleAddressRepository;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author Aghny A. Putra on 21/02/18
 */

public class PeopleAddressRepositoryImpl implements PeopleAddressRepository {

    private static final int FIRST_ELEMENT = 0;

    private final PeopleService mPeopleService;
    private final PeopleAddressMapper mPeopleAddressMapper;

    public PeopleAddressRepositoryImpl(PeopleService peopleService, PeopleAddressMapper peopleAddressMapper) {
        mPeopleService = peopleService;
        mPeopleAddressMapper = peopleAddressMapper;
    }

    /**
     * Get an {@link Observable} which will emits a {@link List < ShipmentAddressModel >}
     *
     * @param params Parameters used to retrieve address data
     * @return List of address
     */
    @Override
    public Observable<List<ShipmentAddressModel>> getAddress(Map<String, String> params) {
        return mPeopleService.getApi()
                .getAddress(params)
                .map(new Func1<Response<TkpdResponse>, List<AddressModel>>() {
                    @Override
                    public List<AddressModel> call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                GetPeopleAddress peopleAddress = response.body()
                                        .convertDataObj(GetPeopleAddress.class);

                                if (peopleAddress.getList().isEmpty()) {
                                    // Successfully obtaining user's list of address
                                    return peopleAddress.getList();
                                } else {
                                    // List of address is empty, therefore result will be treated
                                    // as null data
                                }

                            } else if (response.body().isNullData()) {
                                // Response body is literally null data
                            } else {
                                // Error is occurred
                                String error = response.body().getErrorMessages().get(FIRST_ELEMENT);
                            }
                        }

                        return null;
                    }
                })
                .map(new Func1<List<AddressModel>, List<ShipmentAddressModel>>() {
                    @Override
                    public List<ShipmentAddressModel> call(List<AddressModel> addressModels) {
                        return mPeopleAddressMapper.transform(addressModels);
                    }
                });
    }
}
