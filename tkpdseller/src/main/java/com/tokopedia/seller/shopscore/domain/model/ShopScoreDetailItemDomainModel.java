package com.tokopedia.seller.shopscore.domain.model;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailItemDomainModel {
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
