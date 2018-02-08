package com.tokopedia.transaction.bcaoneklik.domain;


import com.tokopedia.transaction.bcaoneklik.model.PaymentSettingModel;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.PaymentListModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModel;

import rx.Observable;

/**
 * Created by kris on 10/4/17. Tokopedia
 */

public interface IListPaymentRepository {

    Observable<PaymentSettingModel> getListOfPayment(
            Observable<CreditCardModel> creditCardModelObservable,
            Observable<PaymentListModel> bcaOneClickListObservable);

}
