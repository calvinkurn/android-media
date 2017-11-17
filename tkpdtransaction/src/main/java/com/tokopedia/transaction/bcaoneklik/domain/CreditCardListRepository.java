package com.tokopedia.transaction.bcaoneklik.domain;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tokopedia.core.network.apiservices.transaction.CreditCardAuthService;
import com.tokopedia.core.network.apiservices.transaction.CreditCardVaultService;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModelItem;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardResponse;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardSuccessDeleteModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorCheckWhiteListResponse;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorPageModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorUpdateWhiteListResponse;
import com.tokopedia.transaction.exception.ResponseRuntimeException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 8/21/17. Tokopedia
 */

public class CreditCardListRepository implements ICreditCardRepository {


    private static final int SUCCESS_CODE = 200;
    private static final int FAILED_CODE = 500;
    private CreditCardVaultService service;

    private CreditCardAuthService authService;

    public CreditCardListRepository(
            CreditCardVaultService service,
            CreditCardAuthService authService) {
        this.service = service;
        this.authService = authService;
    }

    @Override
    public Observable<CreditCardModel> getCreditCardList(JsonObject requestBody
    ) {
        return service.getApi().getListCreditCard(requestBody).map(
                new Func1<Response<String>, CreditCardModel>() {
            @Override
            public CreditCardModel call(Response<String> stringResponse) {
                handlerFetchError(stringResponse);
                return creditCardModel(new Gson().fromJson(stringResponse.body(),
                        CreditCardResponse.class));
            }
        });
    }

    @Override
    public Observable<CreditCardSuccessDeleteModel> deleteCreditCard(
            JsonObject requestBody
    ) {
        return service.getApi().deleteCreditCard(requestBody).map(new Func1<Response<String>,
                CreditCardSuccessDeleteModel>() {
            @Override
            public CreditCardSuccessDeleteModel call(Response<String> stringResponse) {
                handlerFetchError(stringResponse);
                CreditCardSuccessDeleteModel model = new Gson().fromJson(stringResponse.body(),
                        CreditCardSuccessDeleteModel.class);
                handleDataError(model.isSuccess(), model.getMessage());
                return model;
            }
        });
    }

    @Override
    public Observable<AuthenticatorPageModel> checkCreditCardWhiteList(JsonObject request) {
        return authService.getApi().checkWhiteList(request).map(new Func1<Response<String>,
                AuthenticatorPageModel>() {
            @Override
            public AuthenticatorPageModel call(Response<String> stringResponse) {
                return getPageModel(new Gson().fromJson(stringResponse.body(),
                        AuthenticatorCheckWhiteListResponse.class));
            }
        });
    }

    @Override
    public Observable<AuthenticatorUpdateWhiteListResponse> updateCreditCardWhiteList(JsonObject request) {
        return authService.getApi().updateWhiteList(request)
                .map(new Func1<Response<String>, AuthenticatorUpdateWhiteListResponse>() {
            @Override
            public AuthenticatorUpdateWhiteListResponse call(Response<String> stringResponse) {
                AuthenticatorUpdateWhiteListResponse response = new Gson().fromJson(
                        stringResponse.body(),
                        AuthenticatorUpdateWhiteListResponse.class
                );
                handleDataError(response.getStatusCode() == SUCCESS_CODE, response.getMessage());
                return response;
            }
        });
    }

    private CreditCardModel creditCardModel(CreditCardResponse response) {
        handleDataError(response.getSuccess(), response.getMessage());
        CreditCardModel convertedCreditCardModel = new CreditCardModel();
        List<CreditCardModelItem> creditCardModels = new ArrayList<>();
        for (int i = 0; i < response.getData().size(); i++) {
            CreditCardModelItem modelItem = new CreditCardModelItem();
            modelItem.setBank(response.getData().get(i).getBank());
            modelItem.setCardType(response.getData().get(i).getCardTypeName());
            modelItem.setExpiryMonth(response.getData().get(i).getExpiryMonth());
            modelItem.setExpiryYear(response.getData().get(i).getExpiryYear());
            modelItem.setMaskedNumber(response.getData().get(i).getMaskedNumber());
            modelItem.setTokenId(response.getData().get(i).getTokenId());
            modelItem.setImage(response.getData().get(i).getImage());
            modelItem.setCardTypeImage(response.getData().get(i).getCardTypeImage());
            creditCardModels.add(modelItem);
        }
        convertedCreditCardModel.setCreditCardList(creditCardModels);
        return convertedCreditCardModel;
    }

    private void handlerFetchError(Response<String> response) {
        if (response.body() == null)
            throw new ResponseRuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT_SHORT);
    }

    private void handleDataError(boolean isSuccess, String message) {
        if(!isSuccess) {
            throw new ResponseRuntimeException(message);
        }
    }

    private AuthenticatorPageModel getPageModel(
            AuthenticatorCheckWhiteListResponse data
    ) {
        AuthenticatorPageModel model = new AuthenticatorPageModel();
        handleErrorCheckWhiteList(data);
        model.setUserEmail(data.getDatas().get(0).getUserEmail());
        model.setState(data.getDatas().get(0).getState());
        return model;
    }

    private void handleErrorCheckWhiteList(AuthenticatorCheckWhiteListResponse data) {
        if(data.getDatas() == null || data.getDatas().isEmpty()) {
            throw new ResponseRuntimeException(data.getMessage());
        } else if(data.getStatusCode() == FAILED_CODE) {
            throw new ResponseRuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }
}
