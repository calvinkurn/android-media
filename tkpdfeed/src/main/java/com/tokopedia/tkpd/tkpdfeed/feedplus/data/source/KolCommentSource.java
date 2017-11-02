package com.tokopedia.tkpd.tkpdfeed.feedplus.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.KolCommentMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolComments;

import rx.Observable;

/**
 * @author by nisie on 10/31/17.
 */

public class KolCommentSource {
    KolCommentMapper kolCommentMapper;

    public KolCommentSource(KolCommentMapper kolCommentMapper) {
        this.kolCommentMapper = kolCommentMapper;
    }

    public Observable<KolComments> getComments(RequestParams requestParams) {
        return null;
    }
}
