package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo;

import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ProductCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ProductFeedViewModel;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/18/17.
 */

public class PromotedProductViewModel extends ProductCardViewModel {

    private int rowNumber;

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

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }
}
