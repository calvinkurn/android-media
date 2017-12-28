package com.tokopedia.inbox.rescenter.createreso.view.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.FreeReturnActivityListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.FreeReturnActivityPresenter;

/**
 * Created by yoasfs on 24/08/17.
 */

public class FreeReturnActivity extends BasePresenterActivity<FreeReturnActivityListener.Presenter>
        implements FreeReturnActivityListener.View, HasComponent,
        FragmentGeneralWebView.OnFragmentInteractionListener {

    private static final String PARAM_URL = "param_url";

    private String url;

    public static Intent newInstance(Context context,
                                     String url) {
        Intent intent = new Intent(context, FreeReturnActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_URL, url);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        url = extras.getString(PARAM_URL);
    }

    @Override
    protected void initialPresenter() {
        presenter = new FreeReturnActivityPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_solution_list;
    }

    @Override
    protected void initView() {
        presenter.initFragment(url);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void inflateFragment(Fragment fragment, String TAG) {
        if (getFragmentManager().findFragmentByTag(TAG) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, TAG)
                    .commit();
        }
    }

    @Override
    public Object getComponent() {
        return null;
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
