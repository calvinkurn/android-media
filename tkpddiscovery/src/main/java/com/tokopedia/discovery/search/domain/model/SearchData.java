package com.tokopedia.discovery.search.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author erry on 23/02/17.
 */

public class SearchData {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("items")
    private List<SearchItem> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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