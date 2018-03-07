package com.tokopedia.tkpdstream.channel.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tkpdstream.channel.view.fragment.ChannelFragment;

public class ChannelActivity extends BaseSimpleActivity {

    public static final int RESULT_ERROR_LOGIN = 101;
    public static final String RESULT_MESSAGE = "result_message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        return ChannelFragment.createInstance(bundle);
    }

}
