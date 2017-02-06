package com.tokopedia.seller.topads.lib.datepicker;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.tokopedia.core.listener.GlobalMainTabSelectedListener;

/**
 * Created by normansyahputa on 1/24/17.
 */

public class DatePickerTabListener extends GlobalMainTabSelectedListener {

    private DatePickerList mDatePickerList;

    public DatePickerTabListener(ViewPager mViewPager) {
        super(mViewPager);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        super.onTabSelected(tab);
        if (mDatePickerList != null) {
            mDatePickerList.onSelected(tab.getPosition());
        }
    }

    public void setDatePickerList(DatePickerList datePickerList) {
        this.mDatePickerList = datePickerList;
    }

    public interface DatePickerList {
        void onSelected(int positon);
    }
}
