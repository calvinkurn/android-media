package com.tokopedia.seller.base.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.presentation.CustomerView;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public interface BaseListViewListener<T> extends CustomerView {

    void onSearchLoaded(@NonNull List<T> list, int totalItem);

    void onLoadSearchError(Throwable t);

}