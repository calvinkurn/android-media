package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusTypeFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.inspiration.InspirationProductViewModel;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/18/17.
 */

public class InspirationViewModel implements Visitable<FeedPlusTypeFactory> {

    private String inspired;
    protected ArrayList<InspirationProductViewModel> listProduct;


    public InspirationViewModel(String title, ArrayList<InspirationProductViewModel> listProduct) {
        this.inspired = title;
        this.listProduct = listProduct;
    }

    @Override
    public int type(FeedPlusTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

    public ArrayList<InspirationProductViewModel> getListProduct() {
        return listProduct;
    }

    public void setListProduct(ArrayList<InspirationProductViewModel> listProduct) {
        this.listProduct = listProduct;
    }

    public String getInspired() {
        return inspired;
    }

    public void setInspired(String inspired) {
        this.inspired = inspired;
    }
}
