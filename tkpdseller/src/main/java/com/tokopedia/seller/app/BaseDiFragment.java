package com.tokopedia.seller.app;

import android.app.Activity;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.di.component.HasComponent;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public abstract class BaseDiFragment<P extends BaseDiPresenter> extends BasePresenterFragment<P> {

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    @Override
    protected void initialPresenter() {
        initInjection();
        presenter = getPresenter();
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
    protected void setActionVar() {

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

    protected abstract void initInjection();

    protected abstract P getPresenter();

}
