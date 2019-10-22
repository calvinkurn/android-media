package com.tokopedia.inbox.rescenter.create.customview;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;


/**
 * Created on 6/16/16.
 */
public abstract class BaseView<Data, Listener> extends FrameLayout {

    public static final String TAG = BaseView.class.getSimpleName();

    protected Listener listener;

    public BaseView(Context context) {
        super(context);
        initView(context);
        parseAttribute(context, null);
        setViewListener();
    }


    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        parseAttribute(context, attrs);
        setViewListener();
    }

    public abstract void setListener(Listener listener);

    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutView(), this, true);
    }

    protected abstract int getLayoutView();

    protected abstract void parseAttribute(Context context, AttributeSet attrs);

    protected abstract void setViewListener();

    public abstract void renderData(@NonNull Data data);

}
