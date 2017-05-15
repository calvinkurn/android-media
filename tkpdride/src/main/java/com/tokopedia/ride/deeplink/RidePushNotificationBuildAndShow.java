package com.tokopedia.ride.deeplink;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.activity.RideHomeActivity;
import com.tokopedia.ride.common.configuration.RideConfiguration;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.completetrip.view.CompleteTripActivity;
import com.tokopedia.ride.deeplink.di.RidePushDependencyInjection;
import com.tokopedia.ride.ontrip.domain.GetRideRequestDetailUseCase;
import com.tokopedia.ride.ontrip.domain.GetRideRequestMapUseCase;
import com.tokopedia.ride.ontrip.view.OnTripActivity;
import com.tokopedia.ride.ontrip.view.viewmodel.DriverVehicleAddressViewModel;

import java.util.Date;

import rx.Subscriber;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;

/**
 * Created by alvarisi on 4/12/17.
 */

public class RidePushNotificationBuildAndShow {
    private static int mNotificationId = 004;
    public static final int FINDING_UBER_NOTIFICATION_ID = mNotificationId;
    public static final int ACCEPTED_UBER_NOTIFICATION_ID = mNotificationId;
    private Context mContext;
    private Gson gson;
    private GetRideRequestDetailUseCase getRideRequestDetailUseCase;
    public static final String ACTION_DONE = "RidePushNotificationBuildAndShow#ACTION_DONE";
    public static final String EXTRA_RIDE_RIDE_REQUEST = "EXTRA_RIDE_RIDE_REQUEST";

    public RidePushNotificationBuildAndShow(Context context) {
        mContext = context;
        gson = new Gson();
        SessionHandler sessionHandler = new SessionHandler(mContext);
        String token = String.format("Bearer %s", sessionHandler.getAccessToken(mContext));
        String userId = sessionHandler.getLoginID();
        RidePushDependencyInjection injection = new RidePushDependencyInjection();
        getRideRequestDetailUseCase = injection.provideGetCurrentDetailRideRequestUseCase(token, userId);
    }

    public void processReceivedNotification(Bundle bundle) {
        String processableResp = bundle.getString(Constants.ARG_NOTIFICATION_DESCRIPTION, null);
        if (processableResp == null) return;
        RidePushNotification ridePushNotification = gson
                .fromJson(
                        processableResp,
                        RidePushNotification.class
                );

        String deviceId = GCMHandler.getRegistrationId(mContext);
        String userId = SessionHandler.getLoginID(mContext);
        String hash = md5(userId + "~" + deviceId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_REQUEST_ID, ridePushNotification.getRequestId());
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_USER_ID, userId);
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_HASH, hash);
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        getRideRequestDetailUseCase.execute(requestParams, getSubscriber());

        Log.d("Push", "RidePushNotificationBuildAndShow processReceivedNotification :: " + ridePushNotification.getRequestId());
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
                Intent intent = new Intent();
                intent.setAction(ACTION_DONE);
                intent.putExtra(EXTRA_RIDE_RIDE_REQUEST, rideRequest);
                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mContext);
                manager.sendBroadcast(intent);

                Log.d("Push", "RidePushNotificationBuildAndShow Subscriber OnNext :: " + rideRequest.getStatus());

                switch (rideRequest.getStatus()) {
                    case RideStatus.ARRIVING:
                    case RideStatus.ACCEPTED:
                        showRideAccepted(mContext, rideRequest);
                        break;
                    case RideStatus.NO_DRIVER_AVAILABLE:
                        showNoDriverFoundNotification(mContext);
                        break;
                    case RideStatus.PROCESSING:

                        break;
                    case RideStatus.IN_PROGRESS:
                        break;
                    case RideStatus.DRIVER_CANCELED:
                        showDriverCancelledRide(mContext);
                        break;
                    case RideStatus.RIDER_CANCELED:
                        break;
                    case RideStatus.COMPLETED:
                        showRideCompleted(mContext, rideRequest);
                        break;
                }
            }
        };
    }

    public static void showRideAccepted(final Context context, final RideRequest rideRequest) {
        RideConfiguration rideConfiguration = new RideConfiguration(context);
        rideConfiguration.setActiveRequestId(rideRequest.getRequestId());
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
                                              if (rideRequest.isShared()) {
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
                                                          Intent shareIntent = new Intent(context.getResources().getString(R.string.broadcast_share_eta));
                                                          shareIntent.putExtra("share", url);
                                                          PendingIntent sharePendingIntent = PendingIntent.getBroadcast(context, 0, shareIntent, 0);
                                                          remoteView.setOnClickPendingIntent(R.id.layout_share_eta, sharePendingIntent);

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

                                                          Intent callIntent = new Intent(context.getResources().getString(R.string.broadcast_call_driver));
                                                          callIntent.putExtra("telp", rideRequest.getDriver().getPhoneNumber());
                                                          PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, callIntent, 0);
                                                          remoteView.setOnClickPendingIntent(R.id.layout_call_driver, pendingSwitchIntent);

                                                          Bundle bundle = new Bundle();
                                                          bundle.putParcelable(OnTripActivity.EXTRA_RIDE_REQUEST, rideRequest);
                                                          bundle.putBoolean(Constants.EXTRA_FROM_PUSH, true);
                                                          bundle.putString(RideStatus.KEY, RideStatus.ACCEPTED);
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
                                                          .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE)
                                                          .setPriority(Notification.PRIORITY_MAX)
                                                          .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.qc_launcher))
                                                          .setContentTitle(context.getString(R.string.ride_push_driver_arriving_now))
                                                          .setContentText(String.format("%s (%s stars) will pick you up in %s minutes.",
                                                                  rideRequest.getDriver().getName(),
                                                                  rideRequest.getDriver().getRating(),
                                                                  String.valueOf(rideRequest.getEta()))
                                                          )
                                                          .setCustomBigContentView(remoteView);


                                                  Intent callIntent = new Intent(context.getResources().getString(R.string.broadcast_call_driver));
                                                  callIntent.putExtra("telp", rideRequest.getDriver().getPhoneNumber());
                                                  PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, callIntent, 0);

                                                  remoteView.setOnClickPendingIntent(R.id.layout_call_driver, pendingSwitchIntent);

                                                  Bundle bundle = new Bundle();
                                                  bundle.putParcelable(OnTripActivity.EXTRA_RIDE_REQUEST, rideRequest);
                                                  bundle.putBoolean(Constants.EXTRA_FROM_PUSH, true);
                                                  bundle.putString(RideStatus.KEY, RideStatus.ACCEPTED);
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
        bundle.putParcelable(OnTripActivity.EXTRA_RIDE_REQUEST, rideRequest);
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
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_notify)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.qc_launcher))
                .setContentTitle(context.getString(R.string.ride_push_driver_cancel))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText(context.getString(R.string.ride_push_book_request_again));

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.EXTRA_FROM_PUSH, true);
        TaskStackBuilder stackBuilder = RideHomeActivity.getCallingTaskStask(context, bundle);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public static void showFindingUberNotication(Context context) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_notify)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.qc_launcher))
                .setProgress(0, 0, true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(context.getResources().getString(R.string.notification_title_finding_uber));

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.EXTRA_FROM_PUSH, true);
        TaskStackBuilder stackBuilder = RideHomeActivity.getCallingTaskStask(context, bundle);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private static void showRideCompleted(Context context, RideRequest rideRequest) {
        DriverVehicleAddressViewModel driverAndVehicle = new DriverVehicleAddressViewModel();
        driverAndVehicle.setDriver(rideRequest.getDriver());
        driverAndVehicle.setVehicle(rideRequest.getVehicle());
        driverAndVehicle.setAddress(rideRequest.getAddress());

        Intent intent = CompleteTripActivity.getCallingIntentFromPushNotification(
                context,
                rideRequest.getRequestId(),
                driverAndVehicle
        );

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_notify)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.qc_launcher))
                .setContentTitle(context.getString(R.string.ride_push_toolbar_trip_completed))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText(context.getString(R.string.ride_push_toolbar_tap_view_detail));

        mBuilder.setContentIntent(pendingIntent);
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private static void showNoDriverFoundNotification(Context context) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_notify)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.qc_launcher))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(context.getString(R.string.ride_push_toolbar_driver_not_found))
                .setContentText(context.getString(R.string.ride_push_toolbar_driver_not_found_desc));

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.EXTRA_FROM_PUSH, true);
        TaskStackBuilder stackBuilder = RideHomeActivity.getCallingTaskStask(context, bundle);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public static void cancelActiveNotification(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(mNotificationId);
    }
}
