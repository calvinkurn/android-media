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
        bcaOneClickParam.put("tokopedia_user_id", SessionHandler.getLoginID(mainView.getContext()));
        bcaOneClickParam.put("merchant_code", "tokopedia");
        bcaOneClickParam.put("action", "auth");
        bcaOneClickParam.put("profile_code", "TKPD_DEFAULT");
        compositeSubscription.add(bcaOneClickRepository.getBcaOneClickAccessToken(AuthUtil
                .generateParamsNetwork(mainView.getContext() ,bcaOneClickParam))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void onGetPaymentList(Subscriber<PaymentListModel> subscriber) {
        TKPDMapParam<String, String> paymentListParam = new TKPDMapParam<>();
        paymentListParam.put("tokopedia_user_id", SessionHandler.getLoginID(mainView.getContext()));
        paymentListParam.put("merchant_code", "tokopedia");
        paymentListParam.put("action", "get");
        compositeSubscription.add(bcaOneClickRepository.getPaymentListUserData(AuthUtil
                .generateParamsNetwork(mainView.getContext(), paymentListParam)).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void onDeletePaymentList(Subscriber<PaymentListModel> subscriber, String tokenId) {
        TKPDMapParam<String, String> paymentListParam = new TKPDMapParam<>();
        paymentListParam.put("tokopedia_user_id", SessionHandler.getLoginID(mainView.getContext()));
        paymentListParam.put("merchant_code", "tokopedia");
        paymentListParam.put("action", "delete");
        paymentListParam.put("token_id", tokenId);
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
