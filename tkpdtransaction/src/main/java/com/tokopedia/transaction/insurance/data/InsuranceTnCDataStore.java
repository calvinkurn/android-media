package com.tokopedia.transaction.insurance.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.insurance.data.network.InsuranceWebViewService;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Irfan Khoirul on 11/12/17.
 */

public class InsuranceTnCDataStore {

    private final InsuranceWebViewService service;

    @Inject
    public InsuranceTnCDataStore(InsuranceWebViewService service) {
        this.service = service;
    }

    public Observable<String> getInsuranceTnC() {
        return service.getApi().getInsuranceTnC();
    }

}
