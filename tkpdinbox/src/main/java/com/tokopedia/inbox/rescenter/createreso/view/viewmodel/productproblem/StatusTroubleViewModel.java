package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

/**
 * Created by yoasfs on 14/08/17.
 */

public class StatusTroubleViewModel {
    public int id;
    public String name;

    public StatusTroubleViewModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
