package com.tokopedia.tkpd.tkpdreputation.inbox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationDetailFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailPassModel;

import static com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationFormActivity.ARGS_REPUTATION_ID;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailActivity extends BasePresenterActivity implements HasComponent {

    public static final String ARGS_POSITION = "ARGS_POSITION";
    public static final String ARGS_TAB = "ARGS_TAB";
    public static final String ARGS_PASS_DATA = "ARGS_PASS_DATA";

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        InboxReputationDetailPassModel model = null;
        if (getIntent().getExtras().getParcelable
                (ARGS_PASS_DATA) != null) {
            model = getIntent().getExtras().getParcelable
                    (ARGS_PASS_DATA);
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(InboxReputationDetailFragment
                .class.getSimpleName());
        if (fragment == null) {
            fragment = InboxReputationDetailFragment.createInstance(model);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,
                fragment,
                fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    public static Intent getCallingIntent(Context context, InboxReputationDetailPassModel inboxReputationDetailPassModel, int adapterPosition, int tab) {
        Intent intent = new Intent(context, InboxReputationDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(InboxReputationDetailActivity.ARGS_PASS_DATA, inboxReputationDetailPassModel);
        bundle.putInt(InboxReputationDetailActivity.ARGS_POSITION, adapterPosition);
        bundle.putInt(InboxReputationDetailActivity.ARGS_TAB, tab);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (getIntent().getExtras().getParcelable(InboxReputationDetailActivity.ARGS_PASS_DATA) !=
                null) {
            InboxReputationDetailPassModel model = getIntent().getExtras().getParcelable
                    (InboxReputationDetailActivity.ARGS_PASS_DATA);

            if (toolbar != null && model.getInvoice() != null)
                toolbar.setTitle(model.getInvoice());
            if (toolbar != null && model.getCreateTime() != null)
                toolbar.setSubtitle(model.getCreateTime());
        }
    }


}
