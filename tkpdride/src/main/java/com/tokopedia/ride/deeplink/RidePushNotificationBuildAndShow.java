package com.tokopedia.ride.deeplink;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.utils.ActivitiesLifecycleCallbacks;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.activity.RideHomeActivity;
import com.tokopedia.ride.common.configuration.RideConfiguration;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.deeplink.di.RidePushDependencyInjection;
import com.tokopedia.ride.ontrip.domain.GetCurrentDetailRideRequestUseCase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;

/**
 * Created by alvarisi on 4/12/17.
 */

public class RidePushNotificationBuildAndShow {
    private static int mNotificationId = 001;
    private ActivitiesLifecycleCallbacks activitiesLifecycleCallbacks;
    private Gson gson;
    private GetCurrentDetailRideRequestUseCase getCurrentDetailRideRequestUseCase;

    public RidePushNotificationBuildAndShow(ActivitiesLifecycleCallbacks application) {
        activitiesLifecycleCallbacks = application;
        gson = new Gson();
        SessionHandler sessionHandler = new SessionHandler(application.getContext());
        String token = String.format("Bearer %s", sessionHandler.getAccessToken(application.getContext()));
        String userId = sessionHandler.getLoginID();
        RidePushDependencyInjection injection = new RidePushDependencyInjection();
        getCurrentDetailRideRequestUseCase = injection.provideGetCurrentDetailRideRequestUseCase(token, userId);
    }

    public void processReceivedNotification(Bundle bundle){
        RidePushNotification ridePushNotification = gson
                .fromJson(
                        convertBundleToJsonString(bundle),
                        RidePushNotification.class
                );

        String deviceId = GCMHandler.getRegistrationId(activitiesLifecycleCallbacks.getLiveActivityOrNull());
        String userId = SessionHandler.getLoginID(activitiesLifecycleCallbacks.getLiveActivityOrNull());
        String hash = md5(userId + "~" + deviceId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetCurrentDetailRideRequestUseCase.PARAM_REQUEST_ID, ridePushNotification.getRequestId());
        requestParams.putString(GetCurrentDetailRideRequestUseCase.PARAM_USER_ID, userId);
        requestParams.putString(GetCurrentDetailRideRequestUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(GetCurrentDetailRideRequestUseCase.PARAM_HASH, hash);
        requestParams.putString(GetCurrentDetailRideRequestUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(GetCurrentDetailRideRequestUseCase.PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        getCurrentDetailRideRequestUseCase.execute(requestParams, getSubscriber());

    }

    @NonNull
    private Subscriber<RideRequest> getSubscriber() {
        return new Subscriber<RideRequest>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(RideRequest rideRequest) {
                if (!activitiesLifecycleCallbacks.isAppOnBackground()) {
                    Activity activity = activitiesLifecycleCallbacks.getLiveActivityOrNull();
                    if (activity instanceof FcmReceiverUIForeground) {
                        FcmReceiverUIForeground fcmReceiverUIForeground = (FcmReceiverUIForeground) activity;
                        boolean targetScreen = fcmReceiverUIForeground.matchesTarget(AppScreen.SCREEN_RIDE_ONTRIP);
                        if (targetScreen) {
                            fcmReceiverUIForeground.onTargetNotification(Observable.just(rideRequest));
                        }
                    }
                }

                switch (rideRequest.getStatus()){
                    case "arriving":
                    case "accepted":
                        showRideAccepted(activitiesLifecycleCallbacks.getContext(), rideRequest);
                        break;
                    case "no_drivers_available":

                        break;
                    case "processing":

                        break;
                    case "in_progress":
                        break;
                    case "driver_canceled":
                        showDriverCancelledRide(activitiesLifecycleCallbacks.getContext());
                        break;
                    case "rider_canceled":
                        break;
                    case "completed":
                        break;
                }
            }
        };
    }

    public static void showFindingUberNotication(Context context) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(com.tokopedia.ride.R.drawable.ic_stat_notify)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), com.tokopedia.ride.R.drawable.crux_cabs_uber_ic))
                .setProgress(0, 0, true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("Finding your Uber");

        // Issue the notification here.
        // Sets an ID for the notification

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public static void showRideAccepted(Context context, RideRequest rideRequest) {
        // Create remote view and set bigContentView.
        RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                com.tokopedia.ride.R.layout.notification_remote_view_ride_accepted);
        remoteView.setImageViewUri(R.id.iv_car_details, Uri.parse(rideRequest.getVehicle().getPictureUrl()));
        remoteView.setImageViewUri(R.id.iv_driver_img, Uri.parse(rideRequest.getDriver().getPictureUrl()));
        remoteView.setTextViewText(R.id.tv_cab_name, String.format("%s %s", rideRequest.getVehicle().getMake(), rideRequest.getVehicle().getVehicleModel()));
        remoteView.setTextViewText(R.id.tv_cab_number, rideRequest.getVehicle().getLicensePlate());
        remoteView.setTextViewText(R.id.tv_driver_name, rideRequest.getDriver().getName());
        remoteView.setTextViewText(R.id.tv_driver_star, String.format("%s star", rideRequest.getDriver().getRating()));

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(com.tokopedia.ride.R.drawable.ic_stat_notify)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), com.tokopedia.ride.R.drawable.qc_launcher))
                .setContentTitle("Your Uber is arriving now")
                .setContentText(String.format("%s (%s stars) will pick you up in %s minutes.",
                        rideRequest.getDriver().getName(),
                        rideRequest.getDriver().getRating(),
                        String.valueOf(rideRequest.getEta()))
                )
                .setCustomBigContentView(remoteView);

        Bundle bundle = new Bundle();
        bundle.putString(RideHomeActivity.EXTRA_REQUEST_ID, rideRequest.getRequestId());
        TaskStackBuilder stackBuilder = RideHomeActivity.getCallingApplinkTaskStack(context, bundle);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public static void showDriverCancelledRide(Context context) {
        RideConfiguration rideConfiguration = new RideConfiguration();
        rideConfiguration.clearActiveRequest();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(com.tokopedia.ride.R.drawable.ic_stat_notify)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), com.tokopedia.ride.R.drawable.qc_launcher))
                .setContentTitle("Driver cancelled your booking")
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText("Book another Uber");

        Bundle bundle = new Bundle();
        TaskStackBuilder stackBuilder = RideHomeActivity.getCallingApplinkTaskStack(context, bundle);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public static void showRideCompleted(Context context) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(com.tokopedia.ride.R.drawable.ic_stat_notify)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), com.tokopedia.ride.R.drawable.qc_launcher))
                .setContentTitle("Trip Completed")
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText("IDR 9,500");

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public static void showNoDriverFoundNotification(Context context) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(com.tokopedia.ride.R.drawable.ic_stat_notify)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), com.tokopedia.ride.R.drawable.qc_launcher))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("No Driver Found")
                .setContentText("Sorry no driver found immediately, you can try again");

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    protected String convertBundleToJsonString(Bundle bundle) {
        JSONObject json = new JSONObject();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                json.put(key, bundle.getString(key));
            } catch (JSONException e) {
                return null;
            }
        }
        return json.toString();
    }
}
