package com.tokopedia.discovery.newdiscovery.base;

import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;

/**
 * Created by henrypriyono on 10/30/17.
 */

public interface RedirectionListener {
    void performNewProductSearch(String queryParams);
    void showSearchInputView();
}
