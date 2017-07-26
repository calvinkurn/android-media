package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/18/17.
 */

public class OfficialStoreCampaignViewModel implements Visitable<FeedPlusTypeFactory> {

    private String officialStoreHeaderImageUrl;
    private ArrayList<OfficialStoreCampaignProductViewModel> listProduct;

    public OfficialStoreCampaignViewModel(String url,
                                          ArrayList<OfficialStoreCampaignProductViewModel> listProduct) {
        this.officialStoreHeaderImageUrl = url;
        this.listProduct = listProduct;
    }


    public ArrayList<OfficialStoreCampaignProductViewModel> getListProduct() {
        return listProduct;
    }

    @Override
    public int type(FeedPlusTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }


    public void setListProduct(ArrayList<OfficialStoreCampaignProductViewModel> listProduct) {
        this.listProduct = listProduct;
    }

    public String getOfficialStoreHeaderImageUrl() {
        return officialStoreHeaderImageUrl;
    }

    public void setOfficialStoreHeaderImageUrl(String officialStoreHeaderImageUrl) {
        this.officialStoreHeaderImageUrl = officialStoreHeaderImageUrl;
    }
}
