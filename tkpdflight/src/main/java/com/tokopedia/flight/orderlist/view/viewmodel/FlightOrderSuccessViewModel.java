package com.tokopedia.flight.orderlist.view.viewmodel;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderTypeFactory;

/**
 * @author by alvarisi on 12/12/17.
 */

public class FlightOrderSuccessViewModel implements Visitable<FlightOrderTypeFactory> {
    private String id;
    @StringRes
    private int title;
    private String createTime;
    private int status;
    private FlightOrderJourney orderJourney;

    public FlightOrderSuccessViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public FlightOrderJourney getOrderJourney() {
        return orderJourney;
    }

    public void setOrderJourney(FlightOrderJourney orderJourney) {
        this.orderJourney = orderJourney;
    }

    @Override
    public int type(FlightOrderTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(@StringRes int title) {
        this.title = title;
    }
}
