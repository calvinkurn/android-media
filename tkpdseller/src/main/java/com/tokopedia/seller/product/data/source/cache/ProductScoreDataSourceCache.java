package com.tokopedia.seller.product.data.source.cache;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.seller.product.data.source.cache.model.ProductScore.ColorIndicator;
import com.tokopedia.seller.product.data.source.cache.model.ProductScore.DataScoringProduct;
import com.tokopedia.seller.product.data.source.cache.model.ProductScore.IndicatorScore;
import com.tokopedia.seller.product.data.source.cache.model.ProductScore.IndicatorScoring;
import com.tokopedia.seller.product.data.source.cache.model.ProductScore.JsonScoringProduct;
import com.tokopedia.seller.product.data.source.cache.model.ProductScore.Scale;
import com.tokopedia.seller.product.data.source.cache.model.ProductScore.ValueIndicator;
import com.tokopedia.seller.product.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.view.model.scoringproduct.IndicatorScoreView;
import com.tokopedia.seller.product.view.model.scoringproduct.TotalScoringProductView;
import com.tokopedia.seller.product.view.model.scoringproduct.ValueIndicatorScoreModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/12/17.
 */

public class ProductScoreDataSourceCache {

    public static final String COUNT_TYPE_IMAGE = "count";
    public static final String RESOLUTION_TYPE_IMAGE = "resolution";

    private Context context;
    private Gson gson;

    public ProductScoreDataSourceCache(Context context, Gson gson) {
        this.context = context;
        this.gson = gson;
    }

    public Observable<DataScoringProductView> getValidationScore(ValueIndicatorScoreModel valueIndicatorScoreModel) {
        String jsonIndicatorScoreProduct = loadJSONFromAsset();
        DataScoringProduct dataScoringProduct = gson.fromJson(jsonIndicatorScoreProduct, JsonScoringProduct.class).getDataScoringProduct();
        DataScoringProductView dataScoringProductView = calculateScoreProduct(dataScoringProduct, valueIndicatorScoreModel);

        return Observable.just(dataScoringProductView);
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("indicator_score_product.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    private DataScoringProductView calculateScoreProduct(DataScoringProduct dataScoringProduct, ValueIndicatorScoreModel valueIndicatorScoreModel) {
        int tempScore = 0;
        List<IndicatorScoreView> indicatorScoreViews = new ArrayList<>();
        for (IndicatorScore indicatorScore : dataScoringProduct.getIndicatorScore()) {
            int tempScoreEachItem = calculateScoreEachItem(indicatorScore, valueIndicatorScoreModel);
            tempScore = tempScore + tempScoreEachItem;

            String colorScore = calculateColorScore(indicatorScore, tempScoreEachItem);

            IndicatorScoreView indicatorScoreView = new IndicatorScoreView();
            indicatorScoreView.setNameIndicator(indicatorScore.getNameIndicator());
            indicatorScoreView.setIndicatorColor(colorScore);
            List<String> descs = new ArrayList<>();
            for(ValueIndicator valueIndicator: indicatorScore.getValueIndicator()){
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

    private String calculateColorScore(IndicatorScore indicatorScore, int tempScoreEachItem) {
        List<ColorIndicator> colorIndicators = indicatorScore.getColorIndicator();
        for (int i = colorIndicators.size() - 1; i >= 0; i--) {
            ColorIndicator colorIndicator = colorIndicators.get(i);
            if (tempScoreEachItem >= colorIndicator.getMin()) {
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
            case 1:
                return calculateScoreProductImage(indicatorScore, valueIndicatorScoreModel.getImageCount(),
                        valueIndicatorScoreModel.getImageResolution());
            case 2:
                return calculateScoreProductName(indicatorScore, valueIndicatorScoreModel.getLengthProductName());
            case 3:
                return calculateScoreProductDesc(indicatorScore, valueIndicatorScoreModel.getLengthDescProduct());
            case 4:
                return calculateScoreStok(indicatorScore, valueIndicatorScoreModel.isStockStatus());
            case 5:
                return calculateScoreFreeReturns(indicatorScore, valueIndicatorScoreModel.isFreeReturnStatus());
            case 6:
                return calculateScoreCashback(indicatorScore, valueIndicatorScoreModel.isCashbackStatus());
            default:
                return 0;
        }
    }

    private int calculateScoreCashback(IndicatorScore indicatorScore, boolean cashbackStatus) {
        for (ValueIndicator valueIndicator : indicatorScore.getValueIndicator()) {
            return calculateScore(valueIndicator, cashbackStatus ? 1 : 0);
        }
        return 0;
    }

    private int calculateScoreFreeReturns(IndicatorScore indicatorScore, boolean freeReturnStatus) {
        for (ValueIndicator valueIndicator : indicatorScore.getValueIndicator()) {
            return calculateScore(valueIndicator, freeReturnStatus ? 1 : 0);
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

    private int calculateScoreProductImage(IndicatorScore indicatorScore, int imageCount, int imageResolution) {
        for (ValueIndicator valueIndicator : indicatorScore.getValueIndicator()) {
            if(valueIndicator.getIndicatorType().equals(COUNT_TYPE_IMAGE)){
                imageCount =  calculateScore(valueIndicator, imageCount);
            }else if(valueIndicator.getIndicatorType().equals(RESOLUTION_TYPE_IMAGE)){
                imageResolution =calculateScore(valueIndicator, imageResolution);
            }
        }
        return imageCount + imageResolution;
    }

    private int calculateScore(ValueIndicator valueIndicator, int valueCount) {
        List<IndicatorScoring> indicatorScorings = valueIndicator.getIndicatorScoring();
        for(int i = indicatorScorings.size() - 1; i >= 0; i--){
            IndicatorScoring indicatorScoring = indicatorScorings.get(i);
            if(valueCount >= indicatorScoring.getMin()){
                return indicatorScoring.getScore();
            }
        }
        return 0;
    }
}
