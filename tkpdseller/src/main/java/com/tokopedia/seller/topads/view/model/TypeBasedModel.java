package com.tokopedia.seller.topads.view.model;

/**
 * Created by normansyahputa on 2/13/17.
 */

public abstract class TypeBasedModel {
    private int type;

    public TypeBasedModel(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
