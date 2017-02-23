package com.tokopedia.discovery.search.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.search.domain.model.SearchItem;
import com.tokopedia.discovery.search.view.adapter.factory.SearchTypeFactory;

import java.util.List;

/**
 * @author erry on 14/02/17.
 */

public class ShopViewModel implements Visitable<SearchTypeFactory> {

    private List<SearchItem> searchItems;

    @Override
    public int type(SearchTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public List<SearchItem> getSearchItems() {
        return searchItems;
    }

    public void setSearchItems(List<SearchItem> searchItems) {
        this.searchItems = searchItems;
    }
}