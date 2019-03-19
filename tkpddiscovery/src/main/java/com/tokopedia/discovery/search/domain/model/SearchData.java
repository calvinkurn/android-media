package com.tokopedia.discovery.search.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author erry on 23/02/17.
 */

public class SearchData {

    public static final String AUTOCOMPLETE_RECENT_VIEW = "recent_view";
    public static final String AUTOCOMPLETE_RECENT_SEARCH = "recent_search";
    public static final String AUTOCOMPLETE_POPULAR_SEARCH = "popular_search";
    public static final String AUTOCOMPLETE_DIGITAL = "top_digital";
    public static final String AUTOCOMPLETE_CATEGORY = "category";
    public static final String AUTOCOMPLETE_DEFAULT = "autocomplete";
    public static final String AUTOCOMPLETE_HOTLIST = "hotlist";
    public static final String AUTOCOMPLETE_IN_CATEGORY = "in_category";
    public static final String AUTOCOMPLETE_SHOP = "shop";
    public static final String AUTOCOMPLETE_PROFILE = "profile";
    public static final String AUTOCOMPLETE_TOP_PROFILE = "top_profile";

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