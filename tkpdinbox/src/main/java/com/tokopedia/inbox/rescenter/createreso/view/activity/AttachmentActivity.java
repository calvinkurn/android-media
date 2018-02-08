package com.tokopedia.inbox.rescenter.createreso.view.activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.AttachmentActivityListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.AttachmentActivityPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;

/**
 * Created by yoasfs on 30/08/17.
 */

public class AttachmentActivity extends BasePresenterActivity<AttachmentActivityListener.Presenter> implements AttachmentActivityListener.View {

    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";

    ResultViewModel resultViewModel;

    @Override
    public void inflateFragment(Fragment fragment, String TAG) {
        if (getFragmentManager().findFragmentByTag(TAG) != null) {
            getFragmentManager().beginTransaction()
                    .replace(com.tokopedia.core.R.id.container,
                            getFragmentManager().findFragmentByTag(TAG))
                    .commit();
        } else {
            getFragmentManager().beginTransaction()
                    .add(com.tokopedia.core.R.id.container, fragment, TAG)
                    .commit();
        }
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        resultViewModel = extras.getParcelable(RESULT_VIEW_MODEL_DATA);
    }

    @Override
    protected void initialPresenter() {
        presenter = new AttachmentActivityPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_attachment;
    }

    @Override
    protected void initView() {
        presenter.initFragment(resultViewModel);
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
}
