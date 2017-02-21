package com.tokopedia.seller.topads.view.models;

/**
 * Created by normansyahputa on 2/13/17.
 */

public class StateTypeBasedModel extends TypeBasedModel {
    private boolean isSelected;

    public StateTypeBasedModel(int type) {
        super(type);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
