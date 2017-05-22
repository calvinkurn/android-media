package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel;

import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/18/17.
 */

public class InspirationViewModel extends ProductCardViewModel {

    private String inspired;

    public InspirationViewModel(String title, ArrayList<ProductFeedViewModel> listProduct) {
        this.inspired = title;
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

    public String getInspired() {
        return inspired;
    }

    public void setInspired(String inspired) {
        this.inspired = inspired;
    }
}
