package com.tokopedia.tokocash.historytokocash.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.historytokocash.presentation.model.WalletToDepositThanksData;
import com.tokopedia.tokocash.historytokocash.presentation.model.WalletToDepositThanksPassData;

/**
 * Created by nabillasabbaha on 2/26/18.
 */

public class ThankYouMoveToSaldoFragment extends BaseDaggerFragment {

    public static final String ARG_EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA =
            "ARG_EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA";
    public static final String STATE_EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA =
            "STATE_EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA";

    private ImageView ivIcon;
    private TextView tvSubTitle;
    private TextView tvDesc;
    private TextView btnNegative;
    private TextView btnPositive;

    private WalletToDepositThanksPassData stateWalletToDepositThanksPassData;
    private ActionListener actionListener;

    public static ThankYouMoveToSaldoFragment newInstance(WalletToDepositThanksPassData passData) {
        ThankYouMoveToSaldoFragment fragment = new ThankYouMoveToSaldoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thank_you_move_to_saldo, container, false);
        ivIcon = view.findViewById(R.id.iv_icon);
        tvSubTitle = view.findViewById(R.id.tv_subtitle);
        tvDesc = view.findViewById(R.id.tv_description);
        btnNegative = view.findViewById(R.id.btn_negative);
        btnPositive = view.findViewById(R.id.btn_positive);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.stateWalletToDepositThanksPassData =
                getArguments().getParcelable(ARG_EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(
                STATE_EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA, stateWalletToDepositThanksPassData
        );
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            stateWalletToDepositThanksPassData = savedInstanceState.getParcelable(
                    STATE_EXTRA_WALLET_TO_DEPOSIT_THANKS_PASS_DATA
            );
        }
        if (stateWalletToDepositThanksPassData != null) renderContentData();
    }

    private void renderContentData() {
        actionListener.setTitlePage(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getTitle()
        );
        if (stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getTypeResult()
                == WalletToDepositThanksData.TypeResult.SUCCESS) {
            renderSuccessResult();
        } else {
            renderFailedResult();
        }
    }

    private void renderFailedResult() {
        tvSubTitle.setVisibility(View.VISIBLE);
        tvDesc.setVisibility(View.GONE);
        btnPositive.setVisibility(View.VISIBLE);
        btnNegative.setVisibility(View.VISIBLE);
        ivIcon.setVisibility(View.VISIBLE);

        ivIcon.setImageResource(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getIconResId()
        );

        tvSubTitle.setText(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getSubTitle()
        );
        btnPositive.setText(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getTitleButtonPositive()
        );
        btnNegative.setText(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getTitleButtonNegative()
        );

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onRetryClicked();
            }
        });

        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onBackToTokoCashClicked(false);
            }
        });
    }

    private void renderSuccessResult() {
        tvSubTitle.setVisibility(View.VISIBLE);
        tvDesc.setVisibility(View.VISIBLE);
        btnPositive.setVisibility(View.VISIBLE);
        btnNegative.setVisibility(View.GONE);
        ivIcon.setVisibility(View.VISIBLE);

        ivIcon.setImageResource(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getIconResId()
        );

        tvSubTitle.setText(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getSubTitle()
        );
        tvDesc.setText(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getDescription()
        );
        btnPositive.setText(
                stateWalletToDepositThanksPassData.getWalletToDepositThanksData().getTitleButtonPositive()
        );

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onBackToTokoCashClicked(true);
            }
        });

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

    public interface ActionListener {
        void onBackToTokoCashClicked(boolean isSucceeded);

        void onRetryClicked();

        void setTitlePage(String titlePage);
    }
}
