package com.tokopedia.transaction.bcaoneklik.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.util.Sha1EncoderUtils;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.bcaoneklik.interactor.CreditCardAuthenticatorInteractor;
import com.tokopedia.transaction.bcaoneklik.interactor.CreditCardAuthenticatorInteractorImpl;
import com.tokopedia.transaction.bcaoneklik.listener.CreditCardAuthenticationView;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorLogicModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorPageModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorUpdateWhiteListResponse;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.UpdateWhiteListRequestData;
import com.tokopedia.transaction.exception.ResponseRuntimeException;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by kris on 10/11/17. Tokopedia
 */

public class CreditCardAuthenticationPresenterImpl implements CreditCardAuthenticationPresenter{

    private CreditCardAuthenticationView mainView;

    private CreditCardAuthenticatorInteractor interactor;

    public CreditCardAuthenticationPresenterImpl(CreditCardAuthenticatorInteractorImpl interactor) {
        this.interactor = interactor;
    }

    @Override
    public void setListener(CreditCardAuthenticationView view) {
        mainView = view;
    }

    @Override
    public void updateWhiteListStatus(AuthenticatorPageModel model) {
        UpdateWhiteListRequestData data = new UpdateWhiteListRequestData();
        data.setState(model.getState());
        data.setUserEmail(model.getUserEmail());
        JsonObject requestBody = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonParser().parse(new Gson().toJson(data)));
        requestBody.add("data", jsonArray);
        requestBody.addProperty("signature", Sha1EncoderUtils.getRFC2104HMAC(
                model.getUserEmail() + String.valueOf(model.getState()),
                AuthUtil.KEY.ZEUS_WHITELIST));
        interactor.updateAuthenticationStatus(requestBody, updateSubscriber());
    }

    @Override
    public List<AuthenticatorLogicModel> initiateLogicModel(AuthenticatorPageModel model) {
        List<AuthenticatorLogicModel> modelList = new ArrayList<>();
        modelList.add(initiateSingleAuthentication(model));
        modelList.add(initiateDoubleAuthentication(model));
        return modelList;
    }

    private AuthenticatorLogicModel initiateSingleAuthentication(AuthenticatorPageModel model) {
        AuthenticatorLogicModel singleAuthentication = new AuthenticatorLogicModel();
        singleAuthentication.setAuhtenticationTitle(
                mainView.getContext().getString(R.string.authentication_title_1));
        singleAuthentication.setAuthenticationDescription(
                mainView.getContext().getString(R.string.authentication_description_1));
        singleAuthentication.setStateWhenSelected(1);
        singleAuthentication.setSelected(model.getState() == 1);
        return singleAuthentication;
    }

    private AuthenticatorLogicModel initiateDoubleAuthentication(AuthenticatorPageModel model) {
        AuthenticatorLogicModel doubleAuthentication = new AuthenticatorLogicModel();
        doubleAuthentication.setAuhtenticationTitle(
                mainView.getContext().getString(R.string.authentication_title_2));
        doubleAuthentication.setAuthenticationDescription(
                mainView.getContext().getString(R.string.authentication_description_2));
        doubleAuthentication.setStateWhenSelected(0);
        doubleAuthentication.setSelected(model.getState() == 0);
        return doubleAuthentication;
    }

    private Subscriber<AuthenticatorUpdateWhiteListResponse> updateSubscriber() {
        return new Subscriber<AuthenticatorUpdateWhiteListResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof ResponseRuntimeException)
                    mainView.showErrorMessage(e.getMessage());
                else mainView.showErrorMessage(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }

            @Override
            public void onNext(AuthenticatorUpdateWhiteListResponse
                                       authenticatorUpdateWhiteListResponse) {
                mainView.finishUpdateStatus(authenticatorUpdateWhiteListResponse.getMessage());
            }
        };
    }

}
