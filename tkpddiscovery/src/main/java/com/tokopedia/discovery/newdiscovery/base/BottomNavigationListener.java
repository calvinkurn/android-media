package com.tokopedia.discovery.newdiscovery.base;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.List;

/**
 * Created by henrypriyono on 10/16/17.
 */

public interface BottomNavigationListener {
    void setupBottomNavigation(List<AHBottomNavigationItem> items,
                               AHBottomNavigation.OnTabSelectedListener tabSelectedListener);
    void showBottomNavigation();
    void hideBottomNavigation();

    void refreshBottomNavigationIcon(List<AHBottomNavigationItem> bottomNavigationItems);
}
