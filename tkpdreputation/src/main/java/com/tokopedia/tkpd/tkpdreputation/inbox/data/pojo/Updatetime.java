
package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Updatetime {

    @SerializedName("Time")
    @Expose
    private String time;
    @SerializedName("Valid")
    @Expose
    private boolean valid;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

}
