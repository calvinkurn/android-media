package com.tokopedia.seller.fintech.mitratoppers.view.fragment;

import android.net.Uri;

import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.base.view.fragment.BaseWebViewFragment;

/**
 * Created by nathan on 8/18/17.
 */

public class MitraToppersFragment extends BaseWebViewFragment {

    private static final String MITRA_TOPPERS_URL = TkpdBaseURL.WEB_DOMAIN + TkpdBaseURL.FinTech.PATH_MITRA_TOPPERS_WEBVIEW;

    @Override
    protected String getUrl() {
        return URLGenerator.generateURLSessionLogin(Uri.encode(MITRA_TOPPERS_URL), getActivity());
    }
}
