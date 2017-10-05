package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.recentview;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.RecentView;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.feeddetail.EmptyFeedDetailViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.recentview.RecentViewDetailProductViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.RecentViewDetailProductViewModel;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewTypeFactoryImpl extends BaseAdapterTypeFactory
        implements RecentViewTypeFactory {

    private final RecentView.View viewListener;

    public RecentViewTypeFactoryImpl(RecentView.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int type(RecentViewDetailProductViewModel viewModel) {
        return RecentViewDetailProductViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyModel emptyModel) {
        return EmptyFeedDetailViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == RecentViewDetailProductViewHolder.LAYOUT)
            viewHolder = new RecentViewDetailProductViewHolder(view, viewListener);
        else viewHolder = super.createViewHolder(view, type);
        return viewHolder;
    }


}
