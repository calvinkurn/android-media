package com.tokopedia.seller.topads.keyword.view.presenter;

import com.tokopedia.seller.topads.keyword.view.listener.TopAdsKeywordNewChooseGroupView;

/**
 * Created by zulfikarrahman on 5/23/17.
 */

public abstract class TopAdsKeywordNewChooseGroupPresenter<T extends TopAdsKeywordNewChooseGroupView> extends TopAdsKeywordListPresenter<T> {
    public abstract void searchGroupName(String keyword);
}
