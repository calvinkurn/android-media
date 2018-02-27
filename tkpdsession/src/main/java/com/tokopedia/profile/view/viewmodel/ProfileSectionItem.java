package com.tokopedia.profile.view.viewmodel;

import android.support.v4.app.Fragment;

/**
 * Created by alvinatin on 21/02/18.
 */

public class ProfileSectionItem {
    String title;
    Fragment fragment;

    public ProfileSectionItem(String title, Fragment fragment) {
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
