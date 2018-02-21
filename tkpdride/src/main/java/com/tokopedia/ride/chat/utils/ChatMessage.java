package com.tokopedia.ride.chat.utils;

import android.text.format.DateFormat;

import java.util.concurrent.TimeUnit;

/**
 * Created by sachinbansal on 2/13/18.
 */

public class ChatMessage {
    private String message;
    private long timestamp;
    private Type type;
    private DeliveryStatus deliveryStatus;
    private int id;

    public ChatMessage() {

    }

    public ChatMessage(String message, long timestamp, Type type) {
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFormattedTime() {

//        long oneDayInMillis = TimeUnit.DAYS.toMillis(1); // 24 * 60 * 60 * 1000;
//        long timeDifference = System.currentTimeMillis() - timestamp;

        return DateFormat.format("hh:mm a", timestamp).toString();


        /*return timeDifference < oneDayInMillis
                ? DateFormat.format("hh:mm a", timestamp).toString()
                : DateFormat.format("dd MMM - hh:mm a", timestamp).toString();*/
    }

    public enum Type {
        SENT, RECEIVED
    }

    public enum DeliveryStatus {
        SENT_FAILURE, SENT_SUCCESS, DELIVER_SUCCESS, DELIVER_FAILURE, PENDING
    }
}

