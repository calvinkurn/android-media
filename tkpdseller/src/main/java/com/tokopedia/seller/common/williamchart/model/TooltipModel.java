package com.tokopedia.seller.common.williamchart.model;

/**
 * Created by zulfikarrahman on 5/22/17.
 */

public class TooltipModel {
    private int position;
    private String title;
    private String value;

    public TooltipModel(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public TooltipModel() {

    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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


}
