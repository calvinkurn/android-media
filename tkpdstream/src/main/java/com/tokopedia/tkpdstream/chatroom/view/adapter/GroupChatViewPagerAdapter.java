package com.tokopedia.tkpdstream.chatroom.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * @author by nisie on 3/20/18.
 */

public class GroupChatViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();

    public GroupChatViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public static GroupChatViewPagerAdapter createInstance(FragmentManager fm) {
        return new GroupChatViewPagerAdapter(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof Fragment) {
            return fragments.indexOf(object);
        } else {
            return POSITION_NONE;

        }
    }
}
