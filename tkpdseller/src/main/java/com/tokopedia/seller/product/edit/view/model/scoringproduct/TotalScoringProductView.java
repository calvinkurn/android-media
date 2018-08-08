
package com.tokopedia.seller.product.edit.view.model.scoringproduct;

public class TotalScoringProductView {
    private String valueScoreProduct;
    private int countScoreProduct;
    private String color;
    private int maxScore;

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getValueScoreProduct() {
        return valueScoreProduct;
    }

    public void setValueScoreProduct(String valueScoreProduct) {
        this.valueScoreProduct = valueScoreProduct;
    }

    public int getCountScoreProduct() {
        return countScoreProduct;
    }

    public void setCountScoreProduct(int countScoreProduct) {
        this.countScoreProduct = countScoreProduct;
    }
}
