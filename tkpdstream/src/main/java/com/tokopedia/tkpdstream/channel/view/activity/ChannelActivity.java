package com.tokopedia.tkpdstream.channel.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.tkpdstream.channel.view.fragment.ChannelFragment;
import com.tokopedia.tkpdstream.common.BaseStreamActivity;

public class ChannelActivity extends BaseStreamActivity {

    public static final int RESULT_ERROR_LOGIN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return ChannelFragment.createInstance();
    }

}
