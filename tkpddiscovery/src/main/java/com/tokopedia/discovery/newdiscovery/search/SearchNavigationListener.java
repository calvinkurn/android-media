package com.tokopedia.discovery.newdiscovery.search;

public interface SearchNavigationListener {
    void setupSearchNavigation(ClickListener clickListener);

    interface ClickListener {
        void onFilterClick();
        void onSortClick();
    }
}
