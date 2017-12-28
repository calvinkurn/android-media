package com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper;

import android.text.TextUtils;

import com.tkpdfeed.feeds.GetKolFollowingList;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.KolFollowingDomain;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * @author by nisie on 11/2/17.
 */

public class KolFollowingMapper implements Func1<GetKolFollowingList.Data, List<KolFollowingDomain>> {

    @Override
    public List<KolFollowingDomain> call(GetKolFollowingList.Data data) {
        if (data != null
                && data.get_user_kol_following() != null
                && data.get_user_kol_following().users() != null
                && data.get_user_kol_following().users().size() != 0
                && (data.get_user_kol_following().error() == null
                || TextUtils.isEmpty(data.get_user_kol_following().error()))) {
            return convertToDomain(data.get_user_kol_following().users());
        } else if (data != null
                && data.get_user_kol_following() != null
                && (data.get_user_kol_following().error() != null
                && !TextUtils.isEmpty(data.get_user_kol_following().error()))) {
            throw new ErrorMessageException(data.get_user_kol_following().error());
        } else {
            throw new ErrorMessageException(MainApplication.getAppContext().getString(R.string
                    .default_request_error_unknown));
        }
    }

    private List<KolFollowingDomain> convertToDomain(List<GetKolFollowingList.Data.User> dataList) {
        List<KolFollowingDomain> domainList = new ArrayList<>();
        for (GetKolFollowingList.Data.User data : dataList) {
            KolFollowingDomain domain = new KolFollowingDomain(
                    data.id(),
                    data.name(),
                    data.userUrl(),
                    true);
            domainList.add(domain);
        }
        return domainList;
    }
}
