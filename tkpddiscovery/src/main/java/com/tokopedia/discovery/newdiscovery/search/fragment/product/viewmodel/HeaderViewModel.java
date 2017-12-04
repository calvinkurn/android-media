package com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.model.OfficialStoreBannerModel;
import com.tokopedia.discovery.newdiscovery.search.model.SuggestionModel;

/**
 * @author by errysuprayogi on 11/7/17.
 */

public class HeaderViewModel implements Visitable<ProductListTypeFactory> {

    OfficialStoreBannerModel officialStoreBannerModel;
    SuggestionModel suggestionModel;

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public HeaderViewModel() {
    }

    public void setOfficialStoreBannerModel(OfficialStoreBannerModel officialStoreBannerModel) {
        this.officialStoreBannerModel = officialStoreBannerModel;
    }

    public void setSuggestionModel(SuggestionModel suggestionModel) {
        this.suggestionModel = suggestionModel;
    }

    public OfficialStoreBannerModel getOfficialStoreBannerModel() {
        return officialStoreBannerModel;
    }

    public SuggestionModel getSuggestionModel() {
        return suggestionModel;
    }

    public boolean hasHeader() {
        return (suggestionModel != null || officialStoreBannerModel != null);
    }
}
