package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.createreso.view.listener.AttachmentFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;

/**
 * Created by yoasfs on 30/08/17.
 */

public class AttachmentFragment extends BaseDaggerFragment implements AttachmentFragmentListener.View {

    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";

    private ResultViewModel resultViewModel;

    public static AttachmentFragment newInstance(ResultViewModel resultViewModel) {
        AttachmentFragment fragment = new AttachmentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RESULT_VIEW_MODEL_DATA, resultViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        resultViewModel = arguments.getParcelable(RESULT_VIEW_MODEL_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_attachment;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    public void populateDataToView() {

    }


    @Override
    protected void setViewListener() {

    }
}
