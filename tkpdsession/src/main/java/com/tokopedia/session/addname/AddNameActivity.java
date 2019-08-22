package com.tokopedia.session.addname;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.session.addname.fragment.AddNameFragment;

/**
 * @author by nisie on 8/10/18.
 */
public class AddNameActivity extends BaseSimpleActivity implements HasComponent {

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, AddNameActivity.class);
        return intent;
    }

    @DeepLink({ApplinkConst.ADD_NAME_PROFILE})
    public static Intent getCallingApplinkIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        Intent intent = newInstance(context);
        return intent.setData(uri.build());
    }

    @Override
    protected Fragment getNewFragment() {
        return AddNameFragment.newInstance(new Bundle());
    }

    @Override
    public AppComponent getComponent() {
        return ((MainApplication) getApplication()).getAppComponent();
    }
}
