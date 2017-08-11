package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feeddetail;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.feeddetail.EmptyFeedDetailViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.feeddetail.FeedDetailHeaderViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.feeddetail.FeedDetailViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.feeddetail.SingleFeedDetailViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.feeddetail.SingleFeedDetailViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.feeddetail.FeedDetailViewModel;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailTypeFactoryImpl extends BaseAdapterTypeFactory
        implements FeedPlusDetailTypeFactory {

    private final FeedPlusDetail.View viewListener;

    public FeedPlusDetailTypeFactoryImpl(FeedPlusDetail.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int type(FeedDetailViewModel viewModel) {
        return FeedDetailViewHolder.LAYOUT;
    }

    @Override
    public int type(FeedDetailHeaderViewModel viewModel) {
        return FeedDetailHeaderViewHolder.LAYOUT;
    }

    @Override
    public int type(SingleFeedDetailViewModel viewModel) {
        return SingleFeedDetailViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyModel emptyModel) {
        return EmptyFeedDetailViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == SingleFeedDetailViewHolder.LAYOUT)
            viewHolder = new SingleFeedDetailViewHolder(view, viewListener);
        else if (type == FeedDetailViewHolder.LAYOUT)
            viewHolder = new FeedDetailViewHolder(view, viewListener);
        else if (type == FeedDetailHeaderViewHolder.LAYOUT)
            viewHolder = new FeedDetailHeaderViewHolder(view, viewListener);
        else if (type == EmptyFeedDetailViewHolder.LAYOUT)
            viewHolder = new EmptyFeedDetailViewHolder(view, viewListener);
        else viewHolder = super.createViewHolder(view, type);
        return viewHolder;
    }
}
