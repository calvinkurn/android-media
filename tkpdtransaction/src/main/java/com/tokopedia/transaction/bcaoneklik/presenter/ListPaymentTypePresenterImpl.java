package com.tokopedia.transaction.bcaoneklik.presenter;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.Sha1EncoderUtils;
import com.tokopedia.transaction.bcaoneklik.interactor.IPaymentListInteractor;
import com.tokopedia.transaction.bcaoneklik.interactor.PaymentListInteractorImpl;
import com.tokopedia.transaction.bcaoneklik.listener.ListPaymentTypeView;
import com.tokopedia.transaction.bcaoneklik.model.PaymentSettingModel;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.BcaOneClickData;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.PaymentListModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardSuccessDeleteModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorPageModel;
import com.tokopedia.transaction.exception.ResponseRuntimeException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.ACCESS_TOKEN_EXTRAS;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.ACCESS_XCOID_EXTRAS;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.ACTION_AUTH;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.ACTION_DELETE;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.ACTION_GET;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.CREDENTIAL_NUMBER_EXTRAS;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.CREDENTIAL_TYPE_EXTRAS;
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
    private IPaymentListInteractor interactor;

    @Inject
    public ListPaymentTypePresenterImpl(PaymentListInteractorImpl interactor) {
        this.interactor = interactor;

    }

    @Override
    public void onRequestBcaOneClickAccessToken(String tokenId,
                                                String credentialType,
                                                String credentialNumber,
                                                int mode) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(KEY_TOKOPEDIA_USER_ID, SessionHandler.getLoginID(mainView.getContext()));
        params.put(KEY_MERCHANT_CODE, VALUE_TOKOPEDIA_MERCHANT_CODE);
        params.put(KEY_ACTION, ACTION_AUTH);
        params.put(KEY_PROFILE_CODE, VALUE_TOKPEDIA_PROFILE_CODE);
        interactor.getBcaOneClickAccessToken(
                getBcaTokenSubscriber(
                        tokenId, credentialType, credentialNumber
                ), params);
    }

    @Override
    public void onRequestBcaOneClickRegisterToken() {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(KEY_TOKOPEDIA_USER_ID, SessionHandler.getLoginID(mainView.getContext()));
        params.put(KEY_MERCHANT_CODE, VALUE_TOKOPEDIA_MERCHANT_CODE);
        params.put(KEY_ACTION, ACTION_AUTH);
        params.put(KEY_PROFILE_CODE, VALUE_TOKPEDIA_PROFILE_CODE);
        interactor.getBcaOneClickAccessToken(
                getBcaRegisterToken(
                ), params);
    }

    @Override
    public void checkCreditCardWhiteList() {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put("profile_user_id", SessionHandler.getLoginID(mainView.getContext()));
        interactor.checkCreditCardWhiteList(receivedAuthenticatorLogicModel(), AuthUtil
                .generateParamsNetwork(mainView.getContext(), params));
    }

    @Override
    public void onGetBcaOneClickList(Subscriber<PaymentListModel> subscriber) {
        TKPDMapParam<String, String> bcaOneClickListParam = new TKPDMapParam<>();
        bcaOneClickListParam.put(KEY_TOKOPEDIA_USER_ID, SessionHandler.getLoginID(mainView.getContext()));
        bcaOneClickListParam.put(KEY_MERCHANT_CODE, VALUE_TOKOPEDIA_MERCHANT_CODE);
        bcaOneClickListParam.put(KEY_ACTION, ACTION_GET);
        interactor.getBcaOneClickList(subscriber, bcaOneClickListParam);
    }

    //TODO Remove Later?
    @Override
    public void onGetCreditCardList(Context context) {
        String userId = SessionHandler.getLoginID(context);
        String date = generateDate();
        String merchantCode = "tokopedia";
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("user_id", userId);
        requestBody.addProperty("merchant_code", merchantCode);
        requestBody.addProperty("date", date);
        /*requestBody.addProperty("signature", calculateRFC2104HMAC(userId + merchantCode + date,
                AuthUtil.KEY.KEY_CREDIT_CARD_VAULT));*/
        requestBody.addProperty("signature", Sha1EncoderUtils
                .getRFC2104HMAC(userId + merchantCode + date,
                AuthUtil.KEY.KEY_CREDIT_CARD_VAULT));

        interactor.getPaymentList(creditCardListSubsciber(), requestBody);
    }

    @Override
    public void onGetAllPaymentList(Context context) {
        TKPDMapParam<String, String> bcaOneClickListParam = new TKPDMapParam<>();
        bcaOneClickListParam.put(KEY_TOKOPEDIA_USER_ID, SessionHandler.getLoginID(mainView.getContext()));
        bcaOneClickListParam.put(KEY_MERCHANT_CODE, VALUE_TOKOPEDIA_MERCHANT_CODE);
        bcaOneClickListParam.put(KEY_ACTION, ACTION_GET);

        String userId = SessionHandler.getLoginID(context);
        String date = generateDate();
        String merchantCode = "tokopedia";
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("user_id", userId);
        requestBody.addProperty("merchant_code", merchantCode);
        requestBody.addProperty("date", date);
        requestBody.addProperty("signature", Sha1EncoderUtils
                .getRFC2104HMAC(userId + merchantCode + date,
                        AuthUtil.KEY.KEY_CREDIT_CARD_VAULT));

        interactor.getAllPaymentList(paymentSettingListSubscriber(),
                requestBody,
                bcaOneClickListParam);
    }

    @Override
    public void onDeleteBcaOneClick(String tokenId) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> paymentListParam = new TKPDMapParam<>();
        paymentListParam.put(KEY_TOKOPEDIA_USER_ID, SessionHandler.getLoginID(mainView.getContext()));
        paymentListParam.put(KEY_MERCHANT_CODE, VALUE_TOKOPEDIA_MERCHANT_CODE);
        paymentListParam.put(KEY_ACTION, ACTION_DELETE);
        paymentListParam.put(KEY_TOKEN_ID, tokenId);
        interactor.deleteBcaOneClick(deleteBcaOneClickSubscriber(), paymentListParam);
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
        /*requestBody.addProperty("signature", calculateRFC2104HMAC(userId + merchantCode + date,
                AuthUtil.KEY.KEY_CREDIT_CARD_VAULT));*/
        requestBody.addProperty("signature", Sha1EncoderUtils
                .getRFC2104HMAC(userId + merchantCode + date,
                AuthUtil.KEY.KEY_CREDIT_CARD_VAULT));
        requestBody.addProperty("token_id", tokenId);
        interactor.deleteCreditCard(creditCardDeleteSubscriber(), requestBody);
    }

    @Override
    public String getUserLoginAccountName() {
        return SessionHandler.getLoginName(mainView.getContext());
    }

    @Override
    public void onDestroyed() {
        interactor.onActivityDestroyed();
    }

    private String generateDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"
                , Locale.ENGLISH);
        return simpleDateFormat.format(new Date());
    }

    private Subscriber<BcaOneClickData> getBcaTokenSubscriber(
            final String tokenId,
            final String credentialType,
            final String credentialNumber) {
        return new Subscriber<BcaOneClickData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BcaOneClickData bcaOneClickData) {
                Bundle bundle = new Bundle();
                bundle.putString(ACCESS_TOKEN_EXTRAS,
                        bcaOneClickData.getToken().getAccessToken());
                bundle.putString(ACCESS_XCOID_EXTRAS, tokenId);
                bundle.putString(CREDENTIAL_TYPE_EXTRAS, credentialType);
                bundle.putString(CREDENTIAL_NUMBER_EXTRAS, credentialNumber);
                mainView.onBcaOneClickSuccessGetToken(bundle);
            }
        };
    }

    private Subscriber<BcaOneClickData> getBcaRegisterToken() {
        return new Subscriber<BcaOneClickData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BcaOneClickData bcaOneClickData) {
                Bundle bundle = new Bundle();
                bundle.putString(ACCESS_TOKEN_EXTRAS,
                        bcaOneClickData.getToken().getAccessToken());
                mainView.onBcaOneClickSuccessGetRegisterToken(bundle);
            }
        };
    }

    private Subscriber<CreditCardSuccessDeleteModel> creditCardDeleteSubscriber() {
        return new Subscriber<CreditCardSuccessDeleteModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof ResponseRuntimeException) {
                    mainView.onDeleteCreditCardError(e.getMessage());
                }
            }

            @Override
            public void onNext(CreditCardSuccessDeleteModel creditCardSuccessDeleteModel) {
                if(creditCardSuccessDeleteModel.isSuccess()) {
                    mainView.successDeleteCreditCard(creditCardSuccessDeleteModel.getMessage());
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
                if(e instanceof ResponseRuntimeException) {
                    mainView.onLoadAllError(e.getMessage());
                } else mainView.onLoadAllError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT_SHORT);
            }

            @Override
            public void onNext(CreditCardModel creditCardModel) {
                PaymentSettingModel model = new PaymentSettingModel();
                model.setCreditCardResponse(creditCardModel);
                mainView.dismissMainDialog();
                mainView.onFetchDataComplete(model);
            }
        };
    }

    private Subscriber<PaymentSettingModel> paymentSettingListSubscriber() {
        return new Subscriber<PaymentSettingModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
/*                if(e instanceof ResponseRuntimeException) {
                    mainView.onLoadAllError(e.getMessage());
                    onGetCreditCardList(mainView.getContext());
                } else mainView.onLoadAllError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT_SHORT);*/
                onGetCreditCardList(mainView.getContext());
                mainView.dismissMainDialog();
            }

            @Override
            public void onNext(PaymentSettingModel paymentSettingModel) {
                mainView.dismissMainDialog();
                mainView.onFetchDataComplete(paymentSettingModel);
            }
        };
    }

    private Subscriber<PaymentListModel> deleteBcaOneClickSubscriber() {
        return new Subscriber<PaymentListModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mainView.showDeleteBcaOneClickError();
                mainView.dismissProgressDialog();
            }

            @Override
            public void onNext(PaymentListModel paymentListModel) {
                mainView.dismissProgressDialog();
                mainView.refreshList();
            }
        };
    }

    private Subscriber<AuthenticatorPageModel> receivedAuthenticatorLogicModel() {
        return new Subscriber<AuthenticatorPageModel>() {
            @Override
            public void onCompleted() {
                mainView.dismissProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                mainView.dismissProgressDialog();
                if(e instanceof ResponseRuntimeException) {
                    mainView.showError(e.getMessage());
                } else mainView.showError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);

            }

            @Override
            public void onNext(AuthenticatorPageModel data) {
                mainView.dismissProgressDialog();
                mainView.openAuthenticatorPage(data);
            }
        };
    }
}
