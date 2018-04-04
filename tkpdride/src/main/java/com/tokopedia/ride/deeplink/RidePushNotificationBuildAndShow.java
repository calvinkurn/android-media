package com.tokopedia.ride.deeplink;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.utils.NotificationChannelId;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.activity.RideHomeActivity;
import com.tokopedia.ride.common.configuration.RideConfiguration;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.common.ride.domain.model.RideRequestAddress;
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
    public static final int FINDING_UBER_NOTIFICATION_ID = 004;
    public static final int ACCEPTED_UBER_NOTIFICATION_ID = 005;
    public static final int DRIVER_CANCELLED_UBER_NOTIFICATION_ID = 006;
    public static final int COMPLETED_UBER_NOTIFICATION_ID = 007;
    public static final int NO_DRIVER_FOUND_UBER_NOTIFICATION_ID = 007;

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

        CommonUtils.dumper("RidePushNotificationBuildAndShow processReceivedNotification :: " + ridePushNotification.getRequestId());
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

                CommonUtils.dumper("RidePushNotificationBuildAndShow Subscriber OnNext :: " + rideRequest.getStatus());

                switch (rideRequest.getStatus()) {
                    case RideStatus.ARRIVING:
                    case RideStatus.ACCEPTED:
                        cancelActiveNotification(mContext);
                        showRideAccepted(mContext, rideRequest);
                        break;
                    case RideStatus.NO_DRIVER_AVAILABLE:
                        cancelActiveNotification(mContext);
                        showNoDriverFoundNotification(mContext);
                        break;
                    case RideStatus.PROCESSING:
                        break;
                    case RideStatus.IN_PROGRESS:
                        cancelActiveNotification(mContext);
                        showInProgressRide(mContext, rideRequest);
                        break;
                    case RideStatus.DRIVER_CANCELED:
                        cancelActiveNotification(mContext);
                        showDriverCancelledRide(mContext);
                        break;
                    case RideStatus.RIDER_CANCELED:
                        cancelActiveNotification(mContext);
                        break;
                    case RideStatus.COMPLETED:
                        if (rideRequest.getPayment() != null && rideRequest.getPayment().isReceiptReady()) {
                            cancelActiveNotification(mContext);
                            showRideCompleted(mContext, rideRequest);
                        }
                        break;
                }
            }
        };
    }

    public static void showRideAccepted(final Context context, final RideRequest rideRequest) {
        final RideConfiguration rideConfiguration = new RideConfiguration(context);
        rideConfiguration.setActiveRequestId(rideRequest.getRequestId());
        // Create remote view and set bigContentView.
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                com.tokopedia.ride.R.layout.notification_remote_view_ride_accepted);

        remoteView.setTextViewText(R.id.tv_cab_name, String.format("%s %s", rideRequest.getVehicle().getMake(), rideRequest.getVehicle().getVehicleModel()));
        remoteView.setTextViewText(R.id.tv_cab_number, rideRequest.getVehicle().getLicensePlate());
        remoteView.setTextViewText(R.id.tv_driver_name, rideRequest.getDriver().getName());
        remoteView.setTextViewText(R.id.tv_driver_star, String.format("%s", rideRequest.getDriver().getRating()));
        remoteView.setTextViewText(R.id.tv_eta, String.format(String.format("%s will pick you up in %s minutes.",
                rideRequest.getDriver().getName(),
                String.valueOf(Math.round(rideRequest.getPickup().getEta())))
                )
        );
        Glide
                .with(context)
                .load(rideRequest.getDriver().getPictureUrl())
                .asBitmap()
                .error(R.drawable.cabs_uber_ic)
                .into(new SimpleTarget<Bitmap>(100, 100) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        remoteView.setImageViewBitmap(R.id.iv_driver_img, ImageHandler.getRoundedCornerBitmap(resource, 100));
                        getVehicleImageAndShowIt(context, rideRequest, remoteView, rideConfiguration);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        remoteView.setImageViewResource(R.id.iv_driver_img, R.drawable.cabs_uber_ic);
                        getVehicleImageAndShowIt(context, rideRequest, remoteView, rideConfiguration);
                    }
                });

    }

    private static void getVehicleImageAndShowIt(final Context context, final RideRequest rideRequest, final RemoteViews remoteView, final RideConfiguration rideConfiguration) {
        Glide
                .with(context)
                .load(rideRequest.getVehicle().getPictureUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(100, 100) {
                          @Override
                          public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                              remoteView.setImageViewBitmap(R.id.iv_car_details, ImageHandler.getRoundedCornerBitmap(resource, 100));
                              getTripMapUrlAndShowIt(remoteView, context, rideRequest, rideConfiguration);
                          }

                          @Override
                          public void onLoadFailed(Exception e, Drawable errorDrawable) {
                              super.onLoadFailed(e, errorDrawable);
                              remoteView.setImageViewResource(R.id.iv_car_details, R.drawable.cabs_uber_ic);
                              getTripMapUrlAndShowIt(remoteView, context, rideRequest, rideConfiguration);
                          }
                      }
                );
    }

    private static void getTripMapUrlAndShowIt(final RemoteViews remoteView, final Context context, final RideRequest rideRequest, final RideConfiguration rideConfiguration) {
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
                shareIntent.putExtra("riderequest", rideRequest);
                PendingIntent sharePendingIntent = PendingIntent.getBroadcast(context, 0, shareIntent, 0);
                remoteView.setOnClickPendingIntent(R.id.layout_share_eta, sharePendingIntent);

                //create title
                String title = "";
                if (rideRequest.getStatus().equalsIgnoreCase(RideStatus.ACCEPTED)) {
                    title = context.getString(R.string.ride_push_title_template_accepted);
                } else {
                    title = context.getString(R.string.ride_push_title_template_arriving);
                }

                String activeProductName = rideConfiguration.getActiveProductName();
                if (TextUtils.isEmpty(activeProductName)) {
                    activeProductName = "Uber";
                }

                title = String.format(title, activeProductName);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NotificationChannelId.GENERAL)
                        .setSmallIcon(R.drawable.ic_stat_notify_white)
                        .setAutoCancel(true)
                        .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.qc_launcher))
                        .setContentTitle(title)
                        .setContentText(String.format("%s (%s stars) will pick you up in %s minutes.",
                                rideRequest.getDriver().getName(),
                                rideRequest.getDriver().getRating(),
                                String.valueOf(Math.round(rideRequest.getPickup().getEta())))
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
                mNotifyMgr.notify(ACCEPTED_UBER_NOTIFICATION_ID, mBuilder.build());
            }
        });
    }

    public static void showDriverCancelledRide(Context context) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NotificationChannelId.GENERAL)
                .setSmallIcon(R.drawable.ic_stat_notify_white)
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
        mNotifyMgr.notify(DRIVER_CANCELLED_UBER_NOTIFICATION_ID, mBuilder.build());
    }

    public static void showInProgressRide(Context context, final RideRequest rideRequest) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NotificationChannelId.GENERAL)
                .setSmallIcon(R.drawable.ic_stat_notify_white)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.qc_launcher))
                .setContentTitle(context.getString(R.string.ride_push_in_progress))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText(context.getString(R.string.ride_push_track_ride));

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
        mNotifyMgr.notify(DRIVER_CANCELLED_UBER_NOTIFICATION_ID, mBuilder.build());
    }

    public static void showFindingUberNotication(Context context) {
        String title = context.getResources().getString(R.string.notification_title_finding_uber);
        RideConfiguration rideConfiguration = new RideConfiguration(context);
        if (!TextUtils.isEmpty(rideConfiguration.getActiveProductName())) {
            title += String.format(" %s", rideConfiguration.getActiveProductName());
        } else {
            title += " Uber";
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NotificationChannelId.GENERAL)
                .setSmallIcon(R.drawable.ic_stat_notify_white)
                .setAutoCancel(false)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.qc_launcher))
                .setProgress(0, 0, true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(title);

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.EXTRA_FROM_PUSH, true);
        TaskStackBuilder stackBuilder = RideHomeActivity.getCallingTaskStask(context, bundle);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(FINDING_UBER_NOTIFICATION_ID, mBuilder.build());
    }

    private static void showRideCompleted(Context context, RideRequest rideRequest) {
        RideConfiguration configuration = new RideConfiguration(context);
        configuration.clearActiveProductName();
        DriverVehicleAddressViewModel driverAndVehicle = new DriverVehicleAddressViewModel();
        driverAndVehicle.setDriver(rideRequest.getDriver());
        driverAndVehicle.setVehicle(rideRequest.getVehicle());
        RideRequestAddress rideRequestAddress = new RideRequestAddress();
        rideRequestAddress.setStartAddressName(rideRequest.getPickup().getAddressName());
        rideRequestAddress.setStartAddress(rideRequest.getPickup().getAddress());
        rideRequestAddress.setEndAddressName(rideRequest.getDestination().getAddressName());
        rideRequestAddress.setEndAddress(rideRequest.getDestination().getAddress());
        driverAndVehicle.setAddress(rideRequestAddress);

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

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NotificationChannelId.GENERAL)
                .setSmallIcon(R.drawable.ic_stat_notify_white)
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
        mNotifyMgr.notify(COMPLETED_UBER_NOTIFICATION_ID, mBuilder.build());
    }

    private static void showNoDriverFoundNotification(Context context) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NotificationChannelId.GENERAL)
                .setSmallIcon(R.drawable.ic_stat_notify_white)
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
        mNotifyMgr.notify(NO_DRIVER_FOUND_UBER_NOTIFICATION_ID, mBuilder.build());
    }

    public static void cancelActiveNotification(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(FINDING_UBER_NOTIFICATION_ID);
        notificationManager.cancel(ACCEPTED_UBER_NOTIFICATION_ID);
        notificationManager.cancel(COMPLETED_UBER_NOTIFICATION_ID);
        notificationManager.cancel(DRIVER_CANCELLED_UBER_NOTIFICATION_ID);
        notificationManager.cancel(NO_DRIVER_FOUND_UBER_NOTIFICATION_ID);
    }
}
