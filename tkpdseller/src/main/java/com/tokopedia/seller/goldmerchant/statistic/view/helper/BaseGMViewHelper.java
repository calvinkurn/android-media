package com.tokopedia.seller.goldmerchant.statistic.view.helper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by normansyahputa on 7/10/17.
 */

public abstract class BaseGMViewHelper<U> {
    protected Context context;

    public BaseGMViewHelper(@Nullable Context context) {
        this.context = context;
    }

    public abstract void initView(@Nullable View itemView);

    public abstract void bind(@Nullable U data);
}
