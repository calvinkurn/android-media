package com.tokopedia.session.addchangepassword.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.session.R;
import com.tokopedia.session.addchangepassword.view.fragment.AddPasswordFragment;

/**
 * @author by yfsx on 23/03/18.
 */

public class AddPasswordActivity extends BasePresenterActivity implements HasComponent {

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context,  AddPasswordActivity.class);
        intent.putExtras(new Bundle());
        return intent;
    }

    @Override
    public Object getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        String TAG = AddPasswordFragment.class.getSimpleName();
        AddPasswordFragment fragment = AddPasswordFragment.newInstance(new Bundle());
        if (getSupportFragmentManager().findFragmentByTag(TAG) != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(com.tokopedia.core.R.id.container,
                            getSupportFragmentManager().findFragmentByTag(TAG))
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(com.tokopedia.core.R.id.container, fragment, TAG)
                    .commit();
        }
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
