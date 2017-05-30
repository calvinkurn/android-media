package com.tokopedia.seller.topads.keyword.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsKeywordDetailViewListener;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailViewListener;

import java.util.Date;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public abstract class TopadsKeywordDetailPresenter<T extends TopAdsKeywordDetailViewListener> extends BaseDaggerPresenter<T> {
    public abstract void refreshAd(Date startDate, Date endDate, String id);

    public abstract void deleteAd(String id);

    public abstract void unSubscribe();
}
