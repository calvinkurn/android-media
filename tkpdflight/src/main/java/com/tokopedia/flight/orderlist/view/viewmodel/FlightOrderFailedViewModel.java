package com.tokopedia.flight.orderlist.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderTypeFactory;

import java.util.List;

/**
 * @author by alvarisi on 12/12/17.
 */

public class FlightOrderFailedViewModel implements Visitable<FlightOrderTypeFactory> {
    private String title;
    private String id;
    private String createTime;
    private int status;
    private List<FlightOrderJourney> orderJourney;

    public FlightOrderFailedViewModel() {
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

    public List<FlightOrderJourney> getOrderJourney() {
        return orderJourney;
    }

    public void setOrderJourney(List<FlightOrderJourney> orderJourney) {
        this.orderJourney = orderJourney;
    }

    @Override
    public int type(FlightOrderTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
