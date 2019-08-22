package com.tokopedia.session.changename.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.session.changename.view.fragment.ChangeNameFragment;

/**
 * @author by yfsx on 22/03/18.
 */

public class ChangeNameActivity extends BaseSimpleActivity {

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, ChangeNameActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return ChangeNameFragment.newInstance(new Bundle());
    }
}
