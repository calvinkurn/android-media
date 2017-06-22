package com.tokopedia.seller.topads.keyword.view.listener;

import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.base.view.listener.BaseListViewListener;

import java.util.List;

/**
 * Created by zulfikarrahman on 5/23/17.
 */

public interface TopAdsKeywordNewChooseGroupView extends BaseListViewListener {
    void onGetGroupAdList(List<GroupAd> groupAds);

    void onGetGroupAdListError(Throwable e);
}
