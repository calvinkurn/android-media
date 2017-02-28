package com.tokopedia.seller.topads.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 2/23/17.
 */

public class TopAdsCustomRadioButton extends RadioButton {

    private List<OnCheckedChangeListener> listeners = new ArrayList<>();

    public TopAdsCustomRadioButton(Context context) {
        super(context);
    }

    public TopAdsCustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopAdsCustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TopAdsCustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void addOnCheckedChangeListener(OnCheckedChangeListener listener){
        listeners.add(listener);
    }

    public void removeOnCheckedChangeListener(OnCheckedChangeListener listener){
        listeners.remove(listener);
    }

    public void clearOnCheckedChangeListener(){
        listeners.clear();
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if(listeners != null) {
            for (OnCheckedChangeListener listener : listeners) {
                listener.onCheckedChanged(this, checked);
            }
        }
    }
}
