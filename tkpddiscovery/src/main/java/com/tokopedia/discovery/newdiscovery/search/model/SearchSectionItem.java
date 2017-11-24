package com.tokopedia.discovery.newdiscovery.search.model;

import android.support.v4.app.Fragment;

/**
 * Created by henrypriyono on 10/6/17.
 */

public class SearchSectionItem {
    String title;
    Fragment fragment;

    public SearchSectionItem(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
