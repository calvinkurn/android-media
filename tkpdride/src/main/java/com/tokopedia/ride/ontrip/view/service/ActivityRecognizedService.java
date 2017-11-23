package com.tokopedia.ride.ontrip.view.service;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.ride.common.configuration.RideConfiguration;

import java.util.List;

/**
 * Created by Vishal Gupta 6th Sep, 2017
 */
public class ActivityRecognizedService extends IntentService {
    RideConfiguration configuration;

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
        configuration = new RideConfiguration(this);
    }

    public ActivityRecognizedService(String name) {
        super(name);
        configuration = new RideConfiguration(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities(result.getProbableActivities());
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        for (DetectedActivity activity : probableActivities) {
            switch (activity.getType()) {
                case DetectedActivity.IN_VEHICLE: {
                    CommonUtils.dumper("In Vehicle: " + activity.getConfidence());

                    if (activity.getConfidence() >= 75) {
                        saveDeviceInVehicle(true);
                    } else {
                        saveDeviceInVehicle(false);
                    }
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    CommonUtils.dumper("On Bicycle: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    CommonUtils.dumper("On Foot: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.RUNNING: {
                    CommonUtils.dumper("Running: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.STILL: {
                    CommonUtils.dumper("Still: " + activity.getConfidence());
                    if (activity.getConfidence() >= 75) {
                        saveDeviceInVehicle(false);
                    }
                    break;
                }
                case DetectedActivity.TILTING: {
                    CommonUtils.dumper("Tilting: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.WALKING: {
                    CommonUtils.dumper("Walking: " + activity.getConfidence());
                    if (activity.getConfidence() >= 75) {
                        saveDeviceInVehicle(false);
                    }
                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    CommonUtils.dumper("Unknown: " + activity.getConfidence());
                    break;
                }
            }
        }
    }

    private void saveDeviceInVehicle(boolean isDeviceInVehicle) {
        if (configuration != null) {
            configuration.saveDeviceInVehicle(isDeviceInVehicle);
        }
    }
}
