package com.tokopedia.transaction.bcaoneklik.domain;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tokopedia.core.network.apiservices.transaction.CreditCardVaultService;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModelItem;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardResponse;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardSuccessDeleteModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 8/21/17. Tokopedia
 */

public class CreditCardListRepository implements ICreditCardRepository {


    @Override
    public Observable<CreditCardModel> getCreditCardList(
            CreditCardVaultService service,
            JsonObject requestBody
    ) {
        return service.getApi().getListCreditCard(requestBody).map(
                new Func1<Response<String>, CreditCardModel>() {
            @Override
            public CreditCardModel call(Response<String> stringResponse) {
                return creditCardModel(new Gson().fromJson(stringResponse.body(),
                        CreditCardResponse.class));
            }
        });
    }

    @Override
    public Observable<CreditCardSuccessDeleteModel> deleteCreditCard(
            CreditCardVaultService service,
            JsonObject requestBody
    ) {
        return service.getApi().deleteCreditCard(requestBody).map(new Func1<Response<String>,
                CreditCardSuccessDeleteModel>() {
            @Override
            public CreditCardSuccessDeleteModel call(Response<String> stringResponse) {
                return new Gson().fromJson(stringResponse.body(),
                        CreditCardSuccessDeleteModel.class);
            }
        });
    }

    private CreditCardModel creditCardModel(CreditCardResponse response) {
        CreditCardModel convertedCreditCardModel = new CreditCardModel();
        List<CreditCardModelItem> creditCardModels = new ArrayList<>();
        for (int i = 0; i < response.getData().size(); i++) {
            CreditCardModelItem modelItem = new CreditCardModelItem();
            modelItem.setBank(response.getData().get(i).getBank());
            modelItem.setCardType(response.getData().get(i).getCardType());
            modelItem.setExpiryMonth(response.getData().get(i).getExpiryMonth());
            modelItem.setExpiryYear(response.getData().get(i).getExpiryMonth());
            modelItem.setMaskedNumber(response.getData().get(i).getMaskedNumber());
            modelItem.setTokenId(response.getData().get(i).getTokenId());
            modelItem.setImage(response.getData().get(i).getImage());
            creditCardModels.add(modelItem);
        }
        convertedCreditCardModel.setCreditCardList(creditCardModels);
        return convertedCreditCardModel;
    }
}
