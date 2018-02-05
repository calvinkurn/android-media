package com.tokopedia.movies.view.adapter;

import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.movies.R2;

import butterknife.BindView;

/**
 * Created by naveengoyal on 1/4/18.
 */

public class CalendarItemHolder {
    @BindView(R2.id.calendar_layout)
    LinearLayout calendarLayout;
    @BindView(R2.id.tv_date)
    TextView tvDate;
    @BindView(R2.id.tv_day)
    TextView tvDay;

    public void setTvDate(String date) {
        tvDate.setText(date);
    }

    public void setTvDay(String day) {
        tvDay.setText(day);
    }
}
