package com.tokopedia.seller.shopsettings.shipping.customview;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Kris on 6/6/2016.
 * TOKOPEDIA
 */
public abstract class EditShippingCourierView<D, L> extends LinearLayout{

    public EditShippingCourierView(Context context) {
        super(context);
        initView(context);
    }

    public EditShippingCourierView(Context context, AttributeSet attrs){
        super(context, attrs);
        initView(context);
    }

    public EditShippingCourierView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        initView(context);
    }

    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        bindView(view);
    }

    protected abstract int getLayoutView();

    protected abstract void bindView (View view);

    public abstract void renderData(@NonNull D data, int index);

    public abstract void setViewListener(L mainViewListener);

}
