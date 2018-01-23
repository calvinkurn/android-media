package com.tokopedia.mitratoppers.dashboard;

import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.network.URLGenerator;
import com.tokopedia.mitratoppers.common.constant.MitraToppersBaseURL;

/**
 * Created by nathan on 8/18/17.
 */

public class MitraToppersDashboardFragment extends BaseWebViewFragment {

    private UserSession userSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession = ((AbstractionRouter) getActivity().getApplication()).getSession();
    }

    @Override
    protected String getUrl() {
        String gcmId = userSession.getFcmId();
        String userId = userSession.getUserId();
        return URLGenerator.generateURLSessionLogin(
                Uri.encode(getMitraToppersUrl()),
                gcmId,
                userId);
    }

    @Override
    protected String getUserIdForHeader() {
        return userSession.getUserId();
    }

    private static String getMitraToppersUrl() {
        return MitraToppersBaseURL.WEB_DOMAIN + MitraToppersBaseURL.PATH_MITRA_TOPPERS;
    }
}
