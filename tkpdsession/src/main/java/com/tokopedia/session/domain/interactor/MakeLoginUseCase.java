package com.tokopedia.session.domain.interactor;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.data.source.MakeLoginDataSource;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Iterator;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 5/26/17.
 */

public class MakeLoginUseCase extends UseCase<MakeLoginDomain> {

    public static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_UUID = "uuid";
    private final MakeLoginDataSource makeLoginDataSource;
    private SessionHandler sessionHandler;

    @Inject
    public MakeLoginUseCase(MakeLoginDataSource makeLoginDataSource) {
        this.makeLoginDataSource = makeLoginDataSource;
    }

    @Override
    public Observable<MakeLoginDomain> createObservable(RequestParams requestParams) {
        return makeLoginDataSource.makeLogin(requestParams.getParameters());
    }

    public static RequestParams getParam(String userId) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, userId);
        params.putAll(AuthUtil.generateParamsNetwork2(MainApplication.getAppContext(),
                convert(params.getParameters()),
                GCMHandler.getRegistrationId(MainApplication.getAppContext()),
                userId));
        return params;
    }

    private static TKPDMapParam<String, Object> convert(HashMap<String, Object> params){
        TKPDMapParam<String, Object> newParams = new TKPDMapParam<>();
        for(Iterator<String> key = params.keySet().iterator(); key.hasNext(); ){
            String next = key.next();
            newParams.put(next, params.get(next));
        }
        return newParams;
    }
}
