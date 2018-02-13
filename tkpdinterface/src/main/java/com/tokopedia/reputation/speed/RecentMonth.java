package com.tokopedia.reputation.speed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by normansyahputa on 2/13/18.
 */

public class RecentMonth {

    @SerializedName("speed_level")
    @Expose
    private long speedLevel;
    @SerializedName("speed_level_description")
    @Expose
    private String speedLevelDescription;

    public long getSpeedLevel() {
        return speedLevel;
    }

    public void setSpeedLevel(long speedLevel) {
        this.speedLevel = speedLevel;
    }

    public String getSpeedLevelDescription() {
        return speedLevelDescription;
    }

    public void setSpeedLevelDescription(String speedLevelDescription) {
        this.speedLevelDescription = speedLevelDescription;
    }

}
