package com.tokopedia.inbox.inboxchat.presenter;

import com.google.gson.JsonArray;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.inboxchat.domain.usecase.template.CreateTemplateUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.template.DeleteTemplateUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.template.EditTemplateUseCase;
import com.tokopedia.inbox.inboxchat.viewmodel.EditTemplateViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by stevenfredian on 12/22/17.
 */

public class EditTemplateChatPresenter extends BaseDaggerPresenter<EditTemplateChatContract.View>
                    implements EditTemplateChatContract.Presenter{

    private final EditTemplateUseCase editTemplateUseCase;
    private final CreateTemplateUseCase createTemplateUseCase;
    private final DeleteTemplateUseCase deleteTemplateUseCase;

    @Inject
    EditTemplateChatPresenter(EditTemplateUseCase editTemplateUseCase, CreateTemplateUseCase createTemplateUseCase, DeleteTemplateUseCase deleteTemplateUseCase){
        this.editTemplateUseCase = editTemplateUseCase;
        this.createTemplateUseCase = createTemplateUseCase;
        this.deleteTemplateUseCase = deleteTemplateUseCase;
    }

    @Override
    public void attachView(EditTemplateChatContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        getView().dropKeyboard();
        super.detachView();
        editTemplateUseCase.unsubscribe();
        createTemplateUseCase.unsubscribe();
        deleteTemplateUseCase.unsubscribe();
    }

    public void submitText(final String s, String text, List<String> list) {
        final int index = list.indexOf(text);
        List<String> temp = new ArrayList<>();
        temp.addAll(list);
        if(index<0){
            temp.add(s);
            createTemplateUseCase.execute(CreateTemplateUseCase.generateParam(s), new Subscriber<EditTemplateViewModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    getView().showError("create");
                }

                @Override
                public void onNext(EditTemplateViewModel editTemplateViewModel) {
                    if(editTemplateViewModel.isSuccess()) {
                        getView().onResult(editTemplateViewModel, index, s);
                        getView().finish();
                    }else {
                        getView().showError("");
                    }
                }
            });
        }else {
            temp.set(index, s);
            editTemplateUseCase.execute(EditTemplateUseCase.generateParam(index+1,s), new Subscriber<EditTemplateViewModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    getView().showError("edit");
                }

                @Override
                public void onNext(EditTemplateViewModel editTemplateViewModel) {
                    if(editTemplateViewModel.isSuccess()) {
                        getView().onResult(editTemplateViewModel, index, s);
                        getView().finish();
                    }else {
                        getView().showError("");
                    }
                }
            });
        }
    }

    public JsonArray toJsonArray(List<String> yaml) {
        JsonArray array = new JsonArray();
        for (String o : yaml) {
            array.add(o);
        }
        return array;
    }

    @Override
    public void deleteTemplate(final int index) {
        deleteTemplateUseCase.execute(DeleteTemplateUseCase.generateParam(index+1), new Subscriber<EditTemplateViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showError("delete");
            }

            @Override
            public void onNext(EditTemplateViewModel editTemplateViewModel) {
                if(editTemplateViewModel.isSuccess()) {
                    getView().onResult(editTemplateViewModel, index);
                    getView().finish();
                }else {
                    getView().showError("delete");
                }
            }
        });
    }
}
