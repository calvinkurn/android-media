package com.tokopedia.inbox.rescenter.detailv2.view.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.DetailResCenterFragment;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailViewListener;
import com.tokopedia.inbox.rescenter.detailv2.view.presenter.DetailResCenterImpl;
import com.tokopedia.inbox.rescenter.detailv2.view.presenter.DetailResCenterPresenter;
import com.tokopedia.inbox.rescenter.inbox.activity.InboxResCenterActivity;

/**
 * Created by hangnadi on 3/8/17.
 */

public class DetailResCenterActivity extends BasePresenterActivity<DetailResCenterPresenter>
        implements DetailViewListener, HasComponent {

    public static final String PARAM_SHOP_NAME = "shop_name";
    public static final String PARAM_USER_NAME = "user_name";
    public static final String PARAM_IS_SELLER = "is_seller";
    private static final String EXTRA_PARAM_RESOLUTION_CENTER_DETAIL = "resolution_id";
    private static final String TAG_DETAIL_FRAGMENT_RESOLUTION_CENTER = DetailResCenterFragment.class.getSimpleName();
    private String resolutionID;
    private Fragment detailResCenterFragment;
    private String shopName;
    private String userName;
    private boolean isSeller;

    public static Intent newInstance(Context context, String resolutionID) {
        Intent intent = new Intent(context, DetailResCenterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_RESOLUTION_CENTER_DETAIL, resolutionID);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newBuyerInstance(Context context, String resolutionId, String shopName) {
        Intent intent = new Intent(context, DetailResCenterActivity.class);
        intent.putExtra(EXTRA_PARAM_RESOLUTION_CENTER_DETAIL, resolutionId);
        intent.putExtra(PARAM_SHOP_NAME, shopName);
        intent.putExtra(PARAM_IS_SELLER, false);
        return intent;
    }

    public static Intent newSellerInstance(Context context, String resolutionId, String username) {
        Intent intent = new Intent(context, DetailResCenterActivity.class);
        intent.putExtra(EXTRA_PARAM_RESOLUTION_CENTER_DETAIL, resolutionId);
        intent.putExtra(PARAM_USER_NAME, username);
        intent.putExtra(PARAM_IS_SELLER, true);
        return intent;
    }


    @DeepLink(Constants.Applinks.RESCENTER)
    public static TaskStackBuilder getCallingIntent(Context context, Bundle bundle) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Intent parentIntent = InboxResCenterActivity.createIntent(context);
        Intent destinationIntent = new Intent(context, DetailResCenterActivity.class);
        String resoId = bundle.getString(EXTRA_PARAM_RESOLUTION_CENTER_DETAIL, "");
        destinationIntent.putExtra(EXTRA_PARAM_RESOLUTION_CENTER_DETAIL, resoId);
        destinationIntent.putExtras(bundle);
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(destinationIntent);
        return taskStackBuilder;
    }

    @Override
    public String getResolutionID() {
        return resolutionID;
    }

    @Override
    public void setResolutionID(String resolutionID) {
        this.resolutionID = resolutionID;
    }

    @Override
    public Fragment getDetailResCenterFragment() {
        return detailResCenterFragment;
    }

    @Override
    public void setDetailResCenterFragment(Fragment detailResCenterFragment) {
        this.detailResCenterFragment = detailResCenterFragment;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        setResolutionID(extras.getString(EXTRA_PARAM_RESOLUTION_CENTER_DETAIL));
        isSeller = extras.getBoolean(PARAM_IS_SELLER);
        if (isSeller) {
            userName = extras.getString(PARAM_USER_NAME);
        } else {
            shopName = extras.getString(PARAM_SHOP_NAME);
        }
    }

    @Override
    protected void initialPresenter() {
        presenter = new DetailResCenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_res_center_detail;
    }

    @Override
    protected void initView() {
        presenter.generateDetailResCenterFragment();
        if (isSeller) {
            toolbar.setTitle(getString(R.string.complaint_from) + " " + userName);
        } else {
            toolbar.setTitle(getString(R.string.complaint_to) + " " + shopName);
        }
        inflateFragment();
    }

    @Override
    public void inflateFragment() {
        if (getFragmentManager().findFragmentByTag(TAG_DETAIL_FRAGMENT_RESOLUTION_CENTER) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, getDetailResCenterFragment(), TAG_DETAIL_FRAGMENT_RESOLUTION_CENTER)
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
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        getBottomBackSheetActivityTransition();
    }

    public void getBottomBackSheetActivityTransition() {
        overridePendingTransition(R.anim.push_down, R.anim.pull_up);
    }
}
