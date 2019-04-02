package com.tokopedia.discovery.newdiscovery.search.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tokopedia.discovery.newdiscovery.search.model.SearchSectionItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 10/6/17.
 */

public class SearchSectionPagerAdapter extends FragmentPagerAdapter {

    private List<SearchSectionItem> searchSectionItemList = new ArrayList<>();

    public SearchSectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<SearchSectionItem> searchSectionItemList) {
        this.searchSectionItemList = searchSectionItemList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return searchSectionItemList.get(position).getFragment();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return searchSectionItemList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return searchSectionItemList.get(position).getTitle();
    }
}
