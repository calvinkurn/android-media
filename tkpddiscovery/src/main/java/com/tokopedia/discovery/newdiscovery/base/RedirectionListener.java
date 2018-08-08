package com.tokopedia.discovery.newdiscovery.base;

/**
 * Created by henrypriyono on 10/30/17.
 */

public interface RedirectionListener {
    void performNewProductSearch(String query, boolean forceSearch);
    void showSearchInputView();
}
