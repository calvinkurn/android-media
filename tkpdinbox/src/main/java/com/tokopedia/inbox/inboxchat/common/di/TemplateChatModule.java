package com.tokopedia.inbox.inboxchat.common.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.inbox.inboxchat.chattemplate.data.factory.EditTemplateChatFactory;
import com.tokopedia.inbox.inboxchat.chattemplate.data.factory.TemplateChatFactory;
import com.tokopedia.inbox.inboxchat.chattemplate.data.mapper.EditTemplateChatMapper;
import com.tokopedia.inbox.inboxchat.chattemplate.data.mapper.TemplateChatMapper;
import com.tokopedia.inbox.inboxchat.chattemplate.data.repository.EditTemplateRepository;
import com.tokopedia.inbox.inboxchat.chattemplate.data.repository.EditTemplateRepositoryImpl;
import com.tokopedia.inbox.inboxchat.chattemplate.data.repository.TemplateRepository;
import com.tokopedia.inbox.inboxchat.chattemplate.data.repository.TemplateRepositoryImpl;
import com.tokopedia.inbox.inboxchat.chattemplate.domain.usecase.CreateTemplateUseCase;
import com.tokopedia.inbox.inboxchat.chattemplate.domain.usecase.DeleteTemplateUseCase;
import com.tokopedia.inbox.inboxchat.chattemplate.domain.usecase.EditTemplateUseCase;
import com.tokopedia.inbox.inboxchat.chattemplate.domain.usecase.GetTemplateUseCase;
import com.tokopedia.inbox.inboxchat.chattemplate.domain.usecase.SetAvailabilityTemplateUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by stevenfredian on 9/14/17.
 */

@Module
public class TemplateChatModule {

    @TemplateChatScope
    @Provides
    TemplateChatFactory provideTemplateChatFactory(
            ChatService chatService,
            TemplateChatMapper templateChatMapper){
        return new TemplateChatFactory(templateChatMapper, chatService);
    }

    @TemplateChatScope
    @Provides
    EditTemplateChatFactory provideEditTemplateChatFactory(
            ChatService chatService,
            EditTemplateChatMapper templateChatMapper){
        return new EditTemplateChatFactory(templateChatMapper, chatService);
    }

    @TemplateChatScope
    @Provides
    GetTemplateUseCase provideGetTemplateUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutor,
                                                 TemplateRepository templateRepository) {
        return new GetTemplateUseCase(threadExecutor, postExecutor, templateRepository);
    }

    @TemplateChatScope
    @Provides
    SetAvailabilityTemplateUseCase provideSetTemplateUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutor,
                                                             TemplateRepository templateRepository) {
        return new SetAvailabilityTemplateUseCase(threadExecutor, postExecutor, templateRepository);
    }

    @TemplateChatScope
    @Provides
    EditTemplateUseCase provideEditTemplateUseCase(ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutor,
                                                   EditTemplateRepository templateRepository) {
        return new EditTemplateUseCase(threadExecutor, postExecutor, templateRepository);
    }

    @TemplateChatScope
    @Provides
    CreateTemplateUseCase provideCreateTemplateUseCase(ThreadExecutor threadExecutor,
                                                     PostExecutionThread postExecutor,
                                                     EditTemplateRepository templateRepository) {
        return new CreateTemplateUseCase(threadExecutor, postExecutor, templateRepository);
    }

    @TemplateChatScope
    @Provides
    DeleteTemplateUseCase provideDeleteTemplateUseCase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutor,
                                                       EditTemplateRepository templateRepository) {
        return new DeleteTemplateUseCase(threadExecutor, postExecutor, templateRepository);
    }

    @TemplateChatScope
    @Provides
    TemplateRepository provideTemplateRepository(TemplateChatFactory templateChatFactory) {
        return new TemplateRepositoryImpl(templateChatFactory);
    }

    @TemplateChatScope
    @Provides
    EditTemplateRepository provideEditTemplateRepository(EditTemplateChatFactory templateChatFactory) {
        return new EditTemplateRepositoryImpl(templateChatFactory);
    }

    @TemplateChatScope
    @Provides
    TemplateChatMapper provideTemplateChatMapper(){
        return new TemplateChatMapper();
    }

    @TemplateChatScope
    @Provides
    EditTemplateChatMapper provideEditTemplateChatMapper(){
        return new EditTemplateChatMapper();
    }



    @TemplateChatScope
    @Provides
    ChatService provideChatService(){
        return new ChatService();
    }


}
