package com.tokopedia.home.beranda.listener;

import android.content.Context;

import com.tokopedia.core.base.adapter.Visitable;

import java.util.ArrayList;

/**
 * Created by henrypriyono on 1/12/18.
 */

public interface HomeFeedListener {
    void onGoToProductDetailFromInspiration(String productId,
                                            String imageSource,
                                            String name,
                                            String price);

    void onRetryClicked();
    void onShowRetryGetFeed();
    void onSuccessGetFeed(ArrayList<Visitable> visitables);
    void updateCursor(String currentCursor);
    void updateCursorNoNextPageFeed();

    Context getActivity();
}
