package com.tokopedia.tokocash.historytokocash.presentation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.historytokocash.presentation.model.ActionHistory;
import com.tokopedia.tokocash.historytokocash.presentation.model.ItemHistory;

/**
 * Created by nabillasabbaha on 2/12/18.
 */

public class DetailTransactionFragment extends BaseDaggerFragment {

    public static final String ITEM_HISTORY_KEY = "item_history";
    private static final int REQUEST_MOVE_TO_SALDO = 110;

    private ImageView iconItem;
    private TextView priceItem;
    private TextView titleItem;
    private TextView descItem;
    private TextView transactionInfoDetail;
    private Button bantuanBtn;
    private LinearLayout buttonOpsiContainer;
    private TextView notesItem;

    private ItemHistory itemHistory;

    public static DetailTransactionFragment newInstance(ItemHistory itemHistory) {
        DetailTransactionFragment fragment = new DetailTransactionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEM_HISTORY_KEY, itemHistory);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_transaction, container, false);
        iconItem = view.findViewById(R.id.icon_item_history);
        priceItem = view.findViewById(R.id.price_item_history);
        titleItem = view.findViewById(R.id.title_item_history);
        descItem = view.findViewById(R.id.desc_item_history);
        transactionInfoDetail = view.findViewById(R.id.transaction_info_detail);
        bantuanBtn = view.findViewById(R.id.bantuan_btn);
        buttonOpsiContainer = view.findViewById(R.id.button_opsi);
        notesItem = view.findViewById(R.id.notes_item_history);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemHistory = getArguments().getParcelable(ITEM_HISTORY_KEY);

        if (itemHistory.getActionHistoryList() != null) {
            for (ActionHistory actionHistory : itemHistory.getActionHistoryList()) {
                if (actionHistory.getType().equals("button")) {
                    View viewItem = LayoutInflater.from(getContext())
                            .inflate(R.layout.item_button_opsi, buttonOpsiContainer, false);
                    Button buttonOpsi = viewItem.findViewById(R.id.opsi_btn);
                    buttonOpsi.setText(actionHistory.getTitle());
                    buttonOpsi.setOnClickListener(getOpsiListener(actionHistory));
                    buttonOpsiContainer.addView(view);
                }
            }
        }
        bantuanBtn.setOnClickListener(getHelpListener());
        setActionVar();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    private void setActionVar() {
        titleItem.setText(itemHistory.getTitle());
        descItem.setText(itemHistory.getDescription());
        transactionInfoDetail.setText(itemHistory.getTransactionInfoId() + " " +
                itemHistory.getTransactionInfoDate());
        priceItem.setText(itemHistory.getAmountChanges());
        priceItem.setTextColor(ContextCompat.getColor(getActivity(),
                itemHistory.getAmountChangesSymbol().equals("+") ? R.color.green_500 : R.color.red_500));

        ImageHandler.LoadImage(iconItem, itemHistory.getUrlImage());
        if (!TextUtils.isEmpty(itemHistory.getNotes())) {
            notesItem.setText(itemHistory.getNotes());
            notesItem.setVisibility(View.VISIBLE);
        } else {
            notesItem.setVisibility(View.GONE);
        }
        if (itemHistory.getActionHistoryList() != null && itemHistory.getActionHistoryList().size() > 0) {
            buttonOpsiContainer.setVisibility(View.VISIBLE);
            bantuanBtn.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.history_white_border_grey));
            bantuanBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_38));
        } else {
            buttonOpsiContainer.setVisibility(View.GONE);
            bantuanBtn.setBackground(ContextCompat.getDrawable(getActivity(), R.color.medium_green));
            bantuanBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        }
    }

    @NonNull
    private View.OnClickListener getOpsiListener(final ActionHistory actionHistory) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (actionHistory.getType().equals("button") && actionHistory.getName().equals("movetosaldo")) {
//                    WalletToDepositPassData walletToDepositPassData =
//                            new WalletToDepositPassData.Builder()
//                                    .amountFormatted(itemHistory.getAmountPending())
//                                    .method(actionHistory.getMethod())
//                                    .params(actionHistory.getParams())
//                                    .name(actionHistory.getName())
//                                    .url(actionHistory.getUrl())
//                                    .title(actionHistory.getTitle())
//                                    .build();
//                    startActivityForResult(WalletToDepositActivity.newInstance(getApplicationContext(), walletToDepositPassData), REQUEST_MOVE_TO_SALDO);
//                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_MOVE_TO_SALDO) {
//            if (resultCode == WalletToDepositActivity.RESULT_WALLET_TO_DEPOSIT_SUCCESS ||
//                    resultCode == WalletToDepositActivity.RESULT_WALLET_TO_DEPOSIT_FAILED) {
//                finish();
//            }
//        }
    }

    @NonNull
    private View.OnClickListener getHelpListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), HelpHistoryDetailActivity.class);
//                intent.putExtra(HelpHistoryDetailActivity.TRANSACTION_ID, String.valueOf(itemHistory.getTransactionId()));
//                startActivityForResult(intent, 201);
            }
        };
    }

    @Override
    public void onDestroy() {
        if (buttonOpsiContainer.getChildCount() > 0) {
            buttonOpsiContainer.removeAllViews();
        }
        super.onDestroy();
    }
}
