package com.tokopedia.flight.booking.view.viewmodel;

/**
 * @author by alvarisi on 11/21/17.
 */

public class SimpleViewModel {
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
}
