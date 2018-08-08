package com.tokopedia.transaction.bcaoneklik.interactor;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.Sha1EncoderUtils;
import com.tokopedia.transaction.bcaoneklik.domain.BcaOneClickFormRepository;
import com.tokopedia.transaction.bcaoneklik.domain.CreditCardListRepository;
import com.tokopedia.transaction.bcaoneklik.domain.ListPaymentRepository;
import com.tokopedia.transaction.bcaoneklik.domain.creditcardauthentication.UserInfoRepository;
import com.tokopedia.transaction.bcaoneklik.model.PaymentSettingModel;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.BcaOneClickData;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.PaymentListModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardSuccessDeleteModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorPageModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.CheckWhiteListRequestData;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 8/21/17. Tokopedia
 */

public class PaymentListInteractorImpl implements IPaymentListInteractor{

    private CompositeSubscription compositeSubscription;
    private CreditCardListRepository creditCardListRepository;
    private BcaOneClickFormRepository bcaOneClickFormRepository;
    private ListPaymentRepository combinedRequestRepository;
    private UserInfoRepository userInfoRepository;

    public PaymentListInteractorImpl(CompositeSubscription subscription,
                                     CreditCardListRepository creditCardListRepository,
                                     BcaOneClickFormRepository bcaRepository,
                                     ListPaymentRepository combinedRequestRepository,
                                     UserInfoRepository userInfoRepository) {

        this.compositeSubscription = subscription;
        this.creditCardListRepository = creditCardListRepository;
        this.bcaOneClickFormRepository = bcaRepository;
        this.combinedRequestRepository = combinedRequestRepository;
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public void getAllPaymentList(Subscriber<PaymentSettingModel> subscriber,
                                  JsonObject creditCardListRequest,
                                  TKPDMapParam<String, String> bcaOneClickParams) {
        compositeSubscription.add(combinedRequestRepository.getListOfPayment(
                creditCardListRepository.getCreditCardList(creditCardListRequest),
                bcaOneClickFormRepository.getPaymentListUserData(bcaOneClickParams)
        ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void getPaymentList(final Subscriber<CreditCardModel> subscriber,
                               JsonObject requestBody) {
        compositeSubscription.add(creditCardListRepository.getCreditCardList(requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber)
        );
    }

    @Override
    public void deleteCreditCard(Subscriber<CreditCardSuccessDeleteModel> subscriber,
                                 JsonObject requestBody) {
        compositeSubscription.add(creditCardListRepository.deleteCreditCard(requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber)
        );
    }

    @Override
    public void getBcaOneClickAccessToken(Subscriber<BcaOneClickData> subscriber,
                                    TKPDMapParam<String, String> registrationParam) {

        compositeSubscription.add(bcaOneClickFormRepository
                .getBcaOneClickAccessToken(registrationParam)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));

    }

    @Override
    public void deleteBcaOneClick(Subscriber<PaymentListModel> subscriber, TKPDMapParam<String, String> params) {
        compositeSubscription.add(bcaOneClickFormRepository.deleteUserData(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void checkCreditCardWhiteList(
            Subscriber<AuthenticatorPageModel> subscriber,
            TKPDMapParam<String, String> params) {
        compositeSubscription.add(userInfoRepository.getUserInfo(params)
                .flatMap(new Func1<String, Observable<AuthenticatorPageModel>>() {
                    @Override
                    public Observable<AuthenticatorPageModel> call(String userEmail) {
                        JsonObject requestBody = generateGsonParam(userEmail);
                        return creditCardListRepository
                                .checkCreditCardWhiteList(requestBody);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @NonNull
    private JsonObject generateGsonParam(String userEmail) {
        CheckWhiteListRequestData requestData = new CheckWhiteListRequestData();
        requestData.setUserEmail(userEmail);
        JsonObject requestBody = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonParser().parse(new Gson().toJson(requestData)));
        requestBody.add("data", jsonArray);
        requestBody.addProperty(
                "signature",
                Sha1EncoderUtils.getRFC2104HMAC(userEmail,
                        AuthUtil.KEY.ZEUS_WHITELIST));
        return requestBody;
    }

    @Override
    public void getBcaOneClickList(Subscriber<PaymentListModel> subscriber,
                                   TKPDMapParam<String, String> bcaOneClickParam) {
        compositeSubscription.add(bcaOneClickFormRepository
                .getPaymentListUserData(bcaOneClickParam)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void onActivityDestroyed() {
        compositeSubscription.unsubscribe();
    }

}
