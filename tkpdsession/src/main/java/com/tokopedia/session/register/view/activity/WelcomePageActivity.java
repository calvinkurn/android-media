package com.tokopedia.session.register.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.session.register.view.fragment.WelcomePageFragment;

/**
 * @author by yfsx on 14/03/18.
 */

public class WelcomePageActivity extends BaseSimpleActivity {

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, WelcomePageActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return WelcomePageFragment.newInstance();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
