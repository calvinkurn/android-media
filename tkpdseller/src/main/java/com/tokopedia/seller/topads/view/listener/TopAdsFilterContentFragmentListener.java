package com.tokopedia.seller.topads.view.listener;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsFilterContentFragment;

/**
 * Created by normansyahputa on 5/29/17.
 */

public interface TopAdsFilterContentFragmentListener {
    String getTitle(Context context);

    Intent addResult(Intent intent);

    boolean isActive();

    void setActive(boolean active);

    void setCallback(TopAdsFilterContentFragment.Callback callback);
}
