package com.tokopedia.transaction.bcaoneklik.presenter;

import android.content.Context;

import com.tokopedia.core.network.apiservices.payment.BcaOneClickService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.bcaoneklik.BcaOneClickEditView;
import com.tokopedia.transaction.bcaoneklik.BcaOneClickView;
import com.tokopedia.transaction.bcaoneklik.domain.BcaOneClickFormRepository;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.BcaOneClickRegisterData;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.PaymentListModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.ACTION_EDIT;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.KEY_ACTION;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.KEY_CREDENTIAL_NO;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.KEY_CREDENTIAL_TYPE;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.KEY_MAX_LIMIT;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.KEY_MERCHANT_CODE;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.KEY_TOKEN_ID;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.KEY_TOKOPEDIA_USER_ID;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.VALUE_TOKOPEDIA_MERCHANT_CODE;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public class BcaOneClickEditPresenterImpl implements BcaOneClickEditPresenter{

    private BcaOneClickView mainView;
    private CompositeSubscription compositeSubscription;
    private BcaOneClickFormRepository bcaOneClickFormRepository;

    public BcaOneClickEditPresenterImpl(BcaOneClickEditView mainView) {
        BcaOneClickService bcaOneClickService = new BcaOneClickService();
        compositeSubscription = new CompositeSubscription();
        bcaOneClickFormRepository = new BcaOneClickFormRepository(bcaOneClickService);
    }

    @Override
    public void editUserDataBca(Context context,
                                BcaOneClickRegisterData data,
                                Subscriber<PaymentListModel> subscriber) {
        TKPDMapParam<String, String> bcaOneClickRegisterParam = new TKPDMapParam<>();
        bcaOneClickRegisterParam.put(KEY_TOKOPEDIA_USER_ID, SessionHandler.getLoginID(context));
        bcaOneClickRegisterParam.put(KEY_MERCHANT_CODE, VALUE_TOKOPEDIA_MERCHANT_CODE);
        bcaOneClickRegisterParam.put(KEY_TOKEN_ID, data.getTokenId());
        bcaOneClickRegisterParam.put(KEY_ACTION, ACTION_EDIT);
        bcaOneClickRegisterParam.put(KEY_CREDENTIAL_TYPE, data.getCredentialType());
        bcaOneClickRegisterParam.put(KEY_CREDENTIAL_NO, data.getCredentialNumber());
        bcaOneClickRegisterParam.put(KEY_MAX_LIMIT, data.getMaxLimit());
        compositeSubscription.add(bcaOneClickFormRepository.editBcaOneClickData(AuthUtil
                .generateParamsNetwork(context, bcaOneClickRegisterParam))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void onDestroy() {
        compositeSubscription.unsubscribe();
    }
}
