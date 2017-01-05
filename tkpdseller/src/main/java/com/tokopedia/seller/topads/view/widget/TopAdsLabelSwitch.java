package com.tokopedia.seller.topads.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class TopAdsLabelSwitch extends CardView {
    @BindView(R2.id.title_item)
    TextView title;

    @BindView(R2.id.desc_switch)
    TextView desc_switch;

    @BindView(R2.id.switch_active_ads)
    SwitchCompat switchCompat;

    private ListenerSwitchValue listenerSwitchValue;
    private String titleText;

    public TopAdsLabelSwitch(Context context) {
        super(context);
        init();
    }

    public TopAdsLabelSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TopAdsLabelSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.TopAdsLabelView);

        try {
            titleText = styledAttributes.getString(R.styleable.TopAdsLabelView_title);
        }finally {
            styledAttributes.recycle();
        }


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        title.setText(titleText);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(listenerSwitchValue!=null){
                    listenerSwitchValue.onValueChange(buttonView, isChecked);
                }

                if(isChecked){
                    desc_switch.setText(R.string.label_active_topads);
                }else{
                    desc_switch.setText(R.string.label_non_active_topads);
                }
            }
        });
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.item_detail_topads_switch_layout, null);
        ButterKnife.bind(this, view);
        addView(view);
    }

    public void setTitle(String textTitle){
        title.setText(textTitle);
        invalidate();
        requestLayout();
    }

    public String getTitle(){
        return title.getText().toString();
    }

    public boolean getValue(){
        return switchCompat.isChecked();
    }

    public void setValue(boolean isChecked){
        switchCompat.setChecked(isChecked);
        invalidate();
        requestLayout();
    }

    public void setListenerValue(ListenerSwitchValue listenerSwitchValue){
        this.listenerSwitchValue = listenerSwitchValue;
    }

    public interface ListenerSwitchValue{
        void onValueChange(CompoundButton buttonView, boolean isChecked);
    }
}
