package com.tokopedia.loyalty.view.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.tokopedia.loyalty.view.data.PromoMenuData;
import com.tokopedia.loyalty.view.fragment.PromoListFragment;

import java.util.List;

/**
 * Created by nabillasabbaha on 1/5/18.
 */

public class PromoPagerAdapter extends FragmentStatePagerAdapter {

    private List<PromoMenuData> promoMenuDataList;

    public PromoPagerAdapter(FragmentManager fm, List<PromoMenuData> promoMenuDataList) {
        super(fm);
        this.promoMenuDataList = promoMenuDataList;
    }

    @Override
    public Fragment getItem(int position) {
        return PromoListFragment.newInstance(promoMenuDataList.get(position));
    }

    @Override
    public int getCount() {
        return promoMenuDataList.size();
    }
}
