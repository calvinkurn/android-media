package com.tokopedia.seller.opportunity.adapter.viewmodel;

/**
 * Created by nisie on 4/20/17.
 */

public class SimpleCheckListItemModel {
    String title;
    String value;
    boolean isSelected;

    public SimpleCheckListItemModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
