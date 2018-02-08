package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/18/17.
 */

public class OfficialStoreCampaignViewModel implements Visitable<FeedPlusTypeFactory> {

    private final String officialStoreHeaderImageUrl;
    private final String redirectUrl;
    private final String hexColor;
    private final String title;
    private final ArrayList<OfficialStoreCampaignProductViewModel> listProduct;
    private final int page;
    private int rowNumber;

    public OfficialStoreCampaignViewModel(String url,
                                          String redirectUrl,
                                          String hexColor,
                                          String title,
                                          ArrayList<OfficialStoreCampaignProductViewModel> listProduct, int page) {
        this.officialStoreHeaderImageUrl = url;
        this.redirectUrl = redirectUrl;
        this.hexColor = hexColor;
        this.listProduct = listProduct;
        this.title = title;
        this.page = page;
    }


    public ArrayList<OfficialStoreCampaignProductViewModel> getListProduct() {
        return listProduct;
    }

    @Override
    public int type(FeedPlusTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

    public String getOfficialStoreHeaderImageUrl() {
        return officialStoreHeaderImageUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getHexColor() {
        return hexColor;
    }

    public String getTitle() {
        return title;
    }

    public int getPage() {
        return page;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }
}
