package com.tokopedia.seller.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.datepicker.utils.DatePickerUtils;

import java.util.Date;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class DateLabelView extends BaseCustomView {

    private TextView dateTextView;
    private TextView comparedDateTextView;
    private String content;

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
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.DatePickerLabelView);
        try {
            content = styledAttributes.getString(R.styleable.DatePickerLabelView_date_picker_content);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_date_label_view, this);
        dateTextView = (TextView) view.findViewById(R.id.text_view_date);
        comparedDateTextView = (TextView) view.findViewById(R.id.text_view_compared_date);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dateTextView.setText(content);
        invalidate();
        requestLayout();
    }

    public void setDate(Date startDate, Date endDate) {
        setDate(startDate.getTime(), endDate.getTime());
    }

    public void setDate(long startDate, long endDate) {
        dateTextView.setText(DatePickerUtils.getRangeDateFormatted(dateTextView.getContext(), startDate, endDate));
        invalidate();
        requestLayout();
    }

    public void setComparedDate(long startDate, long endDate) {
        comparedDateTextView.setText(DatePickerUtils.getRangeDateFormatted(dateTextView.getContext(), startDate, endDate));
        invalidate();
        requestLayout();
    }

    public void setComparedDateVisibility(int visibility) {
        comparedDateTextView.setVisibility(visibility);
    }
}