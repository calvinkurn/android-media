package com.tokopedia.loyalty.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.drawer2.data.viewmodel.TopPointDrawerData;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 04/12/17.
 */

public class TokoPointDrawerBroadcastReceiver extends BroadcastReceiver {
    private CompositeSubscription compositeSubscription;
    ITokoPointRepository tokoplusRepository;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        compositeSubscription.add(
                tokoplusRepository.getPointDrawer(param)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<TopPointDrawerData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(TopPointDrawerData topPointDrawerData) {

                            }
                        })
        );
    }
}
