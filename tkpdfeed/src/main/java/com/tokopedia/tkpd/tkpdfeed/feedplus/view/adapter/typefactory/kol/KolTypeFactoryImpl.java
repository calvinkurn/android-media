package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.kol;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.kol.KolCommentHeaderViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.kol.KolCommentViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolComment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolCommentHeaderViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolCommentViewModel;

/**
 * @author by nisie on 10/31/17.
 */

public class KolTypeFactoryImpl extends BaseAdapterTypeFactory
        implements KolTypeFactory {

    private final KolComment.View viewListener;

    public KolTypeFactoryImpl(KolComment.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int type(KolCommentViewModel viewModel) {
        return KolCommentViewHolder.LAYOUT;
    }

    @Override
    public int type(KolCommentHeaderViewModel viewModel) {
        return KolCommentHeaderViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == KolCommentViewHolder.LAYOUT)
            viewHolder = new KolCommentViewHolder(view, viewListener);
        else if (type == KolCommentHeaderViewHolder.LAYOUT)
            viewHolder = new KolCommentHeaderViewHolder(view, viewListener);
        else viewHolder = super.createViewHolder(view, type);

        return viewHolder;
    }
}
