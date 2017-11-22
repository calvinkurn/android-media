package com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel;

import android.support.v4.app.Fragment;

/**
 * @author by alifa on 10/26/17.
 */

public class CategorySectionItem {
    String title;
    Fragment fragment;

    public CategorySectionItem(String title, Fragment fragment) {
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
