package com.tokopedia.seller.shopscore.view.model;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreViewModelData {
    private String title;
    private int value;
    private String description;
    private int progressBarColor;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProgressBarColor() {
        return progressBarColor;
    }

    public void setProgressBarColor(int progressBarColor) {
        this.progressBarColor = progressBarColor;
    }
}
