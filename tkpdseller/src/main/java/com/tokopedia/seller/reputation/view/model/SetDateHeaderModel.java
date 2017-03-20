package com.tokopedia.seller.reputation.view.model;

import com.tokopedia.seller.topads.view.model.TypeBasedModel;

/**
 * Created by normansyahputa on 3/17/17.
 */

public class SetDateHeaderModel extends TypeBasedModel {
    public static final int TYPE = 1292832;
    String startDate;
    String endDate;

    public SetDateHeaderModel() {
        super(TYPE);
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
