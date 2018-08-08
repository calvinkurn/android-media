package com.tokopedia.transaction.bcaoneklik.presenter;

import android.content.Context;

import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.BcaOneClickRegisterData;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.BcaOneClickSuccessRegisterData;

import rx.Subscriber;

/**
 * Created by kris on 7/25/17. Tokopedia
 */

public interface BcaOneClickPresenter {

    void addUserDataBca(Context context, BcaOneClickRegisterData data,
                        Subscriber<BcaOneClickSuccessRegisterData> subscriber);

    void onDestroy();

}
