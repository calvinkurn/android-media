package com.tokopedia.seller.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class LabelSwitch extends FrameLayout {
    TextView titleTextView;
    TextView switchTextView;
    SwitchCompat switchStatus;

    private CompoundButton.OnCheckedChangeListener listener;
    private String titleText;
    private boolean switchEnable;

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
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.LabelView);
        try {
            titleText = styledAttributes.getString(R.styleable.LabelView_title);
            switchEnable = styledAttributes.getBoolean(R.styleable.LabelView_switch_enable, false);
        } finally {
            styledAttributes.recycle();
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
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.item_detail_topads_switch_layout, this);
        titleTextView = (TextView) view.findViewById(R.id.title_text_view);
        switchTextView = (TextView) view.findViewById(R.id.switch_text_view);
        switchStatus = (SwitchCompat) view.findViewById(R.id.switch_status);
    }

    public void setTitle(String textTitle) {
        titleTextView.setText(textTitle);
        invalidate();
        requestLayout();
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

    public void setSwitchEnabled(boolean enabled) {
        if (switchStatus.isEnabled() != enabled) {
            switchStatus.setEnabled(enabled);
        }
    }

    public void setListenerValue(CompoundButton.OnCheckedChangeListener listener) {
        this.listener = listener;
    }
}
