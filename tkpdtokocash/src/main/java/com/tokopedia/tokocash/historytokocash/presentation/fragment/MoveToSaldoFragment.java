package com.tokopedia.tokocash.historytokocash.presentation.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.di.TokoCashComponent;
import com.tokopedia.tokocash.historytokocash.presentation.activity.ThankYouMoveToSaldoActivity;
import com.tokopedia.tokocash.historytokocash.presentation.contract.MoveToSaldoContract;
import com.tokopedia.tokocash.historytokocash.presentation.model.WalletToDepositData;
import com.tokopedia.tokocash.historytokocash.presentation.model.WalletToDepositPassData;
import com.tokopedia.tokocash.historytokocash.presentation.model.WalletToDepositThanksData;
import com.tokopedia.tokocash.historytokocash.presentation.model.WalletToDepositThanksPassData;
import com.tokopedia.tokocash.historytokocash.presentation.presenter.MoveToSaldoPresenter;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 2/19/18.
 */

public class MoveToSaldoFragment extends BaseDaggerFragment implements MoveToSaldoContract.View {
    public static final String ARG_EXTRA_WALLET_TO_DEPOSIT_PASS_DATA =
            "ARG_EXTRA_WALLET_TO_DEPOSIT_PASS_DATA";
    public static final String STATE_EXTRA_WALLET_TO_DEPOSIT_PASS_DATA =
            "STATE_EXTRA_WALLET_TO_DEPOSIT_PASS_DATA";
    public static final String STATE_EXTRA_WALLET_TO_DEPOSIT_DATA =
            "STATE_EXTRA_WALLET_TO_DEPOSIT_DATA";

    private RelativeLayout mainContainer;
    private ProgressBar mainProgressBar;
    private TextView tvSubtitle;
    private TextView tvNominal;
    private TextView btnCancel;
    private TextView btnProcess;

    private ActionListener actionListener;
    private WalletToDepositData stateWalletToDepositData;
    private WalletToDepositPassData statePassData;
    private TkpdProgressDialog progressDialogNormal;

    @Inject
    MoveToSaldoPresenter presenter;

    public static MoveToSaldoFragment newInstance(WalletToDepositPassData passData) {
        MoveToSaldoFragment fragment = new MoveToSaldoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_EXTRA_WALLET_TO_DEPOSIT_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initInjector();
        presenter.attachView(this);
        View view = inflater.inflate(R.layout.fragment_move_to_saldo, container, false);
        mainContainer = view.findViewById(R.id.main_container);
        mainProgressBar = view.findViewById(R.id.pb_main_loading);
        tvSubtitle = view.findViewById(R.id.tv_subtitle);
        tvNominal = view.findViewById(R.id.tv_nominal);
        btnCancel = view.findViewById(R.id.btn_negative);
        btnProcess = view.findViewById(R.id.btn_positive);

        progressDialogNormal = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.statePassData = getArguments().getParcelable(ARG_EXTRA_WALLET_TO_DEPOSIT_PASS_DATA);
        showInitProgressLoading();
        renderInitializePage(
                new WalletToDepositData.Builder()
                        .subTitle(getString(R.string.tokocash_title_confirmation_move_saldo))
                        .title(getTitlePageFromPassData())
                        .nominalFormatted(getWalletAmountFormattedFromPassData())
                        .titleButtonNegative(getString(R.string.button_label_cancel))
                        .titleButtonPositive(getString(R.string.tokocash_button_move_saldo))
                        .build());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_EXTRA_WALLET_TO_DEPOSIT_DATA, stateWalletToDepositData);
        outState.putParcelable(STATE_EXTRA_WALLET_TO_DEPOSIT_PASS_DATA, statePassData);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            WalletToDepositPassData walletToDepositPassData = savedInstanceState.getParcelable(
                    STATE_EXTRA_WALLET_TO_DEPOSIT_PASS_DATA
            );
            if (walletToDepositPassData != null) this.statePassData = walletToDepositPassData;
            WalletToDepositData walletToDepositData = savedInstanceState.getParcelable(
                    STATE_EXTRA_WALLET_TO_DEPOSIT_DATA
            );
            if (walletToDepositData != null) this.stateWalletToDepositData = walletToDepositData;

            if (stateWalletToDepositData != null) renderInitializePage(stateWalletToDepositData);
            else actionListener.onWalletToDepositFailed();
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        if (context instanceof ActionListener) {
            actionListener = (ActionListener) context;
        } else {
            throw new RuntimeException("Activity isn't instance of ActionListener");
        }
    }

    private void showInitProgressLoading() {
        mainContainer.setVisibility(View.GONE);
        mainProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideInitProgressLoading() {
        mainContainer.setVisibility(View.VISIBLE);
        mainProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressLoading() {
        progressDialogNormal.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        if (progressDialogNormal.isProgress())
            progressDialogNormal.dismiss();
    }

    private void renderInitializePage(final WalletToDepositData walletToDepositData) {
        hideInitProgressLoading();
        this.stateWalletToDepositData = walletToDepositData;
        tvSubtitle.setText(stateWalletToDepositData.getSubTitle());
        tvNominal.setText(stateWalletToDepositData.getNominalFormatted());
        btnCancel.setText(stateWalletToDepositData.getTitleButtonNegative());
        btnProcess.setText(stateWalletToDepositData.getTitleButtonPositive());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processMoveToSaldo(statePassData.getUrl(), statePassData.getParams());
            }
        });
        actionListener.setTitlePage(stateWalletToDepositData.getTitle());
    }

    public void renderThankYouPage(WalletToDepositThanksData data) {
        startActivityForResult(ThankYouMoveToSaldoActivity.newInstance(getActivity(),
                new WalletToDepositThanksPassData.Builder()
                        .walletToDepositThanksData(data)
                        .build()),
                ThankYouMoveToSaldoActivity.REQUEST_CODE);
    }

    @Override
    public void wrappingDataSuccess(long amount) {
        String amountFormatted = CurrencyFormatHelper.ConvertToRupiah(String.valueOf(amount));
        WalletToDepositThanksData successWalletDepositThanksPage =
                new WalletToDepositThanksData.Builder()
                        .title(getString(R.string.tokocash_success_title_move_saldo))
                        .subTitle(getString(R.string.tokocash_success_subtitle_move_saldo))
                        .description(String.format(getString(R.string.tokocash_success_amount),
                                amountFormatted.replace(",", ".")))
                        .titleButtonPositive(getString(R.string.tokocash_move_page))
                        .typeResult(WalletToDepositThanksData.TypeResult.SUCCESS)
                        .iconResId(R.drawable.move_to_saldo_success)
                        .build();
        renderThankYouPage(successWalletDepositThanksPage);
    }

    @Override
    public void wrappingDataFailed() {
        WalletToDepositThanksData failedWalletDepositThanksPage =
                new WalletToDepositThanksData.Builder()
                        .title(getString(R.string.tokocash_failed_title_move_saldo))
                        .subTitle(getString(R.string.tokocash_failed_subtitle_move_saldo))
                        .titleButtonNegative(getString(R.string.tokocash_move_page))
                        .titleButtonPositive(getString(R.string.tokocash_try_again))
                        .typeResult(WalletToDepositThanksData.TypeResult.FAILED)
                        .iconResId(R.drawable.move_to_saldo_failed)
                        .build();
        renderThankYouPage(failedWalletDepositThanksPage);
    }

    private String getTitlePageFromPassData() {
        return statePassData.getTitle();
    }

    private String getWalletAmountFormattedFromPassData() {
        return statePassData.getAmountFormatted();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroyView();
    }

    @Override
    protected void initInjector() {
        getComponent(TokoCashComponent.class).inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ThankYouMoveToSaldoActivity.REQUEST_CODE) {
            switch (resultCode) {
                case ThankYouMoveToSaldoActivity.RESULT_BACK_RETRY:
                    presenter.processMoveToSaldo(statePassData.getUrl(), statePassData.getParams());
                    break;
                case ThankYouMoveToSaldoActivity.RESULT_BACK_FAILED:
                    actionListener.onWalletToDepositFailed();
                    break;
                case ThankYouMoveToSaldoActivity.RESULT_BACK_SUCCESS:
                    actionListener.onWalletToDepositSucceeded();
                    break;
            }
        }
    }

    public interface ActionListener {

        void setTitlePage(String titlePage);

        void onWalletToDepositSucceeded();

        void onWalletToDepositFailed();
    }
}
