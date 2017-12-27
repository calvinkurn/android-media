package com.tokopedia.discovery.newdynamicfilter.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.discovery.newdynamicfilter.adapter.viewholder.DynamicFilterViewHolder;

/**
 * Created by henrypriyono on 8/11/17.
 */

public interface DynamicFilterTypeFactory {
    int type(Filter filter);

    DynamicFilterViewHolder createViewHolder(View view, int viewType);
}
