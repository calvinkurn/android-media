package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistHeaderViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistProductViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.SearchEmptyViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactory;

/**
 * Created by hangnadi on 10/8/17.
 */

public interface HotlistTypeFactory extends SearchSectionTypeFactory {

    int type(HotlistHeaderViewModel header);

    int type(HotlistProductViewModel product);

    int type(SearchEmptyViewModel empty);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
