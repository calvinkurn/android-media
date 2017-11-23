package com.tokopedia.seller.product.edit.view.model.categoryrecomm;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Test on 5/12/2017.
 */
public class ProductCategoryPredictionViewModel implements Parcelable{
    private double confidenceScore;
    private List<ProductCategoryIdViewModel> productCategoryId = null;

    private String categoryName[];
    private int categoryId[];
    private String printedString;

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public List<ProductCategoryIdViewModel> getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(List<ProductCategoryIdViewModel> productCategoryId) {
        this.productCategoryId = productCategoryId;
        setPrintedStringAndId();
    }

    private void setPrintedStringAndId() {
        printedString = "";
        int size = productCategoryId.size();
        categoryId = new int[size];
        categoryName = new String[size];
        for (int i = 0; i < size; i++) {
            ProductCategoryIdViewModel productCategoryItem = productCategoryId.get(i);
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

    public int getLastCategoryId() {
        if (printedString == null) {
            setPrintedStringAndId();
        }
        return categoryId[categoryId.length - 1];
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.confidenceScore);
        dest.writeList(this.productCategoryId);
        dest.writeStringArray(this.categoryName);
        dest.writeIntArray(this.categoryId);
        dest.writeString(this.printedString);
    }

    public ProductCategoryPredictionViewModel() {
    }

    protected ProductCategoryPredictionViewModel(Parcel in) {
        this.confidenceScore = in.readDouble();
        this.productCategoryId = new ArrayList<ProductCategoryIdViewModel>();
        in.readList(this.productCategoryId, ProductCategoryIdViewModel.class.getClassLoader());
        this.categoryName = in.createStringArray();
        this.categoryId = in.createIntArray();
        this.printedString = in.readString();
    }

    public static final Creator<ProductCategoryPredictionViewModel> CREATOR = new Creator<ProductCategoryPredictionViewModel>() {
        @Override
        public ProductCategoryPredictionViewModel createFromParcel(Parcel source) {
            return new ProductCategoryPredictionViewModel(source);
        }

        @Override
        public ProductCategoryPredictionViewModel[] newArray(int size) {
            return new ProductCategoryPredictionViewModel[size];
        }
    };
}