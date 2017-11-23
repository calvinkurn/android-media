package com.tokopedia.flight.booking.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.type.ItemType;

/**
 * @author by alvarisi on 11/21/17.
 */

public class SimpleViewModel implements ItemType {


    public static final int TYPE = 983;
    private String label;
    private String description;

    public SimpleViewModel() {
    }

    public SimpleViewModel(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
