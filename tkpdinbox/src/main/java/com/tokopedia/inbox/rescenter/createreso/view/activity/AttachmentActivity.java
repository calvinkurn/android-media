package com.tokopedia.inbox.rescenter.createreso.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.AttachmentFragment;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;

/**
 * Created by yoasfs on 30/08/17.
 */

public class AttachmentActivity extends BaseSimpleActivity {

    public static Intent getInstance(Context context, ResultViewModel resultViewModel) {
        Intent intent = new Intent(context, AttachmentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(RESULT_VIEW_MODEL_DATA, resultViewModel);
        intent.putExtras(bundle);
        return intent;
    }

    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";

    @Override
    protected Fragment getNewFragment() {
        return AttachmentFragment.newInstance(getIntent().getExtras());
    }
}
