package com.tokopedia.inbox.inboxchat.presenter;

import android.content.Context;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.inboxchat.viewmodel.EditTemplateViewModel;

/**
 * Created by stevenfredian on 12/22/17.
 */

public class EditTemplateChatContract {

    public interface View extends CustomerView {
        void onResult(EditTemplateViewModel editTemplateViewModel, int index, String s);

        void finish();

        void dropKeyboard();

        void showError(String error);

        void onResult(EditTemplateViewModel editTemplateViewModel, int index);
    }

    interface Presenter extends CustomerPresenter<View> {

        void deleteTemplate(int index);
    }
}
