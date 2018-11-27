package com.tokopedia.track.components;

import android.content.Context;

public abstract class ContextAnalytics {
    private final Context context;

    public ContextAnalytics(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
