package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.recentview;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.RecentViewDetailProductViewModel;

/**
 * @author by nisie on 7/4/17.
 */

public interface RecentViewTypeFactory {

    int type(RecentViewDetailProductViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
