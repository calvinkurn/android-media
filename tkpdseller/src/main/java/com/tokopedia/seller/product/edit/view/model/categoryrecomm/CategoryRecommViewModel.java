package com.tokopedia.seller.product.edit.view.model.categoryrecomm;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Test on 5/12/2017.
 */

public class CategoryRecommViewModel implements Parcelable{
    private List<ProductCategoryPredictionViewModel> productCategoryPrediction = null;

    public List<ProductCategoryPredictionViewModel> getProductCategoryPrediction() {
        return productCategoryPrediction;
    }

    public void setProductCategoryPrediction(List<ProductCategoryPredictionViewModel> productCategoryPrediction) {
        this.productCategoryPrediction = productCategoryPrediction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.productCategoryPrediction);
    }

    public CategoryRecommViewModel() {
    }

    protected CategoryRecommViewModel(Parcel in) {
        this.productCategoryPrediction = new ArrayList<ProductCategoryPredictionViewModel>();
        in.readList(this.productCategoryPrediction, ProductCategoryPredictionViewModel.class.getClassLoader());
    }

    public static final Creator<CategoryRecommViewModel> CREATOR = new Creator<CategoryRecommViewModel>() {
        @Override
        public CategoryRecommViewModel createFromParcel(Parcel source) {
            return new CategoryRecommViewModel(source);
        }

        @Override
        public CategoryRecommViewModel[] newArray(int size) {
            return new CategoryRecommViewModel[size];
        }
    };
}
