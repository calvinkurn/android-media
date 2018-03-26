package com.tokopedia.tkpdstream.chatroom.domain.pojo.sprintsale;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 3/26/18.
 */

public class UpcomingSprintSalePojo {

    @SerializedName("upcoming_flashsale")
    @Expose
    private UpcomingFlashsale upcomingFlashsale;

    public UpcomingFlashsale getUpcomingFlashsale() {
        return upcomingFlashsale;
    }

    public void setUpcomingFlashsale(UpcomingFlashsale upcomingFlashsale) {
        this.upcomingFlashsale = upcomingFlashsale;
    }

}
