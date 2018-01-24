package com.tokopedia.topads.dashboard.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.network.URLGenerator;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.data.model.data.DataCredit;

public class TopAdsPaymentCreditFragment extends BaseWebViewFragment {

    private DataCredit dataCredit;
    private UserSession userSession;

    public static TopAdsPaymentCreditFragment createInstance() {
        TopAdsPaymentCreditFragment fragment = new TopAdsPaymentCreditFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataCredit = getActivity().getIntent().getParcelableExtra(TopAdsConstant.EXTRA_CREDIT);
        userSession = ((AbstractionRouter) getActivity().getApplication()).getSession();
    }

    @Override
    protected String getUrl() {
        return URLGenerator.generateURLSessionLogin(
                Uri.encode(dataCredit.getProductUrl()),
                userSession.getFcmId(),
                userSession.getUserId());
    }

    @Nullable
    @Override
    protected String getUserIdForHeader() {
        return userSession.getUserId();
    }

    @Override
    protected boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if (getActivity() != null && ((TkpdCoreRouter) getActivity().getApplication())
                .isSupportedDelegateDeepLink(url)) {
            ((TkpdCoreRouter) getActivity().getApplication())
                    .actionNavigateByApplinksUrl(getActivity(), url, new Bundle());
            return true;
        }
        return false;
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}