package com.tokopedia.discovery.newdiscovery.base;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterFlagSelectedModel;

import java.util.ArrayList;

/**
 * Created by henrypriyono on 12/03/18.
 */

public interface BottomSheetListener {
    void loadFilterItems(ArrayList<Filter> filters, FilterFlagSelectedModel filterFlagSelectedModel);

    void setFilterResultCount(String formattedResultCount);

    void closeFilterBottomSheet();

    boolean isBottomSheetShown();

    void launchFilterBottomSheet();
}
