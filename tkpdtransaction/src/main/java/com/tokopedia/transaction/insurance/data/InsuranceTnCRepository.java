package com.tokopedia.transaction.insurance.data;

import com.tokopedia.core.manage.general.districtrecommendation.data.DistrictRecommendationEntityMapper;
import com.tokopedia.core.manage.general.districtrecommendation.data.entity.AddressResponseEntity;
import com.tokopedia.core.manage.general.districtrecommendation.data.source.DistrictRecommendationDataStore;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.AddressResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Irfan Khoirul on 11/12/17.
 */

public class InsuranceTnCRepository {
    private final InsuranceTnCDataStore dataStore;

    @Inject
    public InsuranceTnCRepository(InsuranceTnCDataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Observable<String> getInsuranceTnc() {
        return dataStore.getInsuranceTnC();
    }

}
