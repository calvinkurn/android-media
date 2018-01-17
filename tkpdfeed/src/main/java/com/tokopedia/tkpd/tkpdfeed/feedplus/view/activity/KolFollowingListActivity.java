package com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.KolFollowingListFragment;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingListActivity extends TActivity implements HasComponent {
    public static final String ARGS_USER_ID = "user_id";

    public static Intent getCallingIntent(Context context, int userId) {
        Intent intent = new Intent(context, KolFollowingListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_USER_ID, userId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        initView();
    }

    private void initView() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);

        if (fragment == null)
            fragment = KolFollowingListFragment.createInstance(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                fragment).commit();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public Object getComponent() {
        return getApplicationComponent();
    }

}
