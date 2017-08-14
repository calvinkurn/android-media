package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProblemViewModel {
    private int type;
    private String name;

    public ProblemViewModel(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
