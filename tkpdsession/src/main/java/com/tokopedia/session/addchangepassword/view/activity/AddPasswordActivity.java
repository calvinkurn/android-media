package com.tokopedia.session.addchangepassword.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.session.addchangepassword.view.fragment.AddPasswordFragment;

/**
 * @author by yfsx on 23/03/18.
 */

public class AddPasswordActivity extends BaseSimpleActivity {

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context,  AddPasswordActivity.class);
        intent.putExtras(new Bundle());
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return AddPasswordFragment.newInstance(new Bundle());
    }

}
