package com.tokopedia.loyalty.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tokopedia.core.drawer2.data.viewmodel.TopPointDrawerData;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
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
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("user_id", SessionHandler.getLoginID(context));
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
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(TopPointDrawerData topPointDrawerData) {
                                Log.d("TokoPointDrawerBR", topPointDrawerData.toString());
                            }
                        })
        );
    }
}
