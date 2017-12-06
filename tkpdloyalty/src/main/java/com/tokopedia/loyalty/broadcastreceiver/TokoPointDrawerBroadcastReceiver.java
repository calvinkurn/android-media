package com.tokopedia.loyalty.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.loyalty.di.component.DaggerTokoPointBroadcastComponent;
import com.tokopedia.loyalty.di.component.TokoPointBroadcastComponent;
import com.tokopedia.loyalty.di.module.ServiceApiModule;
import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 04/12/17.
 */

public class TokoPointDrawerBroadcastReceiver extends BroadcastReceiver {
    private CompositeSubscription compositeSubscription;
    @Inject
    ITokoPointRepository tokoplusRepository;

    @Override
    public void onReceive(Context context, Intent intent) {

        TokoPointBroadcastComponent tokoPointBroadcastComponent = DaggerTokoPointBroadcastComponent
                .builder()
                .serviceApiModule(new ServiceApiModule())
                .build();
        tokoPointBroadcastComponent.inject(this);


        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(
                tokoplusRepository.getPointDrawer(AuthUtil.generateParamsNetwork(context))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<TokoPointDrawerData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(TokoPointDrawerData topPointDrawerData) {
                                Log.d("TokoPointDrawerBR", topPointDrawerData.toString());
                            }
                        })
        );
    }
}
