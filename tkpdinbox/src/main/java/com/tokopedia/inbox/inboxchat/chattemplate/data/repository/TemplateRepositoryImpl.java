package com.tokopedia.inbox.inboxchat.chattemplate.data.repository;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.chattemplate.data.factory.TemplateChatFactory;
import com.tokopedia.inbox.inboxchat.chattemplate.view.viewmodel.GetTemplateViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class TemplateRepositoryImpl implements TemplateRepository{

    private TemplateChatFactory templateChatFactory;

    public TemplateRepositoryImpl(TemplateChatFactory templateChatFactory) {
        this.templateChatFactory = templateChatFactory;
    }

    @Override
    public Observable<GetTemplateViewModel> getTemplate(TKPDMapParam<String, Object> parameters) {
        return templateChatFactory.createCloudGetTemplateDataSource().getTemplate(parameters);
    }

    @Override
    public Observable<GetTemplateViewModel> setAvailabilityTemplate(JsonObject parameters) {
        return templateChatFactory.createCloudSetTemplateDataSource().setTemplate(parameters);
    }
}
