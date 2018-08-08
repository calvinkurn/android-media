package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;

/**
 * Created by normansyahputa on 1/4/18.
 */

public class ProductAddInfoFragment extends BaseDaggerFragment {

    private static final String STATIC_ASSET_LOCATION = "STATIC_ASSET_LOCATION";
    private WebView webView;

    public static ProductAddInfoFragment create(String staticAssetLoc){
        Bundle bundle = new Bundle();
        bundle.putString(STATIC_ASSET_LOCATION, staticAssetLoc);
        ProductAddInfoFragment productAddInfoFragment
                = new ProductAddInfoFragment();
        productAddInfoFragment.setArguments(bundle);
        return productAddInfoFragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_reputation_info, container, false);
        webView = root.findViewById(R.id.webview_reputation_info);

        webView.getSettings().setJavaScriptEnabled(true);

        if(!TextUtils.isEmpty(getArguments().getString(STATIC_ASSET_LOCATION))){
            webView.loadUrl(getArguments().getString(STATIC_ASSET_LOCATION));
        }

        return root;
    }
}
