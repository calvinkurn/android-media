package com.tokopedia.transaction.bcaoneklik.presenter;

import android.content.Context;

import com.tokopedia.core.network.apiservices.payment.BcaOneClickService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.bcaoneklik.BcaOneClickView;
import com.tokopedia.transaction.bcaoneklik.domain.BcaOneClickFormRepository;
import com.tokopedia.transaction.bcaoneklik.model.BcaOneClickRegisterData;
import com.tokopedia.transaction.bcaoneklik.model.BcaOneClickSuccessRegisterData;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 7/25/17. Tokopedia
 */

public class BcaOneClickPresenterImpl implements BcaOneClickPresenter{

    private BcaOneClickView mainView;
    private CompositeSubscription compositeSubscription;
    private BcaOneClickFormRepository bcaOneClickFormRepository;

    public BcaOneClickPresenterImpl(BcaOneClickView view) {
        mainView = view;
        BcaOneClickService bcaOneClickService = new BcaOneClickService();
        compositeSubscription = new CompositeSubscription();
        bcaOneClickFormRepository = new BcaOneClickFormRepository(bcaOneClickService);
    }

    @Override
    public void addUserDataBca(Context context,
                               BcaOneClickRegisterData data,
                               Subscriber<BcaOneClickSuccessRegisterData>subscriber) {
        TKPDMapParam<String, String> bcaOneClickRegisterParam = new TKPDMapParam<>();
        bcaOneClickRegisterParam.put("tokopedia_user_id", SessionHandler.getLoginID(context));
        bcaOneClickRegisterParam.put("merchant_code", "tokopedia");
        bcaOneClickRegisterParam.put("token_id", data.getTokenId());
        bcaOneClickRegisterParam.put("action", "add");
        bcaOneClickRegisterParam.put("credential_type", data.getCredentialType());
        bcaOneClickRegisterParam.put("credential_no", data.getCredentialNumber());
        bcaOneClickRegisterParam.put("max_limit", data.getMaxLimit());
        compositeSubscription.add(bcaOneClickFormRepository.registerBcaOneClickData(AuthUtil
                .generateParamsNetwork(context, bcaOneClickRegisterParam))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }
}
