package com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper;

import android.text.TextUtils;

import com.tkpdfeed.feeds.LikeKolPost;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.LikeKolDomain;

import rx.functions.Func1;

/**
 * @author by nisie on 11/3/17.
 */

public class LikeKolMapper implements Func1<LikeKolPost.Data, LikeKolDomain> {
    @Override
    public LikeKolDomain call(LikeKolPost.Data data) {
        if (data != null
                && data.do_like_kol_post() != null
                && data.do_like_kol_post().data() != null
                && (data.do_like_kol_post().error() == null
                || TextUtils.isEmpty(data.do_like_kol_post().error()))) {
            return convertToDomain(data.do_like_kol_post().data());
        } else if (data != null
                && data.do_like_kol_post() != null
                && (data.do_like_kol_post().error() != null
                && !TextUtils.isEmpty(data.do_like_kol_post().error()))) {
            throw new ErrorMessageException(data.do_like_kol_post().error());
        } else {
            throw new ErrorMessageException(MainApplication.getAppContext().getString(R.string
                    .default_request_error_unknown));
        }

    }

    private LikeKolDomain convertToDomain(LikeKolPost.Data.Data1 data) {
        return new LikeKolDomain(data.success()== null? false : true);
    }
}
