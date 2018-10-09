package com.tokopedia.seller.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.seller.R;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class LabelSwitch extends BaseCustomView {
    private TextView titleTextView;
    private TextView switchTextView;
    private SwitchCompat switchStatus;

    private CompoundButton.OnCheckedChangeListener listener;
    private String titleText;
    private boolean switchEnable;
    private TextView summaryTextView;
    private float titleTextSize;
    private int titleTextColor;
    private float contentTextSize;
    private int contentTextColor;

    public LabelSwitch(Context context) {
        super(context);
        init();
    }

    public LabelSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LabelSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.LabelSwitch);
        try {
            titleText = styledAttributes.getString(R.styleable.LabelSwitch_ls_title);
            switchEnable = styledAttributes.getBoolean(R.styleable.LabelSwitch_ls_switch_enable, true);
            titleTextSize = styledAttributes.getDimension(R.styleable.LabelSwitch_ls_title_text_size, 0);
            titleTextColor = styledAttributes.getColor(R.styleable.LabelSwitch_ls_title_color, 0);
            contentTextSize = styledAttributes.getDimension(R.styleable.LabelSwitch_ls_content_text_size, 0);
            contentTextColor = styledAttributes.getColor(R.styleable.LabelSwitch_ls_content_color, 0);
        } finally {
            styledAttributes.recycle();
        }
        if (titleTextSize!= 0) {
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        }
        if (titleTextColor!= 0) {
            titleTextView.setTextColor(titleTextColor);
        }

        if (contentTextSize!= 0) {
            summaryTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentTextSize);
        }
        if (contentTextColor!= 0) {
            summaryTextView.setTextColor(contentTextColor);
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTextView.setText(titleText);
        switchStatus.setEnabled(switchEnable);
        switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null) {
                    listener.onCheckedChanged(buttonView, isChecked);
                }
            }
        });
        this.getRootView().setClickable(true);
        this.getRootView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                switchStatus.setChecked(!switchStatus.isChecked());
            }
        });
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_label_switch, this);
        titleTextView = (TextView) view.findViewById(R.id.title_text_view);
        summaryTextView = (TextView) view.findViewById(R.id.summary_text_view);
        switchTextView = (TextView) view.findViewById(R.id.switch_text_view);
        switchStatus = (SwitchCompat) view.findViewById(R.id.switch_status);
    }

    public void setTitle(String textTitle) {
        titleTextView.setText(textTitle);
        invalidate();
        requestLayout();
    }

    public void setSummary(String summaryText) {
        if (TextUtils.isEmpty(summaryText)) {
            summaryTextView.setVisibility(View.GONE);
        } else {
            summaryTextView.setText(summaryText);
            summaryTextView.setVisibility(View.VISIBLE);
        }
    }

    public String getTitle() {
        return titleTextView.getText().toString();
    }

    public void setSwitchStatusText(String textTitle) {
        switchTextView.setText(textTitle);
        invalidate();
        requestLayout();
    }

    public boolean isChecked() {
        return switchStatus.isChecked();
    }

    public void setChecked(boolean isChecked) {
        switchStatus.setChecked(isChecked);
        invalidate();
        requestLayout();
    }

    public void setCheckedNoListener(boolean isChecked) {
        CompoundButton.OnCheckedChangeListener tempListener = this.listener;
        this.listener = null;
        switchStatus.setChecked(isChecked);
        this.listener = tempListener;
        invalidate();
        requestLayout();
    }

    public void setSwitchEnabled(boolean enabled) {
        if (switchStatus.isEnabled() != enabled) {
            switchStatus.setEnabled(enabled);
        }
    }

    public void setListenerValue(CompoundButton.OnCheckedChangeListener listener) {
        this.listener = listener;
    }
}
