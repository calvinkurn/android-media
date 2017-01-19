package com.tokopedia.seller.gmstat.views.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.tokopedia.seller.gmstat.views.CustomFragment;
import com.tokopedia.seller.gmstat.views.PeriodFragment;

import static com.tokopedia.seller.gmstat.views.GMStatActivityFragment.getDateFormat;

/**
 * Created by normansyahputa on 1/19/17.
 */

public class SetDatePagerAdapter extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "PERIODE", "KUSTOM" };
    private Context context;
    private boolean isGM;
    private int lastSelectionPeriod;
    private long sDate;
    private long eDate;

    public SetDatePagerAdapter(FragmentManager fm, Context context, boolean isGM,
                               int lastSelectionPeriod, long sDate, long eDate) {
        super(fm);
        this.context = context;
        this.isGM = isGM;
        this.lastSelectionPeriod = lastSelectionPeriod;
        this.sDate = sDate;
        this.eDate = eDate;
        Log.d("MNORMANSYAH", "sDate "+getDateFormat(sDate)+" eDate "+getDateFormat(eDate));
    }

    @Override
    public int getCount() {
        if(!isGM)
            return 1;
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PeriodFragment.newInstance(lastSelectionPeriod);
            case 1:
            default:
                return CustomFragment.newInstance(sDate,eDate);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
