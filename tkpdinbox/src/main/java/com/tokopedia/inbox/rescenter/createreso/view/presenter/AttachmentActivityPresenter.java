package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;

import com.tokopedia.inbox.rescenter.createreso.view.fragment.AttachmentFragment;
import com.tokopedia.inbox.rescenter.createreso.view.listener.AttachmentActivityListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;

/**
 * Created by yoasfs on 30/08/17.
 */

public class AttachmentActivityPresenter implements AttachmentActivityListener.Presenter {

    AttachmentActivityListener.View mainView;
    Context context;

    public AttachmentActivityPresenter(AttachmentActivityListener.View mainView, Context context) {
        this.mainView = mainView;
        this.context = context;
    }

    @Override
    public void attachView(AttachmentActivityListener.View view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void initFragment(ResultViewModel resultViewModel) {
        mainView.inflateFragment(AttachmentFragment.newInstance(resultViewModel),
                AttachmentFragment.class.getSimpleName());
    }
}
