package com.tokopedia.session.activation.view.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.session.R;
import com.tokopedia.session.activation.view.fragment.ChangeEmailFragment;

/**
 * Created by nisie on 4/18/17.
 */

public class ChangeEmailActivity extends BasePresenterActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        ChangeEmailFragment fragment = ChangeEmailFragment.createInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();

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

    public static Intent getCallingIntent(Context context, String oldEmail) {
        Intent intent = new Intent(context, ChangeEmailActivity.class);
        intent.putExtra(ChangeEmailFragment.EXTRA_EMAIL, oldEmail);
        return intent;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
