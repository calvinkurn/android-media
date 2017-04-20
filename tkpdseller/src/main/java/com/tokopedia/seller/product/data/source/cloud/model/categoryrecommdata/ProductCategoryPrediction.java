package com.tokopedia.seller.product.data.source.cloud.model.categoryrecommdata;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hendry on 4/18/2017.
 */

public class ProductCategoryPrediction {

    @SerializedName("confidence_score")
    @Expose
    private double confidenceScore;
    @SerializedName("product_category_id")
    @Expose
    private List<ProductCategoryId> productCategoryId = null;

    private String categoryName[];
    private int categoryId[];
    private String printedString;

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public List<ProductCategoryId> getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(List<ProductCategoryId> productCategoryId) {
        this.productCategoryId = productCategoryId;
        setPrintedStringAndId();
    }

    private void setPrintedStringAndId(){
        printedString = "";
        int size = productCategoryId.size();
        categoryId = new int[size];
        categoryName = new String[size];
        for (int i=0; i < size; i++) {
            ProductCategoryId productCategoryItem = productCategoryId.get(i);
            int id = productCategoryItem.getId();
            categoryName[i] = productCategoryItem.getName();
            categoryId[i] = id; // will be the last item end of loop
            if (!TextUtils.isEmpty(categoryName[i])) {
                if (!TextUtils.isEmpty(printedString)) {
                    printedString += " / ";
                }
                printedString += categoryName[i];
            }
        }
    }

    public String getPrintedString() {
        if (printedString == null) {
            setPrintedStringAndId();
        }
        return printedString;
    }

    public int getLastCategoryId(){
        if (printedString == null) {
            setPrintedStringAndId();
        }
        return categoryId[categoryId.length-1];
    }

    public int[] getCategoryId() {
        if (categoryName == null) {
            setPrintedStringAndId();
        }
        return categoryId;
    }

    public String[] getCategoryName() {
        if (categoryName == null) {
            setPrintedStringAndId();
        }
        return categoryName;
    }

}
