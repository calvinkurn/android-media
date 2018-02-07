package com.tokopedia.tkpdstream.common;

import android.os.Bundle;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;


/**
 * @author by nisie on 2/1/18.
 */

public abstract class BaseStreamActivity extends BaseSimpleActivity {

    private StreamComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
    }

    private void initInjector() {
//        getStreamComponent().inject(this);
    }

//    protected StreamComponent getStreamComponent() {
//        if (component == null) {
//            component = DaggerStreamComponent.builder().baseAppComponent(
//                    ((BaseMainApplication) getApplication()).getBaseAppComponent()).build();
//        }
//        return component;
//    }
}
