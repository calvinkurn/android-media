package com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.common.data.Option;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.model.SuggestionModel;
import com.tokopedia.topads.sdk.domain.model.CpmModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 11/7/17.
 */

public class HeaderViewModel implements Visitable<ProductListTypeFactory> {

    SuggestionModel suggestionModel;
    List<Option> quickFilterList = new ArrayList<>();
    private GuidedSearchViewModel guidedSearch;
    CpmModel cpmModel;
    private GlobalNavViewModel globalNavViewModel;

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public HeaderViewModel() {
    }

    public CpmModel getCpmModel() {
        return cpmModel;
    }

    public void setCpmModel(CpmModel cpmModel) {
        this.cpmModel = cpmModel;
    }

    public void setSuggestionModel(SuggestionModel suggestionModel) {
        this.suggestionModel = suggestionModel;
    }

    public SuggestionModel getSuggestionModel() {
        return suggestionModel;
    }

    public boolean hasHeader() {
        return (suggestionModel != null);
    }

    public List<Option> getQuickFilterList() {
        return quickFilterList;
    }

    public void setQuickFilterList(List<Option> quickFilterList) {
        this.quickFilterList.clear();
        this.quickFilterList.addAll(quickFilterList);
    }

    public void setGuidedSearch(GuidedSearchViewModel guidedSearch) {
        this.guidedSearch = guidedSearch;
    }

    public GuidedSearchViewModel getGuidedSearch() {
        return guidedSearch;
    }

    public GlobalNavViewModel getGlobalNavViewModel() {
        return globalNavViewModel;
    }

    public void setGlobalNavViewModel(GlobalNavViewModel globalNavViewModel) {
        this.globalNavViewModel = globalNavViewModel;
    }
}
