package com.tokopedia.discovery.newdiscovery.search;

public interface SearchNavigationListener {
    void setupSearchNavigation(ClickListener clickListener);

    void refreshMenuItemGridIcon(int titleResId, int iconResId);

    interface ClickListener {
        void onFilterClick();
        void onSortClick();
        void onChangeGridClick();
    }
}
