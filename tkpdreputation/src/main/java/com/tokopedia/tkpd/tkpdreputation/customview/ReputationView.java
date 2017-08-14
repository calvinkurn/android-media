package com.tokopedia.tkpd.tkpdreputation.customview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * @author by nisie on 8/11/17.
 */

public class ReputationView extends LinearLayout {

    public ReputationView(Context context) {
        super(context);
    }

    public ReputationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ReputationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ReputationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


}
