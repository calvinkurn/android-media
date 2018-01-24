package com.tokopedia.mitratoppers.preapprove.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.mitratoppers.MitraToppersComponentInstance;
import com.tokopedia.mitratoppers.R;
import com.tokopedia.mitratoppers.preapprove.data.model.response.preapprove.ResponsePreApprove;
import com.tokopedia.mitratoppers.common.di.component.MitraToppersComponent;
import com.tokopedia.mitratoppers.preapprove.view.activity.MitraToppersPreApproveWebViewActivity;
import com.tokopedia.mitratoppers.preapprove.view.listener.MitraToppersPreApproveView;
import com.tokopedia.mitratoppers.preapprove.view.presenter.MitraToppersPreApprovePresenter;

import javax.inject.Inject;

public class MitraToppersPreApproveLabelFragment extends BaseDaggerFragment implements MitraToppersPreApproveView {

    @Inject
    public MitraToppersPreApprovePresenter mitraToppersPreApprovePresenter;
    private View rootView;
    private LabelView labelView;

    public static MitraToppersPreApproveLabelFragment newInstance() {
        return new MitraToppersPreApproveLabelFragment();
    }

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
        View view =  LayoutInflater.from(getContext()).inflate(R.layout.fragment_mitra_toppers_preapprove, container, false);
        rootView = view.findViewById(R.id.vg_mitra_toppers_root);
        labelView = view.findViewById(R.id.label_view_mitra_toppers);
        rootView.setVisibility(View.GONE);
        return view;
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
        // no loading shown currently
    }

    private void hideLoading(){
        // no loading hidden currently
    }

    @Override
    public void onSuccessGetPreApprove(final ResponsePreApprove responsePreApprove) {
        hideLoading();
        labelView.setContent(responsePreApprove.getPreapp().getRatePerMonth3m().getAmountIdr());
        labelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MitraToppersPreApproveWebViewActivity.getIntent(getContext(),
                        responsePreApprove.getUrl());
                startActivity(intent);
            }
        });
        rootView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onErrorGetPreApprove(Throwable e) {
        hideLoading();
        rootView.setVisibility(View.GONE);
    }

}
