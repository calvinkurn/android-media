package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.listener.AttachmentFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.Attachment;

/**
 * Created by yoasfs on 31/08/17.
 */

public class AttachmentFragmentPresenter extends BaseDaggerPresenter<AttachmentFragmentListener.View> implements AttachmentFragmentListener.Presenter {

    private Context context;
    private ResultViewModel resultViewModel;
    private AttachmentFragmentListener.View mainView;
    private Attachment attachment;

    public AttachmentFragmentPresenter(Context context, AttachmentFragmentListener.View mainView) {
        this.context = context;
        this.mainView = mainView;
        attachment = new Attachment();
    }


    @Override
    public void initResultViewModel(ResultViewModel resultViewModel) {
        this.resultViewModel = resultViewModel;
    }

    @Override
    public void onInformationStringChanged(String information) {
        attachment.information = information.length() < 30 ? "" : information;
        mainView.updateView(attachment);
    }

    @Override
    public void btnContinueClicked() {
        resultViewModel.message.remark = attachment.information;
        mainView.submitData(resultViewModel);
    }
}
