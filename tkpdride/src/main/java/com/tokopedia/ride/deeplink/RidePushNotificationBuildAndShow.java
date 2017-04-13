package com.tokopedia.ride.deeplink;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.utils.ActivitiesLifecycleCallbacks;
import com.tokopedia.core.review.var.Const;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.activity.RideHomeActivity;
import com.tokopedia.ride.common.configuration.RideConfiguration;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.deeplink.di.RidePushDependencyInjection;
import com.tokopedia.ride.ontrip.domain.GetCurrentDetailRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.GetRideRequestMapUseCase;
import com.tokopedia.ride.ontrip.receiver.CallDriverReceiver;
import com.tokopedia.ride.ontrip.view.OnTripActivity;

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
        String processableResp = bundle.getString(Constants.ARG_NOTIFICATION_DESCRIPTION, null);
        if (processableResp == null) return;
        RidePushNotification ridePushNotification = gson
                .fromJson(
                        processableResp,
                        RidePushNotification.class
                );

        String deviceId = GCMHandler.getRegistrationId(activitiesLifecycleCallbacks.getContext());
        String userId = SessionHandler.getLoginID(activitiesLifecycleCallbacks.getContext());
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
                        showNoDriverFoundNotification(activitiesLifecycleCallbacks.getContext());
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
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private static void showRideAccepted(final Context context, final RideRequest rideRequest) {
        // Create remote view and set bigContentView.
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                com.tokopedia.ride.R.layout.notification_remote_view_ride_accepted);

        remoteView.setTextViewText(R.id.tv_cab_name, String.format("%s %s", rideRequest.getVehicle().getMake(), rideRequest.getVehicle().getVehicleModel()));
        remoteView.setTextViewText(R.id.tv_cab_number, rideRequest.getVehicle().getLicensePlate());
        remoteView.setTextViewText(R.id.tv_driver_name, rideRequest.getDriver().getName());
        remoteView.setTextViewText(R.id.tv_driver_star, String.format("%s star", rideRequest.getDriver().getRating()));
        Glide
                .with(context)
                .load(rideRequest.getVehicle().getPictureUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(100, 100) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        remoteView.setImageViewBitmap(R.id.iv_driver_img, ImageHandler.getRoundedCornerBitmap(resource, 100));
                        Glide
                                .with(context)
                                .load(rideRequest.getDriver().getPictureUrl())
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>(100, 100) {
                                          @Override
                                          public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                              remoteView.setImageViewBitmap(R.id.iv_car_details, ImageHandler.getRoundedCornerBitmap(resource, 100));
                                              if (rideRequest.isShared()){
                                                  remoteView.setViewVisibility(R.id.layout_share_eta, View.VISIBLE);

                                                  SessionHandler sessionHandler = new SessionHandler(context);
                                                  String token = String.format("Bearer %s", sessionHandler.getAccessToken(context));
                                                  String userId = sessionHandler.getLoginID();
                                                  RidePushDependencyInjection injection = new RidePushDependencyInjection();

                                                  GetRideRequestMapUseCase getRideRequestMapUseCase = injection.provideGetRideRequestMapUseCase(token, userId);

                                                  RequestParams requestParams = RequestParams.create();
                                                  requestParams.putString(GetRideRequestMapUseCase.PARAM_REQUEST_ID, rideRequest.getRequestId());

                                                  getRideRequestMapUseCase.execute(requestParams, new Subscriber<String>() {
                                                      @Override
                                                      public void onCompleted() {

                                                      }

                                                      @Override
                                                      public void onError(Throwable e) {

                                                      }

                                                      @Override
                                                      public void onNext(String url) {
                                                          NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                                                  .setSmallIcon(R.drawable.ic_stat_notify)
                                                                  .setAutoCancel(true)
                                                                  .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.qc_launcher))
                                                                  .setContentTitle("Your Uber is arriving now")
                                                                  .setContentText(String.format("%s (%s stars) will pick you up in %s minutes.",
                                                                          rideRequest.getDriver().getName(),
                                                                          rideRequest.getDriver().getRating(),
                                                                          String.valueOf(rideRequest.getEta()))
                                                                  )
                                                                  .setCustomBigContentView(remoteView);


                                                          Intent share = new Intent(Intent.ACTION_SEND);
                                                          share.setType("text/plain");
                                                          share.putExtra(Intent.EXTRA_SUBJECT, "Klik link ini untuk menemukan perjalanan anda dari Tokopedia.");
                                                          share.putExtra(Intent.EXTRA_TEXT, url);
                                                          context.startActivity(Intent.createChooser(share, "Bagikan Link!"));
                                                          PendingIntent sharePendingIntent = PendingIntent.getService(context, 0, share, PendingIntent.FLAG_UPDATE_CURRENT);
                                                          remoteView.setOnClickFillInIntent(R.id.layout_share_eta, share);



                                                          Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                                          callIntent.setData(Uri.parse("tel:" + rideRequest.getDriver().getPhoneNumber()));
                                                          remoteView.setOnClickFillInIntent(R.id.layout_call_driver, callIntent);

                                                          Bundle bundle = new Bundle();
                                                          bundle.putString(RideHomeActivity.EXTRA_REQUEST_ID, rideRequest.getRequestId());
                                                          bundle.putBoolean(Constants.EXTRA_FROM_PUSH, true);
                                                          TaskStackBuilder stackBuilder = OnTripActivity.getCallingApplinkTaskStack(context, bundle);

                                                          PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
                                                          mBuilder.setContentIntent(resultPendingIntent);

                                                          // Gets an instance of the NotificationManager service
                                                          NotificationManager mNotifyMgr =
                                                                  (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

                                                          // Builds the notification and issues it.
                                                          mNotifyMgr.notify(mNotificationId, mBuilder.build());
                                                      }
                                                  });
                                              } else {
                                                  remoteView.setViewVisibility(R.id.layout_share_eta, View.GONE);
                                                  NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                                          .setSmallIcon(R.drawable.ic_stat_notify)
                                                          .setAutoCancel(true)
                                                          .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.qc_launcher))
                                                          .setContentTitle("Your Uber is arriving now")
                                                          .setContentText(String.format("%s (%s stars) will pick you up in %s minutes.",
                                                                  rideRequest.getDriver().getName(),
                                                                  rideRequest.getDriver().getRating(),
                                                                  String.valueOf(rideRequest.getEta()))
                                                          )
                                                          .setCustomBigContentView(remoteView);


                                                  Intent callIntent = new Intent("CallDriverReceiver");
                                                  callIntent.putExtra("telp", rideRequest.getDriver().getPhoneNumber());
                                                  PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, callIntent, 0);

                                                  remoteView.setOnClickPendingIntent(R.id.layout_call_driver, pendingSwitchIntent);

                                                  Bundle bundle = new Bundle();
                                                  bundle.putString(RideHomeActivity.EXTRA_REQUEST_ID, rideRequest.getRequestId());
                                                  bundle.putBoolean(Constants.EXTRA_FROM_PUSH, true);
                                                  TaskStackBuilder stackBuilder = OnTripActivity.getCallingApplinkTaskStack(context, bundle);

                                                  PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
                                                  mBuilder.setContentIntent(resultPendingIntent);

                                                  // Gets an instance of the NotificationManager service
                                                  NotificationManager mNotifyMgr =
                                                          (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

                                                  // Builds the notification and issues it.
                                                  mNotifyMgr.notify(mNotificationId, mBuilder.build());
                                              }
                                          }
                                      }
                                );
                    }
                });
    }

    private static void executeAcceptedRideRequest(Context context, RideRequest rideRequest, RemoteViews remoteView) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_notify)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.qc_launcher))
                .setContentTitle("Your Uber is arriving now")
                .setContentText(String.format("%s (%s stars) will pick you up in %s minutes.",
                        rideRequest.getDriver().getName(),
                        rideRequest.getDriver().getRating(),
                        String.valueOf(rideRequest.getEta()))
                )
                .setCustomBigContentView(remoteView);

        Bundle bundle = new Bundle();
        bundle.putString(RideHomeActivity.EXTRA_REQUEST_ID, rideRequest.getRequestId());
        bundle.putBoolean(Constants.EXTRA_FROM_PUSH, true);
        TaskStackBuilder stackBuilder = OnTripActivity.getCallingApplinkTaskStack(context, bundle);

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
        bundle.putBoolean(Constants.EXTRA_FROM_PUSH, true);
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

    private static void showNoDriverFoundNotification(Context context) {
        RideConfiguration rideConfiguration = new RideConfiguration();
        rideConfiguration.clearActiveRequest();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(com.tokopedia.ride.R.drawable.ic_stat_notify)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), com.tokopedia.ride.R.drawable.qc_launcher))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("No Driver Found")
                .setContentText("Sorry no driver found immediately, you can try again");

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.EXTRA_FROM_PUSH, true);
        TaskStackBuilder stackBuilder = RideHomeActivity.getCallingApplinkTaskStack(context, bundle);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private String convertBundleToJsonString(Bundle bundle) {
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
