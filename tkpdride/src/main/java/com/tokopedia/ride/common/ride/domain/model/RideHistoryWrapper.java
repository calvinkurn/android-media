package com.tokopedia.ride.common.ride.domain.model;

import com.tokopedia.ride.bookingride.domain.model.Paging;
import com.tokopedia.ride.history.domain.model.RideHistory;

import java.util.List;

/**
 * Created by alvarisi on 7/19/17.
 */

public class RideHistoryWrapper {
    private List<RideHistory> histories;
    private Paging paging;

    public List<RideHistory> getHistories() {
        return histories;
    }

    public void setHistories(List<RideHistory> histories) {
        this.histories = histories;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }


}
