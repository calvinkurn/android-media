package com.tokopedia.transaction.bcaoneklik.usecase;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.Sha1EncoderUtils;
import com.tokopedia.transaction.bcaoneklik.domain.CreditCardListRepository;
import com.tokopedia.transaction.bcaoneklik.domain.creditcardauthentication.UserInfoRepository;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorUpdateWhiteListResponse;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.UpdateWhiteListRequestData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 4/23/18. Tokopedia
 */

public class CreditCardFingerPrintUseCase extends UseCase<AuthenticatorUpdateWhiteListResponse>{

    private final CreditCardListRepository creditCardListRepository;

    private final UserInfoRepository userInfoRepository;

    private static final String USER_ID_KEY = "USER_ID_KEY";

    public CreditCardFingerPrintUseCase(CreditCardListRepository creditCardListRepository,
                                        UserInfoRepository userInfoRepository) {
        this.creditCardListRepository = creditCardListRepository;
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public Observable<AuthenticatorUpdateWhiteListResponse> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> userInfoParam = new TKPDMapParam<>();
        userInfoParam.put("profile_user_id", requestParams.getString(USER_ID_KEY, ""));
        return userInfoRepository.getUserInfo(userInfoParam)
                .flatMap(new Func1<String, Observable<AuthenticatorUpdateWhiteListResponse>>() {
                    @Override
                    public Observable<AuthenticatorUpdateWhiteListResponse> call(String email) {
                        UpdateWhiteListRequestData data = new UpdateWhiteListRequestData();
                        data.setState(1);
                        data.setUserEmail(email);
                        JsonObject requestBody = new JsonObject();
                        JsonArray jsonArray = new JsonArray();
                        jsonArray.add(new JsonParser().parse(new Gson().toJson(data)));
                        requestBody.add("data", jsonArray);
                        requestBody.addProperty("signature", Sha1EncoderUtils.getRFC2104HMAC(
                                email + String.valueOf(1),
                                AuthUtil.KEY.ZEUS_WHITELIST));
                        return creditCardListRepository.updateCreditCardWhiteList(requestBody);
                    }
                }
        );
    }
}
