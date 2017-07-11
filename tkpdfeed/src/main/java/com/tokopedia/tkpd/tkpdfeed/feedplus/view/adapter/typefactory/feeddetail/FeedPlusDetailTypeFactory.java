package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feeddetail;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.feeddetail.SingleFeedDetailViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.feeddetail.FeedDetailViewModel;

/**
 * @author by nisie on 5/18/17.
 */

public interface FeedPlusDetailTypeFactory {

    int type(FeedDetailViewModel viewModel);

    int type(FeedDetailHeaderViewModel viewModel);

    int type(SingleFeedDetailViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
