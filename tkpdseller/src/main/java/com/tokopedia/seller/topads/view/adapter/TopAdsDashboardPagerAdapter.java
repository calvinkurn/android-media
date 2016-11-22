package com.tokopedia.seller.topads.view.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.tokopedia.core.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.inboxmessage.InboxMessageConstant;

import java.util.List;

/**
 * Created by Nisie on 5/10/16.
 */
public class TopAdsDashboardPagerAdapter extends FragmentPagerAdapter implements InboxMessageConstant {
    private List<Fragment> fragmentList;


    public TopAdsDashboardPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return MainApplication.getAppContext().getString(R.string.title_inbox_message);
            case 1:
                return MainApplication.getAppContext().getString(R.string.title_inbox_sent);
            case 2:
                return MainApplication.getAppContext().getString(R.string.title_inbox_archive);
            default:
                return MainApplication.getAppContext().getString(R.string.title_inbox_trash);
        }
    }

    public Fragment getItemByTag(String tag) {
        switch (tag) {
            case MESSAGE_ALL:
                return getItem(0);
            case MESSAGE_SENT:
                return getItem(1);
            case MESSAGE_ARCHIVE:
                return getItem(2);
            case MESSAGE_TRASH:
                return getItem(3);
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
