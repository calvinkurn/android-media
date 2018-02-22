package com.tokopedia.transaction.checkout.data.repository;

import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
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

    private final PeopleService mPeopleService;

    public PeopleAddressRepositoryImpl(PeopleService peopleService) {
        mPeopleService = peopleService;
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
                .map(new Func1<Response<TkpdResponse>, List<ShipmentAddressModel>>() {
                    @Override
                    public List<ShipmentAddressModel> call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {

                            } else {

                            }
                        }

                        return null;
                    }
                });
    }
}
