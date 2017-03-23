package com.tokopedia.seller.app;

import android.app.Activity;
import android.os.Bundle;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;

import javax.inject.Inject;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public abstract class BaseDiFragment<C extends BaseFragmentComponent, P extends BaseDiPresenter> extends BasePresenterFragment<P> {

    protected C component;
    @Inject
    protected P diPresenter;

    @SuppressWarnings("unchecked")
    protected <CA> CA getComponent(Class<CA> componentType) {
        return componentType.cast(((HasComponent<CA>) getActivity()).getComponent());
    }

    @Override
    protected final void initialPresenter() {
        component = initInjection();
        component.inject(this);
        presenter = diPresenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unsubscribeOnDestroy();
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }


    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    protected abstract C initInjection();

    protected AppComponent getApplicationComponent(){
        return ((BaseActivity) getActivity()).getApplicationComponent();
    }

}
