package com.tokopedia.transaction.bcaoneklik.model.creditcard;

import java.util.List;

/**
 * Created by kris on 8/23/17. Tokopedia
 */

public class CreditCardModel {

    private List<CreditCardModelItem> creditCardList;

    public List<CreditCardModelItem> getCreditCardList() {
        return creditCardList;
    }

    public void setCreditCardList(List<CreditCardModelItem> creditCardList) {
        this.creditCardList = creditCardList;
    }
}
