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

    private TextView contentTextView;
    private String content;
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
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.DatePickerLabelView);
        try {
            content = styledAttributes.getString(R.styleable.DatePickerLabelView_date_picker_content);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_date_label_view, this);
        contentTextView = (TextView) view.findViewById(R.id.text_view_content);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentTextView.setText(content);
        invalidate();
        requestLayout();
    }

    public void setContent(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        updateDateView();
    }

    private void updateDateView() {
        contentTextView.setText(DatePickerUtils.getRangeDateFormatted(contentTextView.getContext(), startDate, endDate));
        invalidate();
        requestLayout();
    }
}