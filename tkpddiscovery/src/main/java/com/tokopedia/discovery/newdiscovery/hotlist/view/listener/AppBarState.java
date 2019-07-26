package com.tokopedia.discovery.newdiscovery.hotlist.view.listener;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({AppBarState.EXPANDED, AppBarState.COLLAPSED, AppBarState.IDLE})
public @interface AppBarState {
    int EXPANDED = -1;
    int COLLAPSED = 1;
    int IDLE = 0;
}
