package com.tokopedia.transaction.bcaoneklik.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.bcaoneklik.domain.BcaOneClickFormRepository;
import com.tokopedia.transaction.bcaoneklik.interactor.IPaymentListInteractor;
import com.tokopedia.transaction.bcaoneklik.interactor.PaymentListInteractorImpl;
import com.tokopedia.transaction.bcaoneklik.listener.ListPaymentTypeView;
import com.tokopedia.transaction.bcaoneklik.model.BcaOneClickData;
import com.tokopedia.transaction.bcaoneklik.model.PaymentListModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardSuccessDeleteModel;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

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
    private static final String MAC_ALGORITHM = "HmacSHA1";
    private ListPaymentTypeView mainView;
    private CompositeSubscription compositeSubscription;
    private BcaOneClickFormRepository bcaOneClickRepository;
    private IPaymentListInteractor interactor;

    @Inject
    public ListPaymentTypePresenterImpl(CompositeSubscription compositeSubscription,
                                        BcaOneClickFormRepository bcaOneClickRepository,
                                        PaymentListInteractorImpl interactor) {
        this.compositeSubscription = compositeSubscription;
        this.bcaOneClickRepository = bcaOneClickRepository;
        this.interactor = interactor;

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
    public void onGetCreditCardList(Context context) {

        String userId = SessionHandler.getLoginID(context);
        String date = generateDate();
        String merchantCode = "tokopedia";
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("user_id", userId);
        requestBody.addProperty("merchant_code", merchantCode);
        requestBody.addProperty("date", date);
        //TODO change key to production
        requestBody.addProperty("signature", calculateRFC2104HMAC(userId + merchantCode + date,
                "QN6vublhpfqppu01uxdk5f"));

        interactor.getPaymentList(creditCardListSubsciber(), requestBody);
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
    public void setViewListener(ListPaymentTypeView view) {
        mainView = view;
    }

    @Override
    public void onCreditCardDeleted(Context context, String tokenId) {
        String userId = SessionHandler.getLoginID(context);
        String date = generateDate();
        String merchantCode = "tokopedia";
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("user_id", userId);
        requestBody.addProperty("merchant_code", merchantCode);
        requestBody.addProperty("date", date);
        //TODO change key to production
        requestBody.addProperty("signature", calculateRFC2104HMAC(userId + merchantCode + date,
                "QN6vublhpfqppu01uxdk5f"));
        requestBody.addProperty("token_id", tokenId);
        interactor.deleteCreditCard(creditCardDeleteSubscriber(), requestBody);
    }

    @Override
    public void onDestroyed() {
        compositeSubscription.unsubscribe();
    }

    private String generateDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"
                , Locale.ENGLISH);
        return simpleDateFormat.format(new Date());
    }

    private String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    private String calculateRFC2104HMAC(String authString, String authKey) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(authKey.getBytes(), MAC_ALGORITHM);
            Mac mac = Mac.getInstance(MAC_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(authString.getBytes());
            return convertToHex(rawHmac);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "";
        }
    }

    private Subscriber<CreditCardSuccessDeleteModel> creditCardDeleteSubscriber() {
        return new Subscriber<CreditCardSuccessDeleteModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CreditCardSuccessDeleteModel creditCardSuccessDeleteModel) {
                if(creditCardSuccessDeleteModel.isSuccess()) {
                    mainView.successDeleteCreditCard();
                }
            }
        };
    }

    private Subscriber<CreditCardModel> creditCardListSubsciber() {
        return new Subscriber<CreditCardModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CreditCardModel creditCardModel) {
                mainView.receivedCreditCardList(creditCardModel);
            }
        };
    }
}
