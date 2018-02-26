package com.tokopedia.transaction.checkout.domain.repository;

import com.tokopedia.transaction.checkout.view.data.RecipientAddressModel;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author Aghny A. Putra on 21/02/18
 */

public interface PeopleAddressRepository {

    /**
     * Get an {@link Observable} which will emits a {@link List<RecipientAddressModel>}
     *
     * @param params Parameters used to retrieve address data
     * @return List of all address
     */
    Observable<List<RecipientAddressModel>> getAllAddress(final Map<String, String> params);

}
