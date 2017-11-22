package com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper;

import android.text.TextUtils;

import com.tkpdfeed.feeds.FollowKol;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FollowKolDomain;

import rx.functions.Func1;

/**
 * @author by nisie on 11/3/17.
 */

public class FollowKolMapper implements Func1<FollowKol.Data, FollowKolDomain> {

    @Override
    public FollowKolDomain call(FollowKol.Data data) {
        if (data != null
                && data.do_follow_kol() != null
                && data.do_follow_kol().data() != null
                && (data.do_follow_kol().error() == null
                || TextUtils.isEmpty(data.do_follow_kol().error()))) {
            return convertToDomain(data.do_follow_kol().data());
        } else if (data != null
                && data.do_follow_kol() != null
                && (data.do_follow_kol().error() != null
                && !TextUtils.isEmpty(data.do_follow_kol().error()))) {
            throw new ErrorMessageException(data.do_follow_kol().error());
        } else {
            throw new ErrorMessageException(MainApplication.getAppContext().getString(R.string
                    .default_request_error_unknown));
        }
    }

    private FollowKolDomain convertToDomain(FollowKol.Data.Data1 data) {
        return new FollowKolDomain(data.status() == null ? 0 : data.status());
    }
}
