package com.tokopedia.discovery.newdiscovery.hotlist.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.discovery.R;

/**
 * Created by nakama on 12/5/17.
 */

public class HotlistPromoView extends BaseCustomView {

    public HotlistPromoView(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.customview_hotlist_promo, this);

    }

    private void init(AttributeSet attrs) {
        init();
    }

    public HotlistPromoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public HotlistPromoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
}
