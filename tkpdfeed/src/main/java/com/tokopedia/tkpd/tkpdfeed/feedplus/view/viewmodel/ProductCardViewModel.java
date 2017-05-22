package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * @author by nisie on 5/15/17.
 */

public abstract class ProductCardViewModel implements Visitable<FeedPlusTypeFactory> {

    protected ArrayList<ProductFeedViewModel> listProduct;

    public ArrayList<ProductFeedViewModel> getListProduct() {
        return listProduct;
    }

    public void setListProduct(ArrayList<ProductFeedViewModel> listProduct) {
        this.listProduct = listProduct;
    }
}
