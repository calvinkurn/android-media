package com.tokopedia.transaction.bcaoneklik.usecase;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.Sha1EncoderUtils;
import com.tokopedia.transaction.bcaoneklik.di.fingerprint.DaggerSingleAuthenticationComponent;
import com.tokopedia.transaction.bcaoneklik.di.fingerprint.SingleAuthenticationComponent;
import com.tokopedia.transaction.bcaoneklik.domain.CreditCardListRepository;
import com.tokopedia.transaction.bcaoneklik.domain.creditcardauthentication.UserInfoRepository;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorUpdateWhiteListResponse;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.UpdateWhiteListRequestData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 4/23/18. Tokopedia
 */

public class CreditCardFingerPrintUseCase extends UseCase<Boolean>{

    @Inject
    CreditCardListRepository creditCardListRepository;

    @Inject
    UserInfoRepository userInfoRepository;

    private static final String PROFILE_USER_ID = "profile_user_id";

    public static final String USER_ID_KEY = "USER_ID_KEY";

    public static final String DEVICE_ID_KEY = "DEVICE_ID_KEY";

    public static final String UPDATED_STATE = "UPDATED_STATE";

    public CreditCardFingerPrintUseCase() {
        SingleAuthenticationComponent component = DaggerSingleAuthenticationComponent
                .builder()
                .build();
        component.inject(this);
    }


    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        final int updatedWhiteListState = requestParams.getInt(UPDATED_STATE, 0);
        final String deviceIdKey = requestParams.getString(DEVICE_ID_KEY, "");
        final String userIdKey = requestParams.getString(USER_ID_KEY, "");
        TKPDMapParam<String, String> userInfoParam = new TKPDMapParam<>();
        userInfoParam.put(PROFILE_USER_ID, requestParams.getString(userIdKey, ""));
        return userInfoRepository.getUserInfo(AuthUtil.generateParamsNetwork(
                userIdKey, deviceIdKey, userInfoParam))
                .flatMap(new Func1<String, Observable<AuthenticatorUpdateWhiteListResponse>>() {
                    @Override
                    public Observable<AuthenticatorUpdateWhiteListResponse> call(String email) {
                        UpdateWhiteListRequestData data = new UpdateWhiteListRequestData();
                        data.setState(updatedWhiteListState);
                        data.setUserEmail(email);
                        JsonObject requestBody = new JsonObject();
                        JsonArray jsonArray = new JsonArray();
                        jsonArray.add(new JsonParser().parse(new Gson().toJson(data)));
                        requestBody.add("data", jsonArray);
                        requestBody.addProperty("signature", Sha1EncoderUtils.getRFC2104HMAC(
                                email + String.valueOf(updatedWhiteListState),
                                AuthUtil.KEY.ZEUS_WHITELIST));
                        return creditCardListRepository.updateCreditCardWhiteList(requestBody);
                    }
                }
        ).map(new Func1<AuthenticatorUpdateWhiteListResponse, Boolean>() {
                    @Override
                    public Boolean call(AuthenticatorUpdateWhiteListResponse authenticatorUpdateWhiteListResponse) {
                        return authenticatorUpdateWhiteListResponse.getStatusCode() == 200;
                    }
                });
    }
}
