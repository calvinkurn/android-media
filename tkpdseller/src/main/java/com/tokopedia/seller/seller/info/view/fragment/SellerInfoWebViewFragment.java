package com.tokopedia.seller.seller.info.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.seller.base.view.fragment.BaseWebViewFragment;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;

/**
 * Created by normansyahputa on 12/5/17.
 */
public class SellerInfoWebViewFragment extends BaseWebViewFragment {

    public static final String EXTRA_URL = "EXTRA_URL";
    private String extraUrl;

    private UserSession userSession;

    public static Fragment newInstance(String url){
        Fragment fragment = new SellerInfoWebViewFragment();
        Bundle argument = new Bundle();
        argument.putString(EXTRA_URL, url);
        fragment.setArguments(argument);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession = ((AbstractionRouter) getActivity().getApplication()).getSession();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if(savedInstanceState==null){
            if(getArguments()!= null){
                extraUrl = getArguments().getString(EXTRA_URL);
            }
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected String getUrl() {
        return URLGenerator.generateURLSessionLogin(Uri.encode(extraUrl), getActivity());
    }

    @Override
    protected String getUserIdForHeader() {
        return userSession.getUserId();
    }
}
