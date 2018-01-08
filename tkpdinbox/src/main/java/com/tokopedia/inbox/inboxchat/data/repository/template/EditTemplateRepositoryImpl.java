package com.tokopedia.inbox.inboxchat.data.repository.template;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.data.factory.template.EditTemplateChatFactory;
import com.tokopedia.inbox.inboxchat.viewmodel.EditTemplateViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class EditTemplateRepositoryImpl implements EditTemplateRepository{

    private EditTemplateChatFactory templateChatFactory;

    public EditTemplateRepositoryImpl(EditTemplateChatFactory templateChatFactory) {
        this.templateChatFactory = templateChatFactory;
    }

    @Override
    public Observable<EditTemplateViewModel> editTemplate(int index, TKPDMapParam<String, Object> parameters) {
        return templateChatFactory.createCloudEditTemplateDataSource().editTemplate(index, parameters);
    }

    @Override
    public Observable<EditTemplateViewModel> createTemplate(TKPDMapParam<String, Object> parameters) {
        return templateChatFactory.createCloudEditTemplateDataSource().createTemplate(parameters);
    }

    @Override
    public Observable<EditTemplateViewModel> deleteTemplate(int index) {
        return templateChatFactory.createCloudEditTemplateDataSource().deleteTemplate(index);
    }
}
