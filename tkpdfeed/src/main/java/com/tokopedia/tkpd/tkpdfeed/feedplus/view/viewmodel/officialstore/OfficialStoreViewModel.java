package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore;

import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ProductCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ProductFeedViewModel;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/18/17.
 */

public class OfficialStoreViewModel extends ProductCardViewModel {

    private String officialStoreHeaderImageUrl;

    public OfficialStoreViewModel(String url, ArrayList<ProductFeedViewModel> listProduct) {
        this.officialStoreHeaderImageUrl = url;
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

    public String getOfficialStoreHeaderImageUrl() {
        return officialStoreHeaderImageUrl;
    }

    public void setOfficialStoreHeaderImageUrl(String officialStoreHeaderImageUrl) {
        this.officialStoreHeaderImageUrl = officialStoreHeaderImageUrl;
    }
}
