package com.tokopedia.shop.info.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment;
import com.tokopedia.shop.note.view.fragment.ShopNoteListFragment;
import com.tokopedia.shop.product.view.fragment.ShopProductListLimitedFragment;
import com.tokopedia.shop.product.view.widget.ShopPagePromoWebView;

/**
 * Created by normansyahputa on 3/13/18.
 */

public class ShopInfoPagerAdapter extends FragmentStatePagerAdapter {
    private final String[] titleArray;
    private SparseArrayCompat<Fragment> registeredFragments = new SparseArrayCompat<>();

    public ShopInfoPagerAdapter(FragmentManager fragmentManager, String[] titleArray) {
        super(fragmentManager);
        this.titleArray = titleArray;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
            case 1:
                return titleArray[position];
            default:
                return super.getPageTitle(position);
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ShopInfoFragment.createInstance();
            case 1:
                return ShopNoteListFragment.createInstance();
        }
        return ShopInfoFragment.createInstance();
    }

    @Override
    public int getCount() {
        return titleArray.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object o = super.instantiateItem(container, position);
        registeredFragments.put(position, (Fragment) o);
        return o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
