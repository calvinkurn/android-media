package com.tokopedia.inbox.inboxchat.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.inbox.inboxchat.data.factory.template.EditTemplateChatFactory;
import com.tokopedia.inbox.inboxchat.data.factory.template.TemplateChatFactory;
import com.tokopedia.inbox.inboxchat.data.mapper.template.EditTemplateChatMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.template.TemplateChatMapper;
import com.tokopedia.inbox.inboxchat.data.repository.template.EditTemplateRepository;
import com.tokopedia.inbox.inboxchat.data.repository.template.EditTemplateRepositoryImpl;
import com.tokopedia.inbox.inboxchat.data.repository.template.TemplateRepository;
import com.tokopedia.inbox.inboxchat.data.repository.template.TemplateRepositoryImpl;
import com.tokopedia.inbox.inboxchat.domain.usecase.template.CreateTemplateUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.template.DeleteTemplateUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.template.EditTemplateUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.template.GetTemplateUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.template.SetAvailabilityTemplateUseCase;

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
