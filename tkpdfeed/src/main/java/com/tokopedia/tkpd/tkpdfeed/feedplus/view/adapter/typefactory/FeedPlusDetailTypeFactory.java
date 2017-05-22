package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.FeedDetailViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductCardHeaderViewModel;

/**
 * @author by nisie on 5/18/17.
 */

public interface FeedPlusDetailTypeFactory {

    int type(FeedDetailViewModel viewModel);

    int type(ProductCardHeaderViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
