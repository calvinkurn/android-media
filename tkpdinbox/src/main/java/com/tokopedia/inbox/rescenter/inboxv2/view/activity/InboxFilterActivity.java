package com.tokopedia.inbox.rescenter.inboxv2.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.customView.TextDrawable;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.inboxv2.view.fragment.InboxFilterFragment;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.ResoInboxFilterModel;

/**
 * Created by yfsx on 24/01/18.
 */

public class InboxFilterActivity extends BasePresenterActivity implements HasComponent {
    public static final String TAG = InboxFilterFragment.class.getSimpleName();
    public static final String PARAM_FILTER_MODEL = "view_model";

    private ResoInboxFilterModel inboxFilterModel;

    public static Intent newInstance(Context context, ResoInboxFilterModel filterModel) {
        Intent intent = new Intent(context, InboxFilterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_FILTER_MODEL, filterModel);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle("Filter");
    }

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
        return R.layout.activity_detail_res_chat;
    }

    @Override
    protected void initView() {
        Fragment fragment = InboxFilterFragment.getFragmentInstance(getIntent().getExtras());
        inboxFilterModel = getIntent().getParcelableExtra(PARAM_FILTER_MODEL);
        if (getSupportFragmentManager().findFragmentByTag(TAG) != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(com.tokopedia.core.R.id.container,
                            getSupportFragmentManager().findFragmentByTag(TAG))
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(com.tokopedia.core.R.id.container, fragment, TAG)
                    .commit();
        }
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
    public Object getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, R.id.action_reset, 0, "");
        MenuItem menuItem = menu.findItem(R.id.action_reset); // OR THIS
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(getDetailMenuItem());
        return true;
    }

    private Drawable getDetailMenuItem() {
        TextDrawable drawable = new TextDrawable(this);
        drawable.setText(getResources().getString(R.string.string_reset));
        drawable.setTextColor(Color.parseColor("#42b549"));
        return drawable;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reset) {
            Fragment fragment = InboxFilterFragment.getResetFragmentInstance(inboxFilterModel);
            getSupportFragmentManager().beginTransaction()
                    .add(com.tokopedia.core.R.id.container, fragment, TAG)
                    .commit();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    public void getBottomBackSheetActivityTransition() {
        overridePendingTransition(R.anim.push_down, R.anim.pull_up);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getBottomBackSheetActivityTransition();
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_x_black);
    }
}
