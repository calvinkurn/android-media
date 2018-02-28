package com.tokopedia.tokocash.historytokocash.presentation.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.di.TokoCashComponent;
import com.tokopedia.tokocash.historytokocash.presentation.compoundview.BalanceTokoCashView;
import com.tokopedia.tokocash.historytokocash.presentation.compoundview.ReceivedTokoCashView;
import com.tokopedia.tokocash.historytokocash.presentation.contract.HomeTokoCashContract;
import com.tokopedia.tokocash.historytokocash.presentation.presenter.HomeTokoCashPresenter;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 8/18/17.
 */
public class HomeTokoCashFragment extends BaseDaggerFragment implements HomeTokoCashContract.View {

    public static final String EXTRA_TOP_UP_AVAILABLE = "EXTRA_TOP_UP_AVAILABLE";

    private RelativeLayout mainContent;
    private ProgressBar progressLoading;

    private BalanceTokoCashView balanceTokoCashView;
    private ReceivedTokoCashView receivedTokoCashView;
    private BottomSheetView bottomSheetTokoCashView;

    private boolean topUpAvailable;
    private ActionListener listener;

    @Inject
    HomeTokoCashPresenter presenter;

    public static HomeTokoCashFragment newInstance(boolean isTopUpAvailable) {
        HomeTokoCashFragment fragment = new HomeTokoCashFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_TOP_UP_AVAILABLE, isTopUpAvailable);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initInjector();
        presenter.attachView(this);
        View view = inflater.inflate(R.layout.fragment_home_tokocash, container, false);
        balanceTokoCashView = view.findViewById(R.id.balance_tokocash_layout);
        receivedTokoCashView = view.findViewById(R.id.received_tokocash_layout);
        mainContent = view.findViewById(R.id.main_content);
        progressLoading = view.findViewById(R.id.pb_main_loading);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.topUpAvailable = getArguments().getBoolean(EXTRA_TOP_UP_AVAILABLE, true);
        bottomSheetTokoCashView = new BottomSheetView(getActivity());
        presenter.processGetBalanceTokoCash();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showProgressLoading() {
        progressLoading.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressLoading() {
        if (progressLoading.getVisibility() == View.VISIBLE) {
            progressLoading.setVisibility(View.GONE);
            mainContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage() {

    }

    @Override
    public void renderBalanceTokoCash(BalanceTokoCash balanceTokoCash) {
        listener.setTitle(balanceTokoCash.getTitleText());
        balanceTokoCashView.setListener(getBalanceListener());
        balanceTokoCashView.renderDataBalance(balanceTokoCash);
        receivedTokoCashView.renderReceivedView(balanceTokoCash);
        bottomSheetTokoCashView.renderBottomSheet(new BottomSheetView
                .BottomSheetField.BottomSheetFieldBuilder()
                .setTitle(getString(R.string.title_tooltip_tokocash))
                .setBody(getString(R.string.body_tooltip_tokocash))
                .setImg(R.drawable.activate_ic_activated)
                .build());
    }

    private BalanceTokoCashView.ActionListener getBalanceListener() {
        return new BalanceTokoCashView.ActionListener() {
            @Override
            public void showTooltipHoldBalance() {
                bottomSheetTokoCashView.show();
            }
        };
    }

    @Override
    public void showEmptyPage(Throwable throwable) {
        String message = ErrorHandler.getErrorMessage(getActivity(), throwable);
        NetworkErrorHelper.showEmptyState(getActivity(), mainContent, message, getRetryListener());
    }

    private NetworkErrorHelper.RetryClickedListener getRetryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.processGetBalanceTokoCash();
            }
        };
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    private void clearHolder(LinearLayout holderView) {
        if (holderView.getChildCount() > 0) {
            holderView.removeAllViews();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ActionListener) context;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(TokoCashComponent.class).inject(this);
    }

    public interface ActionListener {
        void setTitle(String title);
    }
}