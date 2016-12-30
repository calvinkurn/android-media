package com.tokopedia.seller.topads.view.custom;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class ItemDetailTopAdsSwitch extends View {
    @BindView(R2.id.title_item)
    TextView title;

    @BindView(R2.id.desc_switch)
    TextView desc_switch;

    @BindView(R2.id.switch_active_ads)
    SwitchCompat switchCompat;

    private ListenerSwitchValue listenerSwitchValue;

    public ItemDetailTopAdsSwitch(Context context) {
        super(context);
        init();
    }

    public ItemDetailTopAdsSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemDetailTopAdsSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ItemDetailTopAdsSwitch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.item_detail_topads_switch_layout, null);
        ButterKnife.bind(this, view);
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
    }

    public void setTitle(String textTitle){
        title.setText(textTitle);
    }

    public String getTitle(){
        return title.getText().toString();
    }

    public boolean getValue(){
        return switchCompat.isChecked();
    }

    public void setValue(boolean isChecked){
        switchCompat.setChecked(isChecked);
    }

    public void setListenerValue(ListenerSwitchValue listenerSwitchValue){
        this.listenerSwitchValue = listenerSwitchValue;
    }

    public interface ListenerSwitchValue{
        void onValueChange(CompoundButton buttonView, boolean isChecked);
    }
}
