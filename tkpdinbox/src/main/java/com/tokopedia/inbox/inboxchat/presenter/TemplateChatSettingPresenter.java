package com.tokopedia.inbox.inboxchat.presenter;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.inboxchat.domain.usecase.template.GetTemplateUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.template.SetAvailabilityTemplateUseCase;
import com.tokopedia.inbox.inboxchat.viewmodel.GetTemplateViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.TemplateChatModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by stevenfredian on 12/11/17.
 */

public class TemplateChatSettingPresenter extends BaseDaggerPresenter<TemplateChatContract.View>
                    implements TemplateChatContract.Presenter{

    private final SetAvailabilityTemplateUseCase setAvailabilityTemplateUseCase;
    private GetTemplateUseCase getTemplateUseCase;
    private SessionHandler sessionHandler;

    @Inject
    TemplateChatSettingPresenter(GetTemplateUseCase getTemplateUseCase,
                                 SetAvailabilityTemplateUseCase setAvailabilityTemplateUseCase,
                                 SessionHandler sessionHandler){
        this.getTemplateUseCase = getTemplateUseCase;
        this.setAvailabilityTemplateUseCase = setAvailabilityTemplateUseCase;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void attachView(TemplateChatContract.View view) {
        super.attachView(view);
        getTemplate();
    }

    @Override
    public void detachView() {
        super.detachView();
        getTemplateUseCase.unsubscribe();
    }

    public void getTemplate(){
        getTemplateUseCase.execute(GetTemplateUseCase.generateParam(), new Subscriber<GetTemplateViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().setTemplate(null);
            }

            @Override
            public void onNext(GetTemplateViewModel getTemplateViewModel) {
                List<Visitable> temp = getTemplateViewModel.getListTemplate();
                getView().setChecked(temp != null);
                getView().setTemplate(temp);
            }
        });
    }

    @Override
    public void setArrange(boolean enabled) {

    }

    @Override
    public void setArrange(int from) {

    }
}
