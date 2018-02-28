package com.tokopedia.profile.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nakama on 28/02/18.
 */

public class ProfileReputation {

    @SerializedName("active")
    @Expose
    private boolean active;
    @SerializedName("positive")
    @Expose
    private int positive;
    @SerializedName("neutral")
    @Expose
    private int neutral;
    @SerializedName("negative")
    @Expose
    private int negative;
    @SerializedName("percentage")
    @Expose
    private int percentage;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getPositive() {
        return positive;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }

    public int getNeutral() {
        return neutral;
    }

    public void setNeutral(int neutral) {
        this.neutral = neutral;
    }

    public int getNegative() {
        return negative;
    }

    public void setNegative(int negative) {
        this.negative = negative;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
