package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public class TokoPointHeaderResponse {

    @SerializedName("process_time")
    @Expose
    private double processTime;
    @SerializedName("message")
    @Expose
    private List<String> message = new ArrayList<>();
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("error_code")
    @Expose
    private String errorCode;

    public double getProcessTime() {
        return processTime;
    }

    public List<String> getMessage() {
        return message;
    }

    public String getReason() {
        return reason;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessageFormatted() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < message.size(); i++) {
            stringBuilder.append(message.get(i));
            if (i < message.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString().trim();
    }
}
