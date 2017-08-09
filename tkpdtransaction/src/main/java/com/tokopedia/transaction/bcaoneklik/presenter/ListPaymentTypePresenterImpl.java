package com.tokopedia.transaction.bcaoneklik.presenter;

import com.tokopedia.core.network.apiservices.payment.BcaOneClickService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.bcaoneklik.domain.BcaOneClickFormRepository;
import com.tokopedia.transaction.bcaoneklik.listener.ListPaymentTypeView;
import com.tokopedia.transaction.bcaoneklik.model.BcaOneClickData;
import com.tokopedia.transaction.bcaoneklik.model.PaymentListModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.ACTION_AUTH;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.ACTION_DELETE;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.ACTION_GET;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.KEY_ACTION;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.KEY_MERCHANT_CODE;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.KEY_PROFILE_CODE;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.KEY_TOKEN_ID;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.KEY_TOKOPEDIA_USER_ID;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.VALUE_TOKOPEDIA_MERCHANT_CODE;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.VALUE_TOKPEDIA_PROFILE_CODE;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public class ListPaymentTypePresenterImpl implements ListPaymentTypePresenter {
    private ListPaymentTypeView mainView;
    private CompositeSubscription compositeSubscription;
    private BcaOneClickFormRepository bcaOneClickRepository;

    public ListPaymentTypePresenterImpl(ListPaymentTypeView view) {
        mainView = view;
        BcaOneClickService bcaOneClickService = new BcaOneClickService();
        compositeSubscription = new CompositeSubscription();
        bcaOneClickRepository = new BcaOneClickFormRepository(bcaOneClickService);
    }

    @Override
    public void onRegisterOneClickBcaChosen(Subscriber<BcaOneClickData> subscriber) {
        TKPDMapParam<String, String> bcaOneClickParam = new TKPDMapParam<>();
        bcaOneClickParam.put(KEY_TOKOPEDIA_USER_ID, SessionHandler.getLoginID(mainView.getContext()));
        bcaOneClickParam.put(KEY_MERCHANT_CODE, VALUE_TOKOPEDIA_MERCHANT_CODE);
        bcaOneClickParam.put(KEY_ACTION, ACTION_AUTH);
        bcaOneClickParam.put(KEY_PROFILE_CODE, VALUE_TOKPEDIA_PROFILE_CODE);
        compositeSubscription.add(bcaOneClickRepository.getBcaOneClickAccessToken(AuthUtil
                .generateParamsNetwork(mainView.getContext() ,bcaOneClickParam))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void onGetPaymentList(Subscriber<PaymentListModel> subscriber) {
        mainView.showMainDialog();
        TKPDMapParam<String, String> paymentListParam = new TKPDMapParam<>();
        paymentListParam.put(KEY_TOKOPEDIA_USER_ID, SessionHandler.getLoginID(mainView.getContext()));
        paymentListParam.put(KEY_MERCHANT_CODE, VALUE_TOKOPEDIA_MERCHANT_CODE);
        paymentListParam.put(KEY_ACTION, ACTION_GET);
        compositeSubscription.add(bcaOneClickRepository.getPaymentListUserData(AuthUtil
                .generateParamsNetwork(mainView.getContext(), paymentListParam)).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void onDeletePaymentList(Subscriber<PaymentListModel> subscriber, String tokenId) {
        TKPDMapParam<String, String> paymentListParam = new TKPDMapParam<>();
        paymentListParam.put(KEY_TOKOPEDIA_USER_ID, SessionHandler.getLoginID(mainView.getContext()));
        paymentListParam.put(KEY_MERCHANT_CODE, VALUE_TOKOPEDIA_MERCHANT_CODE);
        paymentListParam.put(KEY_ACTION, ACTION_DELETE);
        paymentListParam.put(KEY_TOKEN_ID, tokenId);
        compositeSubscription.add(bcaOneClickRepository.deleteUserData(AuthUtil
                .generateParamsNetwork(mainView.getContext(), paymentListParam))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void onDestroyed() {
        compositeSubscription.unsubscribe();
    }
}
