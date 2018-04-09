package com.tokopedia.seller.product.edit.data.source.cache;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.seller.product.edit.data.source.cache.model.ProductScore.ColorIndicator;
import com.tokopedia.seller.product.edit.data.source.cache.model.ProductScore.DataScoringProduct;
import com.tokopedia.seller.product.edit.data.source.cache.model.ProductScore.IndicatorScore;
import com.tokopedia.seller.product.edit.data.source.cache.model.ProductScore.IndicatorScoring;
import com.tokopedia.seller.product.edit.data.source.cache.model.ProductScore.Scale;
import com.tokopedia.seller.product.edit.data.source.cache.model.ProductScore.ValueIndicator;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.IndicatorScoreView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.TotalScoringProductView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/12/17.
 */

public class ProductScoreDataSourceCache {

    public static final String COUNT_TYPE_IMAGE = "count";
    public static final String RESOLUTION_TYPE_IMAGE = "resolution";
    public static final int IMAGE_INDICATOR = 1;
    public static final int NAME_INDICATOR = 2;
    public static final int DESC_INDICATOR = 3;
    public static final int STOK_INDICATOR = 4;
    public static final int FREE_RETURN_INDICATOR = 5;
    public static final int PRODUCT_VARIANT = 6;
    public static final int PRODUCT_VIDEO = 7;

    private DataScoringProduct dataScoringProduct;

    public ProductScoreDataSourceCache(DataScoringProduct dataScoringProduct) {
        this.dataScoringProduct = dataScoringProduct;
    }

    public Observable<DataScoringProductView> getValidationScore(ValueIndicatorScoreModel valueIndicatorScoreModel) {
        DataScoringProductView dataScoringProductView = calculateScoreProduct(valueIndicatorScoreModel);
        return Observable.just(dataScoringProductView);
    }

    private DataScoringProductView calculateScoreProduct(ValueIndicatorScoreModel valueIndicatorScoreModel) {
        int tempScore = 0;
        List<IndicatorScoreView> indicatorScoreViews = new ArrayList<>();
        for (IndicatorScore indicatorScore : dataScoringProduct.getIndicatorScore()) {
            int tempScoreEachItem = calculateScoreEachItem(indicatorScore, valueIndicatorScoreModel);
            tempScore = tempScore + tempScoreEachItem;

            int maxScoreEachItem = calculateMaxScoreEachItem(indicatorScore);
            String colorScore = calculateColorScore(indicatorScore, tempScoreEachItem, maxScoreEachItem);

            IndicatorScoreView indicatorScoreView = new IndicatorScoreView();
            indicatorScoreView.setNameIndicator(indicatorScore.getNameIndicator());
            indicatorScoreView.setIndicatorColor(colorScore);
            indicatorScoreView.setMaxScoreIndicator(maxScoreEachItem);
            indicatorScoreView.setScore(tempScoreEachItem);
            List<String> descs = new ArrayList<>();
            for (ValueIndicator valueIndicator : indicatorScore.getValueIndicator()) {
                descs.add(valueIndicator.getIndicatorDesc());
            }
            indicatorScoreView.setIndicatorDescs(descs);

            indicatorScoreViews.add(indicatorScoreView);
        }

        Scale scale = getScalingTotalScore(dataScoringProduct, tempScore);

        DataScoringProductView dataScoringProductView = new DataScoringProductView();

        TotalScoringProductView totalScoringProductView = new TotalScoringProductView();
        totalScoringProductView.setColor(scale.getColor());
        totalScoringProductView.setMaxScore(dataScoringProduct.getTotalScoringProduct().getMaxScore());
        totalScoringProductView.setCountScoreProduct(tempScore);
        totalScoringProductView.setValueScoreProduct(scale.getText());

        dataScoringProductView.setTotalScoringProductView(totalScoringProductView);
        dataScoringProductView.setIndicatorScoreView(indicatorScoreViews);
        return dataScoringProductView;
    }

    private int calculateMaxScoreEachItem(final IndicatorScore indicatorScore) {
        int tempMaxScore = 0;
        for (ValueIndicator valueIndicator : indicatorScore.getValueIndicator()) {
            List<IndicatorScoring> indicatorScorings = valueIndicator.getIndicatorScoring();
            Collections.sort(indicatorScorings, new Comparator<IndicatorScoring>() {
                @Override
                public int compare(IndicatorScoring t2, IndicatorScoring t1) {
                    return t2.getScore() < t1.getScore() ? t1.getScore() : t2.getScore();
                }
            });

            for (int i = indicatorScorings.size() - 1; i >= 0; i--) {
                if (i == indicatorScorings.size() - 1) {
                    IndicatorScoring indicatorScoring = indicatorScorings.get(i);
                    tempMaxScore = tempMaxScore + indicatorScoring.getScore();
                    break;
                }
            }
        }
        return tempMaxScore;
    }

    private String calculateColorScore(IndicatorScore indicatorScore, int tempScoreEachItem, int maxScore) {
        List<ColorIndicator> colorIndicators = indicatorScore.getColorIndicator();
        for (int i = colorIndicators.size() - 1; i >= 0; i--) {
            ColorIndicator colorIndicator = colorIndicators.get(i);
            float percentageScore = tempScoreEachItem / maxScore * 100;
            if (percentageScore >= colorIndicator.getMin()) {
                return colorIndicator.getColor();
            }
        }
        return "";
    }

    private Scale getScalingTotalScore(DataScoringProduct dataScoringProduct, int tempScore) {
        List<Scale> scales = dataScoringProduct.getTotalScoringProduct().getScale();
        for (int i = scales.size() - 1; i >= 0; i--) {
            Scale scale = scales.get(i);
            if (tempScore >= scale.getMin()) {
                return scale;
            }
        }
        return null;
    }

    private int calculateScoreEachItem(IndicatorScore indicatorScore, ValueIndicatorScoreModel valueIndicatorScoreModel) {
        switch (indicatorScore.getIndicatorId()) {
            case IMAGE_INDICATOR:
                return calculateScoreProductImage(indicatorScore, valueIndicatorScoreModel.getImageCount(),
                        valueIndicatorScoreModel.getImageResolution(), valueIndicatorScoreModel.isHasCatalog());
            case NAME_INDICATOR:
                return calculateScoreProductName(indicatorScore, valueIndicatorScoreModel.getLengthProductName());
            case DESC_INDICATOR:
                return calculateScoreProductDesc(indicatorScore, valueIndicatorScoreModel.getLengthDescProduct());
            case STOK_INDICATOR:
                return calculateScoreStok(indicatorScore, valueIndicatorScoreModel.isStockStatus());
            case FREE_RETURN_INDICATOR:
                return calculateScoreFreeReturns(indicatorScore,
                        valueIndicatorScoreModel.isFreeReturnStatus() &&
                        valueIndicatorScoreModel.isFreeReturnActive());
            case PRODUCT_VARIANT:
                return calculateScoreProductVariant(indicatorScore, valueIndicatorScoreModel.isVariantActive());
            case PRODUCT_VIDEO:
                return calculateScoreProductVideo(indicatorScore, valueIndicatorScoreModel.isHasVideo());
            default:
                return 0;
        }
    }

    private int calculateScoreFreeReturns(IndicatorScore indicatorScore, boolean freeReturnStatus) {
        for (ValueIndicator valueIndicator : indicatorScore.getValueIndicator()) {
            return calculateScore(valueIndicator, freeReturnStatus ? 1 : 0);
        }
        return 0;
    }

    private int calculateScoreProductVariant(IndicatorScore indicatorScore, boolean hasProductVariant) {
        for (ValueIndicator valueIndicator : indicatorScore.getValueIndicator()) {
            return calculateScore(valueIndicator, hasProductVariant ? 1 : 0);
        }
        return 0;
    }

    private int calculateScoreProductVideo(IndicatorScore indicatorScore, boolean hasVideo) {
        for (ValueIndicator valueIndicator : indicatorScore.getValueIndicator()) {
            return calculateScore(valueIndicator, hasVideo ? 1 : 0);
        }
        return 0;
    }

    private int calculateScoreStok(IndicatorScore indicatorScore, boolean stockStatus) {
        for (ValueIndicator valueIndicator : indicatorScore.getValueIndicator()) {
            return calculateScore(valueIndicator, stockStatus ? 1 : 0);
        }
        return 0;
    }

    private int calculateScoreProductDesc(IndicatorScore indicatorScore, int lengthDescProduct) {
        for (ValueIndicator valueIndicator : indicatorScore.getValueIndicator()) {
            return calculateScore(valueIndicator, lengthDescProduct);
        }
        return 0;
    }

    private int calculateScoreProductName(IndicatorScore indicatorScore, int lengthProductName) {
        for (ValueIndicator valueIndicator : indicatorScore.getValueIndicator()) {
            return calculateScore(valueIndicator, lengthProductName);
        }
        return 0;
    }

    private int calculateScoreProductImage(IndicatorScore indicatorScore, int imageCount, int imageResolution, boolean hasCatalog) {
        for (ValueIndicator valueIndicator : indicatorScore.getValueIndicator()) {
            if (valueIndicator.getIndicatorType().equals(COUNT_TYPE_IMAGE)) {
                imageCount = calculateScoreImage(valueIndicator, imageCount, hasCatalog);
            } else if (valueIndicator.getIndicatorType().equals(RESOLUTION_TYPE_IMAGE)) {
                imageResolution = calculateScoreImage(valueIndicator, imageResolution, hasCatalog);
            }
        }
        return imageCount + imageResolution;
    }

    private int calculateScoreImage(ValueIndicator valueIndicator, int valueCount, boolean hasCatalog) {
        List<IndicatorScoring> indicatorScorings = valueIndicator.getIndicatorScoring();
        if (hasCatalog){
            return indicatorScorings.get(indicatorScorings.size() - 1).getScore();
        }
        return calculateScore(valueIndicator, valueCount);
    }

    private int calculateScore(ValueIndicator valueIndicator, int valueCount) {
        List<IndicatorScoring> indicatorScorings = valueIndicator.getIndicatorScoring();
        for (int i = indicatorScorings.size() - 1; i >= 0; i--) {
            IndicatorScoring indicatorScoring = indicatorScorings.get(i);
            if (valueCount >= indicatorScoring.getMin()) {
                return indicatorScoring.getScore();
            }
        }
        return 0;
    }
}
