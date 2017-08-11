package com.tokopedia.transaction.bcaoneklik.presenter;

import android.content.Context;

import com.tokopedia.transaction.bcaoneklik.model.BcaOneClickRegisterData;
import com.tokopedia.transaction.bcaoneklik.model.PaymentListModel;

import rx.Subscriber;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public interface BcaOneClickEditPresenter {

    void editUserDataBca(Context context,
                         BcaOneClickRegisterData data,
                         Subscriber<PaymentListModel> subscriber);

    void onDestroy();
}
