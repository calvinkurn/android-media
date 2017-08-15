package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

/**
 * Created by yoasfs on 14/08/17.
 */

public class OrderDetailViewModel {
    private int id;
    private int returnable;

    public OrderDetailViewModel() {

    }

    public OrderDetailViewModel(int id, int returnable) {
        this.id = id;
        this.returnable = returnable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReturnable() {
        return returnable;
    }

    public void setReturnable(int returnable) {
        this.returnable = returnable;
    }
}
