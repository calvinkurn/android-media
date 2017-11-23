package com.tokopedia.transaction.bcaoneklik.domain;


import com.tokopedia.transaction.bcaoneklik.model.PaymentSettingModel;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.PaymentListModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModel;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;

/**
 * Created by kris on 10/4/17. Tokopedia
 */

public class ListPaymentRepository implements IListPaymentRepository{


    @Override
    public Observable<PaymentSettingModel> getListOfPayment(
            Observable<CreditCardModel> creditCardModelObservable,
            Observable<PaymentListModel> bcaOneClickListObservable
    ) {
        return Observable.zip(
                bcaOneClickListObservable,
                creditCardModelObservable,
                new Func2<PaymentListModel, CreditCardModel , PaymentSettingModel>() {
            @Override
            public PaymentSettingModel call(PaymentListModel paymentListModel,
                                            CreditCardModel creditCardModel) {
                PaymentSettingModel model = new PaymentSettingModel();
                model.setCreditCardResponse(creditCardModel);
                model.setBcaOneClickModel(paymentListModel);
                return model;
            }
        });
    }
}
