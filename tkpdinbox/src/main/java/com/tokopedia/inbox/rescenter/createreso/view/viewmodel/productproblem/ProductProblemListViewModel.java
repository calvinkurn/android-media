package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 20/08/17.
 */

public class ProductProblemListViewModel implements Parcelable {
    private List<ProductProblemViewModel> productProblemViewModels = new ArrayList<>();

    public ProductProblemListViewModel(List<ProductProblemViewModel> productProblemViewModels) {
        this.productProblemViewModels = productProblemViewModels;
    }

    public List<ProductProblemViewModel> getProductProblemViewModels() {
        return productProblemViewModels;
    }

    public void setProductProblemViewModels(List<ProductProblemViewModel> productProblemViewModels) {
        this.productProblemViewModels = productProblemViewModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.productProblemViewModels);
    }

    protected ProductProblemListViewModel(Parcel in) {
        this.productProblemViewModels = in.createTypedArrayList(ProductProblemViewModel.CREATOR);
    }

    public static final Creator<ProductProblemListViewModel> CREATOR = new Creator<ProductProblemListViewModel>() {
        @Override
        public ProductProblemListViewModel createFromParcel(Parcel source) {
            return new ProductProblemListViewModel(source);
        }

        @Override
        public ProductProblemListViewModel[] newArray(int size) {
            return new ProductProblemListViewModel[size];
        }
    };
}
