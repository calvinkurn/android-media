package com.tokopedia.discovery.search.view.adapter;

/**
 * Created by eriksuprayogi on 2/21/17.
 */

public interface ItemClickListener {

    void copyTextToSearchView(String text);

    void onDeleteRecentSearchItem(String keyword);

    void onDeleteAllRecentSearch();

    void onItemClicked(String applink, String webUrl);

    void onItemSearchClicked(String keyword, String categoryId, boolean isOfficial);

    void setOnTabShop(boolean onTabShop);
}