package com.tokopedia.seller.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class DateLabelView extends BaseCustomView {

    private static final String RANGE_DATE_FORMAT = "dd MMM yyyy";
    private static final String RANGE_DATE_FORMAT_WITHOUT_YEAR = "dd MMM";

    private TextView dateRangeTextView;
    private String dateRange;
    private Date startDate;
    private Date endDate;

    public DateLabelView(Context context) {
        super(context);
        init();
    }

    public DateLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DateLabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.LabelView);
        try {
            dateRange = styledAttributes.getString(R.styleable.LabelView_title);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_date_label_view, this);
        dateRangeTextView = (TextView) view.findViewById(R.id.text_view_date_range);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dateRangeTextView.setText(dateRange);
        invalidate();
        requestLayout();
    }

    public void setTitle(String textTitle) {
        dateRangeTextView.setText(textTitle);
        invalidate();
        requestLayout();
    }

    public void setDate(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        updateDateView();
    }

    private void updateDateView() {
        dateRangeTextView.setText(getRangeDateFormated());
        invalidate();
        requestLayout();
    }

    private String getRangeDateFormated() {
        if (startDate == null || endDate == null) {
            return "";
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        String startDateFormatted = new SimpleDateFormat(RANGE_DATE_FORMAT, Locale.ENGLISH).format(startDate);
        String endDateFormatted = new SimpleDateFormat(RANGE_DATE_FORMAT, Locale.ENGLISH).format(endDate);
        if (startDateFormatted.equalsIgnoreCase(endDateFormatted)) {
            return endDateFormatted;
        }
        if (startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR)) {
            startDateFormatted = new SimpleDateFormat(RANGE_DATE_FORMAT_WITHOUT_YEAR, Locale.ENGLISH).format(startDate);
        }
        return dateRangeTextView.getContext().getString(R.string.top_ads_range_date_text, startDateFormatted, endDateFormatted);
    }
}