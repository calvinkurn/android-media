package com.tokopedia.inbox.inboxchat.data.repository.template;

import com.google.gson.JsonObject;
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
    public Observable<EditTemplateViewModel> editTemplate(JsonObject parameters) {
        return templateChatFactory.createCloudEditTemplateDataSource().editTemplate(parameters);
    }

    @Override
    public Observable<EditTemplateViewModel> createTemplate(JsonObject parameters) {
        return templateChatFactory.createCloudEditTemplateDataSource().createTemplate(parameters);
    }
}
