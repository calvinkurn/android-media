package com.tokopedia.seller.topads.dashboard.view.model;

/**
 * Created by normansyahputa on 2/13/17.
 */

public abstract class StateTypeBasedModel implements TypeBasedModel {
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
