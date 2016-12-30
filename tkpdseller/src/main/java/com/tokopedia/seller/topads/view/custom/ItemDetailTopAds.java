package com.tokopedia.seller.topads.view.custom;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class ItemDetailTopAds extends View {

    @BindView(R2.id.title_item)
    TextView title;

    @BindView(R2.id.value_item)
    TextView value;

    public ItemDetailTopAds(Context context) {
        super(context);
        init();
    }

    public ItemDetailTopAds(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemDetailTopAds(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ItemDetailTopAds(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        View view = inflate(getContext(), R.layout.item_detail_topads_layout, null);
        ButterKnife.bind(this, view);
    }

    public void setTitle(String textTitle){
        title.setText(textTitle);
    }

    public void setValue(String textValue){
        value.setText(textValue);
    }

    public void setColorValue(@ColorInt int colorValue){
        value.setTextColor(colorValue);
    }

    public String getTitle(){
        return title.getText().toString();
    }

    public String getValue(){
        return value.getText().toString();
    }
}
