package com.tokopedia.inbox.rescenter.detailv2.view;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailViewListener;
import com.tokopedia.inbox.rescenter.detailv2.view.presenter.DetailResCenterImpl;
import com.tokopedia.inbox.rescenter.detailv2.view.presenter.DetailResCenterPresenter;

/**
 * Created by hangnadi on 3/8/17.
 */

public class DetailResCenterActivity extends BasePresenterActivity<DetailResCenterPresenter>
    implements DetailViewListener, HasComponent {

    private static final String EXTRA_PARAM_RESOLUTION_CENTER_DETAIL = "resolution_id";
    private static final String TAG_DETAIL_FRAGMENT_RESOLUTION_CENTER = DetailResCenterFragment.class.getSimpleName();

    private String resolutionID;
    private Fragment detailResCenterFragment;

    public static Intent newInstance(Context context, String resolutionID) {
        Intent intent = new Intent(context, DetailResCenterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_RESOLUTION_CENTER_DETAIL, resolutionID);
        intent.putExtras(bundle);
        return intent;
    }

    @DeepLink(Constants.Applinks.RESCENTER)
    public static Intent getCallingIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, DetailResCenterActivity.class);
        String resoId = bundle.getString(EXTRA_PARAM_RESOLUTION_CENTER_DETAIL, "");
        intent.putExtra(EXTRA_PARAM_RESOLUTION_CENTER_DETAIL, resoId);
        return intent.putExtras(bundle);
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
}
