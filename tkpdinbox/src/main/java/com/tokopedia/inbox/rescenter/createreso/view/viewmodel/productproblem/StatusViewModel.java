package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public class StatusViewModel {
    public boolean delivered;
    public String name;
    public List<StatusTroubleViewModel> trouble;
    public StatusInfoViewModel info;


    public StatusViewModel(boolean delivered, String name, List<StatusTroubleViewModel> trouble, StatusInfoViewModel info) {
        this.delivered = delivered;
        this.name = name;
        this.trouble = trouble;
        this.info = info;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StatusTroubleViewModel> getTrouble() {
        return trouble;
    }

    public void setTrouble(List<StatusTroubleViewModel> trouble) {
        this.trouble = trouble;
    }

    public StatusInfoViewModel getInfo() {
        return info;
    }

    public void setInfo(StatusInfoViewModel info) {
        this.info = info;
    }
}
