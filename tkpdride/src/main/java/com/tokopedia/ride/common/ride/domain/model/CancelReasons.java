package com.tokopedia.ride.common.ride.domain.model;

import java.util.List;

/**
 * Created by alvarisi on 6/14/17.
 */

public class CancelReasons {
    private List<String> reasons;

    public CancelReasons() {
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }
}
