package com.tokopedia.profile.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tokopedia.profile.view.viewmodel.ProfileSectionItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvinatin on 21/02/18.
 */

public class ProfileTabPagerAdapter extends FragmentPagerAdapter {

    private List<ProfileSectionItem> profileSectionItemList = new ArrayList<>();

    public ProfileTabPagerAdapter(FragmentManager fm){
        super(fm);
    }

    public void setItemList(List<ProfileSectionItem> profileSectionItemList){
        this.profileSectionItemList = profileSectionItemList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return profileSectionItemList.get(position).getFragment();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return profileSectionItemList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return profileSectionItemList.get(position).getTitle();
    }
}
