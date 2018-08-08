package com.tokopedia.tkpdpdp.estimasiongkir;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

public class RatesEstimationDetailActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context){
        return new Intent(context, RatesEstimationDetailActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return RatesEstimationDetailFragment.createInstance();
    }
}
