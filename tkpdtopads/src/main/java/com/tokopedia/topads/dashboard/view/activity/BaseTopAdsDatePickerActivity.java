package com.tokopedia.topads.dashboard.view.activity;

import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.base.view.activity.BaseToolbarActivity;

import butterknife.ButterKnife;

/**
 * Created by normansyahputa on 10/29/17.
 * <p>
 * this is just for layering {@link BasePresenterActivity}
 */

public abstract class BaseTopAdsDatePickerActivity<P> extends BaseToolbarActivity {
    @Override
    protected void setupFragment(Bundle savedInstanceState) { /* leave empty */}

    @Override
    protected int getLayoutRes() {
        return getLayoutId();
    }

    private static final String TAG = BasePresenterActivity.class.getSimpleName();

    protected P presenter;
    protected boolean isAfterRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isAfterRotate(savedInstanceState)) {
            setupVar(savedInstanceState);
        } else {
            setupVar();
        }
        if (getIntent().getExtras() != null) {
            setupBundlePass(getIntent().getExtras());
        }
        if (getIntent().getData() != null) {
            setupURIPass(getIntent().getData());
        }
        initialPresenter();

        initViews();
        initView();
        initVar();
        setViewListener();
        setActionVar();
    }

    protected void setupVar(Bundle savedInstanceState) { /*leave empty*/ }

    protected void initViews() {
        ButterKnife.bind(this);
    }

    protected void setupVar() { /*leave empty*/ }

    protected boolean isAfterRotate(Bundle savedInstanceState) {
        return isAfterRotate = savedInstanceState != null;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Kalau memang ada uri data dari intent, mau diapain?
     *
     * @param data uri data dari bundle intent
     */
    protected void setupURIPass(Uri data) {
    }

    /**
     * Kalalu memang ada bundle data dari intent, mau diapain?
     *
     * @param extras bundle extras dari intent
     */
    protected void setupBundlePass(Bundle extras) {
    }

    /**
     * Initial presenter, sesuai dengan Type param class
     */
    protected void initialPresenter() {
    }

    /**
     * Layout id untuk si activity
     *
     * @return Res layout id
     */
    protected int getLayoutId() {
        return 0;
    }

    /**
     * initial wiew atau widget
     */
    protected void initView() {
    }

    /**
     * view / widgetnya mau diapain?
     */
    protected void setViewListener() {
    }

    /**
     * initail variabel di activity
     */
    protected void initVar() {
    }

    /**
     * variable nya mau diapain?
     */
    protected void setActionVar() {
    }
}
