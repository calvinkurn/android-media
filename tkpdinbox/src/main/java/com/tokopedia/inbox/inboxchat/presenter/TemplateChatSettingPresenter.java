package com.tokopedia.inbox.inboxchat.presenter;

import android.util.Log;

import com.google.gson.JsonArray;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.domain.usecase.template.GetTemplateUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.template.SetAvailabilityTemplateUseCase;
import com.tokopedia.inbox.inboxchat.viewmodel.GetTemplateViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.TemplateChatModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by stevenfredian on 12/11/17.
 */

public class TemplateChatSettingPresenter extends BaseDaggerPresenter<TemplateChatContract.View>
        implements TemplateChatContract.Presenter {

    private final SetAvailabilityTemplateUseCase setAvailabilityTemplateUseCase;
    private GetTemplateUseCase getTemplateUseCase;
    private SessionHandler sessionHandler;

    @Inject
    TemplateChatSettingPresenter(GetTemplateUseCase getTemplateUseCase,
                                 SetAvailabilityTemplateUseCase setAvailabilityTemplateUseCase,
                                 SessionHandler sessionHandler) {
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
        setAvailabilityTemplateUseCase.unsubscribe();
    }

    public void getTemplate() {
        getView().showLoading();
        getTemplateUseCase.execute(GetTemplateUseCase.generateParam(), new Subscriber<GetTemplateViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().setTemplate(null);
                getView().finishLoading();
            }

            @Override
            public void onNext(GetTemplateViewModel getTemplateViewModel) {
                List<Visitable> temp = getTemplateViewModel.getListTemplate();
                int size;
                if (temp == null) {
                    size = 0;
                    temp = new ArrayList<>();
                }else {
                    size = temp.size();
                }
                temp.add(new TemplateChatModel(false,size));
                getView().setTemplate(temp);
                getView().setChecked(getTemplateViewModel.isEnabled());
                getView().finishLoading();
            }
        });
    }

    @Override
    public void switchTemplateAvailability(final boolean enabled) {
        JsonArray array = null;
        getView().showLoading();
        setAvailabilityTemplateUseCase.execute(SetAvailabilityTemplateUseCase.generateParam(array, enabled), new Subscriber<GetTemplateViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showError(ErrorHandler.getErrorMessage(e));
                getView().setChecked(!enabled);
                getView().finishLoading();
            }

            @Override
            public void onNext(GetTemplateViewModel getTemplateViewModel) {
                if(getTemplateViewModel.isSuccess()) {
                    getView().successSwitch();
                }
                getView().finishLoading();
            }
        });
    }

    @Override
    public void setArrange(boolean enabled, final ArrayList arrayList, final int from, final int to) {
        JsonArray array = null;
        if (arrayList != null) array = toJsonArray(arrayList);
        getView().showLoading();
        setAvailabilityTemplateUseCase.execute(SetAvailabilityTemplateUseCase.generateParam(array, enabled), new Subscriber<GetTemplateViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showError(ErrorHandler.getErrorMessage(e));
                getView().revertArrange(from, to);
                getView().finishLoading();
            }

            @Override
            public void onNext(GetTemplateViewModel getTemplateViewModel) {
                getView().successRearrange();
                getView().finishLoading();
            }
        });
    }

    private JsonArray toJsonArray(List<Integer> list) {
        JsonArray array = new JsonArray();
        for (Integer o : list) {
            array.add(o);
        }
        return array;
    }
}
