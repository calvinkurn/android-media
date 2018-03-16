package com.tokopedia.tkpdtrain.search.presentation;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;

/**
 * Created by nabillasabbaha on 3/15/18.
 */

public class TrainSearchRecyclerView extends VerticalRecyclerView {

    public TrainSearchRecyclerView(Context context) {
        super(context);
        clearItemDecoration();
    }

    public TrainSearchRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        clearItemDecoration();
    }

    public TrainSearchRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        clearItemDecoration();
    }
}
