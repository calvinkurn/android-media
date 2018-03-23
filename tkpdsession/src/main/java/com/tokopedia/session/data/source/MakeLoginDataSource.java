package com.tokopedia.session.data.source;

import android.util.Log;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.domain.mapper.MakeLoginMapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 10/18/17.
 */

public class MakeLoginDataSource {

    private final AccountsService accountsService;
    private final MakeLoginMapper makeLoginMapper;
    private final SessionHandler sessionHandler;

    public MakeLoginDataSource(AccountsService accountsService, MakeLoginMapper makeLoginMapper,
                               SessionHandler sessionHandler) {
        this.accountsService = accountsService;
        this.makeLoginMapper = makeLoginMapper;
        this.sessionHandler = sessionHandler;
    }

    public Observable<MakeLoginDomain> makeLogin(Map<String, Object> parameters) {
        return accountsService.getApi()
                .makeLogin(convert(parameters))
                .map(makeLoginMapper)
                .doOnNext(saveToCache());
    }

    private static Map<String, String> convert(Map<String, Object> params){
        Map<String, String> newParams = new com.tokopedia.abstraction.common.utils.TKPDMapParam<>();
        for(Iterator<String> key = params.keySet().iterator(); key.hasNext(); ){
            String next = key.next();
            newParams.put(next, params.get(next).toString());
        }
        return newParams;
    }

    private Action1<MakeLoginDomain> saveToCache() {
        return new Action1<MakeLoginDomain>() {
            @Override
            public void call(MakeLoginDomain makeLoginDomain) {
                if (makeLoginDomain.isLogin()) {
                    sessionHandler.setLoginSession(makeLoginDomain.isLogin(),
                            String.valueOf(makeLoginDomain.getUserId()),
                            makeLoginDomain.getFullName(),
                            String.valueOf(makeLoginDomain.getShopId()),
                            makeLoginDomain.isMsisdnVerified(),
                            makeLoginDomain.getShopName());
                    sessionHandler.setEmail(sessionHandler.getTempEmail());
                    sessionHandler.setGoldMerchant(makeLoginDomain.getShopIsGold());
                    sessionHandler.setPhoneNumber(sessionHandler.getTempPhoneNumber
                            (MainApplication.getAppContext()));
                }else{
                    sessionHandler.setTempLoginName(makeLoginDomain.getFullName());
                    sessionHandler.setTempLoginSession(String.valueOf(makeLoginDomain.getUserId()));
                }
            }
        };
    }
}
