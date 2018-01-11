package com.tokopedia.seller.opportunity.presenter.subscriber;

import android.util.Log;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.seller.opportunity.listener.OpportunityView;

import rx.Subscriber;

/**
 * Created by normansyahputa on 1/11/18.
 */

public class NewPriceInfoSubscriber extends Subscriber<String> {
    private OpportunityView view;
    private static final String TAG = "NewPriceInfoSubscriber";

    public NewPriceInfoSubscriber(OpportunityView view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.onErrorTakeOpportunity(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(String s) {
        Log.i(TAG, TAG+"--> "+s);
    }
}
