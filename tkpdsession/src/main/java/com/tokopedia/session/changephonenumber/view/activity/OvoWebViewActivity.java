package com.tokopedia.session.changephonenumber.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseWebViewActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;

/**
 * @author by alvinatin on 03/10/18.
 */

public class OvoWebViewActivity extends BaseWebViewActivity {

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, OvoWebViewActivity.class);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return BaseSessionWebViewFragment.newInstance("https://17-feature-m-staging.tokopedia.com/user/profile/edit/phone");
    }

    @Nullable
    @Override
    protected Intent getContactUsIntent() {
        return null;
    }
}
