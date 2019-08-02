package com.tokopedia.discovery.newdiscovery.category.presentation.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategorySectionItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alifa on 10/26/17.
 */

public class CategorySectionPagerAdapter extends FragmentStatePagerAdapter {
    private List<CategorySectionItem> categorySectionItems = new ArrayList<>();

    public CategorySectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<CategorySectionItem> categorySectionItems) {
        this.categorySectionItems = categorySectionItems;
        notifyDataSetChanged();
    }

    public void clear() {
        this.categorySectionItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return categorySectionItems.get(position).getFragment();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return categorySectionItems.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categorySectionItems.get(position).getTitle();
    }
}
