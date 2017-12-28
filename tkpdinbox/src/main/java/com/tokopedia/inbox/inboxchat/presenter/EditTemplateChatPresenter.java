package com.tokopedia.inbox.inboxchat.presenter;

import com.google.gson.JsonArray;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.inboxchat.domain.usecase.template.CreateTemplateUseCase;
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

    @Inject
    EditTemplateChatPresenter(EditTemplateUseCase editTemplateUseCase, CreateTemplateUseCase createTemplateUseCase){
        this.editTemplateUseCase = editTemplateUseCase;
        this.createTemplateUseCase = createTemplateUseCase;
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
    }

    public void submitText(final String s, String text, List<String> list) {
        final int index = list.indexOf(text);
        List<String> temp = new ArrayList<>();
        temp.addAll(list);
        if(index<0){
            temp.add(s);
        }else {
            temp.set(index, s);
        }
        JsonArray array = toJsonArray(temp);
        editTemplateUseCase.execute(EditTemplateUseCase.generateParam(array, true), new Subscriber<EditTemplateViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(EditTemplateViewModel editTemplateViewModel) {
                if(editTemplateViewModel == null){
                    getView().onResult(editTemplateViewModel, index, s, false);
                }else {
                    getView().onResult(editTemplateViewModel, index, s, true);
                }
                getView().finish();
            }
        });
    }

    public JsonArray toJsonArray(List<String> yaml) {
        JsonArray array = new JsonArray();
        for (String o : yaml) {
            array.add(o);
        }
        return array;
    }

    @Override
    public void deleteTemplate() {

    }
}
