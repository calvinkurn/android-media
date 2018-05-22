package com.tokopedia.tokocash.autosweepmf.data.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Base model class for all server response
 */
public class ResponseContainer {
    @SerializedName("message")
    @Expose
    protected String message;
    @SerializedName("error")
    @Expose
    protected String error;
    @SerializedName("code")
    @Expose
    protected int code;
    @SerializedName("latency")
    @Expose
    protected String latency;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLatency() {
        return latency;
    }

    public void setLatency(String latency) {
        this.latency = latency;
    }

    @Override
    public String toString() {
        return "ResponseContainer{" +
                "message='" + message + '\'' +
                ", error='" + error + '\'' +
                ", code=" + code +
                ", latency='" + latency + '\'' +
                '}';
    }
}
