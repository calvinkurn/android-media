package com.tokopedia.topads.dashboard.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;

/**
 * Created by Nathaniel on 1/27/2017.
 */
public abstract class TopAdsFilterContentFragment<P> extends BasePresenterFragment<P> {

    public interface Callback {

        void onStatusChanged(boolean active);

    }

    public abstract String getTitle(Context context);

    public abstract Intent addResult(Intent intent);

    protected Callback callback;

    /**
     * Sign for title filter list
     */
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle bundle) {

    }

    @Override
    public void onRestoreState(Bundle bundle) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle bundle) {

    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }
}