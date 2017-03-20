package com.tokopedia.seller.app;

import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public abstract class BaseDiActivity<P, C> extends BasePresenterActivity<P> implements HasComponent<C> {

    protected C component;

    @Override
    public C getComponent() {
        return component;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        component = initComponent();
        presenter = getPresenter();

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void setActionVar() {

    }

    protected abstract C initComponent();

    protected abstract P getPresenter();
}
