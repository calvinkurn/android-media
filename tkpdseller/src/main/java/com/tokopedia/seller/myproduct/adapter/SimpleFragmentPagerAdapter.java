package com.tokopedia.seller.myproduct.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tokopedia.seller.myproduct.fragment.ChooserFragment;
import com.tokopedia.seller.myproduct.model.SimpleTextModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.normansyah on 12/9/15.
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private int size;
    private List<SimpleTextModel> simpleTextModels;
    private List<String> pageTitles;
    private Context context;

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context, List<String> pageTitles) {
        this(fm, context, pageTitles, new ArrayList<SimpleTextModel>());
    }

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context, List<String> pageTitles,  List<SimpleTextModel> data){
        super(fm);
        this.context = context;
        simpleTextModels = data;
        this.pageTitles = pageTitles;
        this.size = pageTitles.size();
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Fragment getItem(int position) {
        return ChooserFragment.newInstance(simpleTextModels);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }



}
