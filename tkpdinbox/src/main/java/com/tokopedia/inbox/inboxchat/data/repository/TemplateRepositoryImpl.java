package com.tokopedia.inbox.inboxchat.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.data.factory.TemplateChatFactory;
import com.tokopedia.inbox.inboxchat.viewmodel.GetTemplateViewModel;

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
}
