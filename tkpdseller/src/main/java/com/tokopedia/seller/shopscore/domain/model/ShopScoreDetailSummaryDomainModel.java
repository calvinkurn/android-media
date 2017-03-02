package com.tokopedia.seller.shopscore.domain.model;

/**
 * Created by sebastianuskh on 3/2/17.
 */
public class ShopScoreDetailSummaryDomainModel {
    private Integer badgeScore;
    private Integer value;
    private Integer color;
    private String text;

    public Integer getBadgeScore() {
        return badgeScore;
    }

    public void setBadgeScore(Integer badgeScore) {
        this.badgeScore = badgeScore;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
