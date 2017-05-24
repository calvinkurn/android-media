package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel;

import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/18/17.
 */

public class PromotedProductViewModel extends ProductCardViewModel {

    public PromotedProductViewModel(ArrayList<ProductFeedViewModel> listProduct) {
        this.listProduct = listProduct;
    }

    @Override
    public int type(FeedPlusTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

    public ArrayList<ProductFeedViewModel> getListProduct() {
        return listProduct;
    }

    public void setListProduct(ArrayList<ProductFeedViewModel> listProduct) {
        this.listProduct = listProduct;
    }
}
