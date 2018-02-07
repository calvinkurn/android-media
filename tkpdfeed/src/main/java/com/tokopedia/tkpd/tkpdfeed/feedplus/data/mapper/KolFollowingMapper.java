package com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper;

import android.text.TextUtils;

import com.tkpdfeed.feeds.GetKolFollowingList;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.KolFollowingDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.KolFollowingResultDomain;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * @author by nisie on 11/2/17.
 */

public class KolFollowingMapper implements Func1<GetKolFollowingList.Data, KolFollowingResultDomain> {

    @Override
    public KolFollowingResultDomain call(GetKolFollowingList.Data data) {
        if (data != null
                && data.get_user_kol_following() != null
                && (data.get_user_kol_following().error() == null
                || TextUtils.isEmpty(data.get_user_kol_following().error()))) {
            return convertToDomain(data.get_user_kol_following());
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

    private KolFollowingResultDomain convertToDomain(GetKolFollowingList.Data.Get_user_kol_following user_kol_following) {
        return new KolFollowingResultDomain(
                !TextUtils.isEmpty(user_kol_following.lastCursor()),
                user_kol_following.lastCursor(),
                user_kol_following.users() != null ?
                        mappingKolFollowingDomain(user_kol_following.users()) :
                        new ArrayList<KolFollowingDomain>(),
                "",
                ""
        );
    }

    private List<KolFollowingDomain> mappingKolFollowingDomain(List<GetKolFollowingList.Data.User> userList) {
        List<KolFollowingDomain> domainList = new ArrayList<>();
        for (GetKolFollowingList.Data.User user : userList) {
            KolFollowingDomain domain = new KolFollowingDomain(
                    user.id(),
                    user.name(),
                    user.photo(),
                    user.userApplink(),
                    user.userUrl(),
                    user.isInfluencer());
            domainList.add(domain);
        }
        return domainList;
    }
}
