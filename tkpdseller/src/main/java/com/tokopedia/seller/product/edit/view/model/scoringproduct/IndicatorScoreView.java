
package com.tokopedia.seller.product.edit.view.model.scoringproduct;


import java.util.List;

public class IndicatorScoreView {
    private String nameIndicator;
    private int maxScoreIndicator;
    private int score;
    private String indicatorColor;
    private List<String> indicatorDescs = null;

    public int getMaxScoreIndicator() {
        return maxScoreIndicator;
    }

    public void setMaxScoreIndicator(int maxScoreIndicator) {
        this.maxScoreIndicator = maxScoreIndicator;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getIndicatorColor() {
        return indicatorColor;
    }

    public void setIndicatorColor(String indicatorColor) {
        this.indicatorColor = indicatorColor;
    }

    public List<String> getIndicatorDescs() {
        return indicatorDescs;
    }

    public void setIndicatorDescs(List<String> indicatorDescs) {
        this.indicatorDescs = indicatorDescs;
    }

    public String getNameIndicator() {
        return nameIndicator;
    }

    public void setNameIndicator(String nameIndicator) {
        this.nameIndicator = nameIndicator;
    }
}
