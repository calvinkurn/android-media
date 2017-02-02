package com.tokopedia.transaction.webview;

import android.app.Fragment;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.transaction.R;

/**
 * Created by kris on 1/13/17. Tokopedia
 */

public class WalletWebView extends TActivity implements FragmentGeneralWebView.OnFragmentInteractionListener {

    private String Url;

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Url = getIntent().getExtras().getString("url");
        inflateView(R.layout.wallet_webview);
        showFragmentWebView();
    }

    private void showFragmentWebView() {
        Fragment fragment = FragmentGeneralWebView.createInstance(Url);
        getFragmentManager().beginTransaction().add(R.id.main_view, fragment).commit();
    }


    @Override
    public void onWebViewSuccessLoad() {

    }

    @Override
    public void onWebViewErrorLoad() {

    }

    @Override
    public void onWebViewProgressLoad() {

    }
}
