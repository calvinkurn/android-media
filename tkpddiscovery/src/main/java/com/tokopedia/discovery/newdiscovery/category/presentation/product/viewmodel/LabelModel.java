package com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel;

/**
 * Created by alifa on 3/27/17.
 */

public class LabelModel {

    private String color = "";
    private String title = "";

    public LabelModel(String color, String title) {
        this.color = color;
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
