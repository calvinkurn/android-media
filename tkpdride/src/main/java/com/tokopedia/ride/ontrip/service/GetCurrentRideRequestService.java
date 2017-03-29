package com.tokopedia.ride.ontrip.service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.ontrip.di.OnTripDependencyInjection;
import com.tokopedia.ride.ontrip.domain.CreateRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.GetCurrentDetailRideRequestUseCase;
import com.tokopedia.ride.ontrip.view.OnTripActivity;
import com.tokopedia.ride.ontrip.view.fragment.OnTripMapFragment;

import java.util.Date;

import rx.Subscriber;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;

/**
 * Created by alvarisi on 3/29/17.
 */

public class GetCurrentRideRequestService extends GcmTaskService {
    GetCurrentDetailRideRequestUseCase useCase;
    public static final String EXTRA_RESULT = "extra_result";
    public static final String ACTION_DONE = "GcmTaskService#ACTION_DONE";

    @Override
    public int onRunTask(TaskParams taskParams) {
        Bundle bundle = taskParams.getExtras();
        String requestId = bundle.getString(OnTripMapFragment.EXTRA_RIDE_REQUEST_RESULT);

        useCase = OnTripDependencyInjection.createGetDetailUseCase(getApplicationContext());
        String deviceId = GCMHandler.getRegistrationId(getApplicationContext());
        String userId = SessionHandler.getLoginID(getApplicationContext());
        String hash = md5(userId + "~" + deviceId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetCurrentDetailRideRequestUseCase.PARAM_REQUEST_ID, requestId);
        requestParams.putString(GetCurrentDetailRideRequestUseCase.PARAM_USER_ID, userId);
        requestParams.putString(GetCurrentDetailRideRequestUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(GetCurrentDetailRideRequestUseCase.PARAM_HASH, hash);
        requestParams.putString(GetCurrentDetailRideRequestUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(GetCurrentDetailRideRequestUseCase.PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        useCase.execute(requestParams, new Subscriber<RideRequest>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("GetCurrentRideRequestService complete");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("GetCurrentRideRequestService error");
            }

            @Override
            public void onNext(RideRequest s) {
                CommonUtils.dumper("GetCurrentRideRequestService : " + s);
                Intent intent = new Intent();
                intent.setAction(ACTION_DONE);
                intent.putExtra(EXTRA_RESULT, s);
                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(GetCurrentRideRequestService.this.getApplicationContext());
                manager.sendBroadcast(intent);
            }
        });

        return GcmNetworkManager.RESULT_SUCCESS;
    }
}
