package com.tokopedia.discovery.search.view.adapter.viewmodel;


import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.discovery.search.domain.model.SearchItem;
import com.tokopedia.discovery.search.view.adapter.factory.SearchTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author erry on 14/02/17.
 */

public class DefaultViewModel implements Visitable<SearchTypeFactory> {

    private String id;
    private String searchTerm;
    private List<SearchItem> searchItems = new ArrayList<>();

    @Override
    public int type(SearchTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public List<SearchItem> getSearchItems() {
        return searchItems;
    }

    public void setSearchItems(List<SearchItem> searchItems) {
        this.searchItems = searchItems;
    }
}