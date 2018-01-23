package com.tokopedia.mitratoppers.preapprove.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.mitratoppers.MitraToppersComponentInstance;
import com.tokopedia.mitratoppers.R;
import com.tokopedia.mitratoppers.preapprove.data.model.response.preapprove.ResponsePreApprove;
import com.tokopedia.mitratoppers.preapprove.data.source.cloud.api.MitraToppersApi;
import com.tokopedia.mitratoppers.common.di.component.MitraToppersComponent;
import com.tokopedia.mitratoppers.preapprove.view.listener.MitraToppersPreApproveView;
import com.tokopedia.mitratoppers.preapprove.view.presenter.MitraToppersPreApprovePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MitraToppersPreApproveFragment extends BaseDaggerFragment implements MitraToppersPreApproveView {

    @Inject
    public MitraToppersPreApprovePresenter mitraToppersPreApprovePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initInjector() {
        MitraToppersComponent mitraToppersComponent = MitraToppersComponentInstance.get(
                (BaseMainApplication)getActivity().getApplication());
        mitraToppersComponent.inject(this);
        mitraToppersPreApprovePresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_mitra_toppers_preapprove, container, false);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        showLoading();
        mitraToppersPreApprovePresenter.getPreApproveBalanceUseCase();
    }

    private void showLoading(){

    }

    private void hideLoading(){

    }

    @Override
    public void onSuccessGetPreApprove(ResponsePreApprove responsePreApprove) {
        hideLoading();
        //TODO show views
    }

    @Override
    public void onErrorGetPreApprove(Throwable e) {
        hideLoading();
        // TODO hide all views.
    }

}
