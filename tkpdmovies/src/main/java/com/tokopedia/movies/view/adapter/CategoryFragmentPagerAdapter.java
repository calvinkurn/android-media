package com.tokopedia.movies.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.tokopedia.movies.view.fragment.CategoryFragment;
import com.tokopedia.movies.view.viewmodel.CategoryViewModel;

import java.util.List;

/**
 * Created by ashwanityagi on 21/11/17.
 */

public class CategoryFragmentPagerAdapter extends FragmentStatePagerAdapter {
    List<CategoryViewModel> categoryList;

    public CategoryFragmentPagerAdapter(FragmentManager fm, List<CategoryViewModel> categoryList) {
        super(fm);
        this.categoryList = categoryList;
    }

    @Override
    public Fragment getItem(int position) {

        return CategoryFragment.newInstance(categoryList.get(position));
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categoryList.get(position).getTitle();
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_UNCHANGED;
    }
}
