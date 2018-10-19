package com.tokopedia.seller.seller.info.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;

/**
 * Created by normansyahputa on 12/5/17.
 */
public class SellerInfoWebViewFragment extends BaseSessionWebViewFragment {

    public static SellerInfoWebViewFragment newInstance(String url) {
        SellerInfoWebViewFragment fragment = new SellerInfoWebViewFragment();
        Bundle argument = new Bundle();
        argument.putString(BaseSessionWebViewFragment.ARGS_URL, url);
        fragment.setArguments(argument);
        return fragment;
    }

}
