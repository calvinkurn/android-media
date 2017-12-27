package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.inspiration;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/18/17.
 */

public class InspirationViewModel implements Visitable<FeedPlusTypeFactory> {

    private String title;
    protected ArrayList<InspirationProductViewModel> listProduct;
    private int rowNumber;


    public InspirationViewModel(String title, ArrayList<InspirationProductViewModel> listProduct) {
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }
}
