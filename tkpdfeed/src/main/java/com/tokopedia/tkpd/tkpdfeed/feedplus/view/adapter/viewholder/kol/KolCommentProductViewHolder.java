package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.kol;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolComment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolCommentProductViewModel;

/**
 * @author by nisie on 11/2/17.
 */

public class KolCommentProductViewHolder extends AbstractViewHolder<KolCommentProductViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.kol_comment_product;
    private final KolComment.View viewListener;

    public KolCommentProductViewHolder(View itemView, KolComment.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
    }


    @Override
    public void bind(KolCommentProductViewModel element) {

    }
}
