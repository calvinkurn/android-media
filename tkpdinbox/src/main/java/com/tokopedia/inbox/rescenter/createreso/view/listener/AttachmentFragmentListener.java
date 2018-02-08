package com.tokopedia.inbox.rescenter.createreso.view.listener;

import android.content.Intent;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.Attachment;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.AttachmentViewModel;

import java.util.List;

/**
 * Created by yoasfs on 30/08/17.
 */

public interface AttachmentFragmentListener {

    interface View extends CustomerView {

        void populateDataToView(ResultViewModel resultViewModel);

        void updateView(Attachment attachment);

        void submitData(ResultViewModel resultViewModel);

        void showSnackBarError(String message);

        void addAttachmentFile(AttachmentViewModel attachmentViewModel);

        List<AttachmentViewModel> getAttachmentListFromAdapter();
    }

    interface Presenter extends CustomerPresenter<AttachmentFragmentListener.View> {

        void initResultViewModel(ResultViewModel resultViewModel);

        void onInformationStringChanged(String information);

        void btnContinueClicked();

        void handleDefaultOldUploadImageHandlerResult(int resultCode, Intent data);

        void handleNewGalleryResult(int resultCode, Intent data);

        void onAdapterChanged(List<AttachmentViewModel> attachmentViewModelList);
    }
}
