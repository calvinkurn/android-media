package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

/**
 * Created by yoasfs on 14/08/17.
 */

public class StatusInfoViewModel {
    public boolean show;
    public String date;

    public StatusInfoViewModel(boolean show, String date) {
        this.show = show;
        this.date = date;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
