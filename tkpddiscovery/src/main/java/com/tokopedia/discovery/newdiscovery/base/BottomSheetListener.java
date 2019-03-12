package com.tokopedia.discovery.newdiscovery.base;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterFlagSelectedModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by henrypriyono on 12/03/18.
 */

public interface BottomSheetListener {
    void loadFilterItems(ArrayList<Filter> filters, HashMap<String, String> searchParameter);

    void setFilterResultCount(String formattedResultCount);

    void closeFilterBottomSheet();

    boolean isBottomSheetShown();

    void launchFilterBottomSheet();
}
