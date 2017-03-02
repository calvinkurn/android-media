package com.tokopedia.seller.shopscore.view.model;

/**
 * Created by sebastianuskh on 3/2/17.
 */
public class ShopScoreDetailSummaryViewModel {
    private Integer badgeScore;
    private String html;
    private Integer color;
    private Integer value;
    private String text;

    public Integer getBadgeScore() {
        return badgeScore;
    }

    public void setBadgeScore(Integer badgeScore) {
        this.badgeScore = badgeScore;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
