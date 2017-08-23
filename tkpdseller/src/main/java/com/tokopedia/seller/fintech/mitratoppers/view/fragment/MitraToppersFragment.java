package com.tokopedia.seller.fintech.mitratoppers.view.fragment;

import android.net.Uri;

import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.seller.base.view.fragment.BaseWebViewFragment;

/**
 * Created by nathan on 8/18/17.
 */

public class MitraToppersFragment extends BaseWebViewFragment {

    private static final String MITRA_TOPPERS_URL = "https://www.tokopedia.com/mitra-toppers/";

    @Override
    protected String getUrl() {
        return URLGenerator.generateURLSessionLogin(Uri.encode(MITRA_TOPPERS_URL), getActivity());
    }
}
