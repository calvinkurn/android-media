package com.tokopedia.transaction.checkout.domain.usecase;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.transaction.checkout.data.repository.PeopleAddressRepository;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * @author Aghny A. Putra on 21/02/18
 */

public class GetPeopleAddressUseCase extends UseCase<List<RecipientAddressModel>> {

    private static final String PARAM_ORDER_BY = "order_by";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_QUERY = "query";

    private final PeopleAddressRepository mPeopleAddressRepository;

    public GetPeopleAddressUseCase(PeopleAddressRepository peopleAddressRepository) {
        mPeopleAddressRepository = peopleAddressRepository;
    }

    @Override
    public Observable<List<RecipientAddressModel>> createObservable(RequestParams requestParams) {
        return mPeopleAddressRepository.getAllAddress(requestParams.getParamsAllValueInString());
    }

    /**
     * @param context
     * @param order
     * @param query
     * @param page
     * @return
     */
    public RequestParams getRequestParams(final Context context,
                                          final int order,
                                          final String query,
                                          final int page) {

        // Get people address list from api requires parameter of order, keyword, and page
        final HashMap<String, String> params = new HashMap<String, String>() {{
            put(PARAM_ORDER_BY, String.valueOf(order));
            put(PARAM_QUERY, query);
            put(PARAM_PAGE, String.valueOf(page));
        }};

        // Create network auth params from plain params using auth util generator,
        // which will retrieve another params such as device id, os type and timestamp
        final HashMap<String, Object> authParams = new HashMap<String, Object>() {{
            putAll(AuthUtil.generateParams(context, params));
        }};

        // Create request params which contains the auth params
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(authParams);

        return requestParams;
    }

}