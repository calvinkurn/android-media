package com.tokopedia.seller.goldmerchant.statistic.view.helper.model;

import com.tokopedia.core.util.Pair;

/**
 * Created by normansyahputa on 7/10/17.
 */

public class GMDateRangeDateViewModel {
    Pair<Long, String> startDate;
    Pair<Long, String> endDate;

    public GMDateRangeDateViewModel() {

    }

    public GMDateRangeDateViewModel(GMDateRangeDateViewModel gmDateRangeDateViewModel) {
        setStartDate(new Pair<>(
                gmDateRangeDateViewModel.getStartDate().getModel1(),
                gmDateRangeDateViewModel.getStartDate().getModel2()));

        setEndDate(new Pair<>(
                gmDateRangeDateViewModel.getEndDate().getModel1(),
                gmDateRangeDateViewModel.getEndDate().getModel2()));
    }

    public Pair<Long, String> getStartDate() {
        return startDate;
    }

    public void setStartDate(Pair<Long, String> startDate) {
        this.startDate = startDate;
    }

    public Pair<Long, String> getEndDate() {
        return endDate;
    }

    public void setEndDate(Pair<Long, String> endDate) {
        this.endDate = endDate;
    }
}
