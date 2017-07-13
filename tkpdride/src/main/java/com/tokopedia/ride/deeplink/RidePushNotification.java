package com.tokopedia.ride.deeplink;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/12/17.
 */

public class RidePushNotification {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("desc")
    @Expose
    private String description;
    @SerializedName("request_id")
    @Expose
    private String requestId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("event_type")
    @Expose
    private String eventType;
    @SerializedName("event_time")
    @Expose
    private String eventTime;

    public RidePushNotification() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventTime() {
        return eventTime;
    }
}
