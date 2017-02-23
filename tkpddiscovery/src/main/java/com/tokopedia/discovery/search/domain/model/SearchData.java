package com.tokopedia.discovery.search.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author erry on 23/02/17.
 */

public class SearchData {

    public enum ItemsIds {
        recent_search, popular_search, in_category, autocomplete, shop, hotlist, category
    }

    @SerializedName("id")
    private ItemsIds id;
    @SerializedName("name")
    private String name;
    @SerializedName("items")
    private List<SearchItem> items;

    public ItemsIds getId() {
        return id;
    }

    public void setId(ItemsIds id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SearchItem> getItems() {
        return items;
    }

    public void setItems(List<SearchItem> items) {
        this.items = items;
    }
}