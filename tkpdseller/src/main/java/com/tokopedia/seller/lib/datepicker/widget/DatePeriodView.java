package com.tokopedia.seller.lib.datepicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.datepicker.DatePickerUtils;

import java.util.Date;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class DatePeriodView extends BaseCustomView {

    private RadioButton radioButton;
    private TextView titleTextView;
    private TextView contentTextView;

    private String title;
    private String content;
    private boolean selected;
    private Date startDate;
    private Date endDate;

    public DatePeriodView(Context context) {
        super(context);
        init();
    }

    public DatePeriodView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DatePeriodView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.DatePeriodView);
        try {
            title = styledAttributes.getString(R.styleable.DatePeriodView_date_period_title);
            content = styledAttributes.getString(R.styleable.DatePeriodView_date_period_content);
            selected = styledAttributes.getBoolean(R.styleable.DatePeriodView_date_period_selected, false);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_date_picker_periode, this);
        titleTextView = (TextView) view.findViewById(R.id.text_view_title);
        contentTextView = (TextView) view.findViewById(R.id.text_view_content);
        radioButton = (RadioButton) view.findViewById(R.id.radio_button);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTextView.setText(title);
        contentTextView.setText(content);
        radioButton.setChecked(selected);
        invalidate();
        requestLayout();
    }

    public void setTitle(String textTitle) {
        titleTextView.setText(textTitle);
        invalidate();
        requestLayout();
    }

    public void setContent(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        contentTextView.setText(DatePickerUtils.getRangeDateFormatted(contentTextView.getContext(), startDate, endDate));
        invalidate();
        requestLayout();
    }

    public void setChecked(boolean checked) {
        selected = checked;
        radioButton.setChecked(checked);
        invalidate();
        requestLayout();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        radioButton.setOnClickListener(onClickListener);
    }
}