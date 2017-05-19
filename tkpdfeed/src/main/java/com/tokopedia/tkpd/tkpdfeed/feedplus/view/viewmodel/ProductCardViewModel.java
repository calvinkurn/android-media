package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * @author by nisie on 5/15/17.
 */

public class ProductCardViewModel implements Visitable<FeedPlusTypeFactory>, Parcelable {

    private ProductCardHeaderViewModel productCardHeaderViewModel;
    private ArrayList<ProductFeedViewModel> listProduct;


    public ProductCardViewModel() {
    }

    public ProductCardViewModel(String s, ArrayList<ProductFeedViewModel> listProduct) {
        this.productCardHeaderViewModel = new ProductCardHeaderViewModel();
        this.productCardHeaderViewModel.setShopName(s);
        this.listProduct = listProduct;

    }

    protected ProductCardViewModel(Parcel in) {
        productCardHeaderViewModel = in.readParcelable(ProductCardHeaderViewModel.class.getClassLoader());
        listProduct = in.createTypedArrayList(ProductFeedViewModel.CREATOR);
    }

    public static final Creator<ProductCardViewModel> CREATOR = new Creator<ProductCardViewModel>() {
        @Override
        public ProductCardViewModel createFromParcel(Parcel in) {
            return new ProductCardViewModel(in);
        }

        @Override
        public ProductCardViewModel[] newArray(int size) {
            return new ProductCardViewModel[size];
        }
    };

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(productCardHeaderViewModel, flags);
        dest.writeTypedList(listProduct);
    }

    public ProductCardHeaderViewModel getHeader() {
        return productCardHeaderViewModel;
    }

    public void setHeader(ProductCardHeaderViewModel productCardHeaderViewModel) {
        this.productCardHeaderViewModel = productCardHeaderViewModel;
    }

    public ArrayList<ProductFeedViewModel> getListProduct() {
        return listProduct;
    }

    public void setListProduct(ArrayList<ProductFeedViewModel> listProduct) {
        this.listProduct = listProduct;
    }
}
