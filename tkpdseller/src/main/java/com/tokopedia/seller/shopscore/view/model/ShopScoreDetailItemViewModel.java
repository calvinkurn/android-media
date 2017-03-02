package com.tokopedia.seller.shopscore.view.model;

/**
 * Created by sebastianuskh on 2/27/17.
 */
public class ShopScoreDetailItemViewModel {
    private String title;
    private Integer value;
    private String description;
    private Integer progressBarColor;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getProgressBarColor() {
        return progressBarColor;
    }

    public void setProgressBarColor(Integer progressBarColor) {
        this.progressBarColor = progressBarColor;
    }
}
