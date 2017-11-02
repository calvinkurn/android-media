package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.kol.KolTypeFactory;

/**
 * @author by nisie on 11/2/17.
 */

public class KolCommentHeaderViewModel extends KolCommentViewModel implements
        Visitable<KolTypeFactory> {
    boolean canLoadMore;

    public KolCommentHeaderViewModel(String avatarUrl, String name, String review, String time,
                                     boolean canLoadMore) {
        super(avatarUrl, name, review, time);
        this.canLoadMore = canLoadMore;
    }

    @Override
    public int type(KolTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }
}
