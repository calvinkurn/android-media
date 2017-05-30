package com.tokopedia.seller.topads.keyword.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsKeywordEditDetailView;
import com.tokopedia.seller.topads.keyword.view.model.TopAdsKeywordEditDetailViewModel;

/**
 * @author sebastianuskh on 5/26/17.
 */

public abstract class TopAdsKeywordEditDetailPresenter extends BaseDaggerPresenter<TopAdsKeywordEditDetailView>{
    public abstract void editTopAdsKeywordDetail(TopAdsKeywordEditDetailViewModel topAdsKeywordEditDetailViewModel);
}
