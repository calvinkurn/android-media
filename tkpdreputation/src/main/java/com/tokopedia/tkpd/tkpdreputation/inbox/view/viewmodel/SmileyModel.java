package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel;

/**
 * @author by nisie on 8/28/17.
 */

public class SmileyModel {
    int ResId;
    String name;
    String value;

    public SmileyModel(int resId, String name, String value) {
        ResId = resId;
        this.name = name;
        this.value = value;
    }

    public int getResId() {
        return ResId;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
