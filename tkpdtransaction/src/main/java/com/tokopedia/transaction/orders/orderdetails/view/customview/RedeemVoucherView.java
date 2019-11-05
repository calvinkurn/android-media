package com.tokopedia.transaction.orders.orderdetails.view.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderdetails.data.ActionButton;
import com.tokopedia.transaction.orders.orderdetails.data.Header;
import com.tokopedia.transaction.orders.orderdetails.data.Items;
import com.tokopedia.transaction.orders.orderlist.common.HexValidator;
import com.tokopedia.unifycomponents.Toaster;

public class RedeemVoucherView extends LinearLayout {

    private Context context;
    private TextView voucherNumber, redeemVoucher;
    private int voucherCount;
    private ActionButton actionButton;
    private Items item;
    private SetTapActionDeals setTapActionDeals;
    private FrameLayout retryLoadingView;
    private int count = 0;
    View view;

    public RedeemVoucherView(Context context) {
        super(context);
        initView();
    }

    public RedeemVoucherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RedeemVoucherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public RedeemVoucherView(Context context, int voucherNumber, ActionButton actionButton, Items item, SetTapActionDeals setTapActionDeals) {
        super(context);
        this.context = context;
        this.voucherCount = voucherNumber;
        this.actionButton = actionButton;
        this.item = item;
        this.setTapActionDeals = setTapActionDeals;
        initView();
    }

    private void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.redeem_voucher_deals_layout, this);
        voucherNumber = view.findViewById(R.id.voucher_code_title_deals);
        redeemVoucher = view.findViewById(R.id.redeem_btn_deals);
        retryLoadingView = view.findViewById(R.id.loading_view_retry);
        if (item.getTapActions() != null && item.getTapActions().size() > 0) {
            if (!TextUtils.isEmpty(actionButton.getHeader())) {
                Gson gson = new Gson();
                Header header = gson.fromJson(actionButton.getHeader(), Header.class);
                if (!TextUtils.isEmpty(header.getItemLabel())) {
                    voucherNumber.setText(header.getItemLabel());
                }
                renderRetryButton(actionButton, count);
            } else {
                if (voucherCount > 0) {
                    voucherNumber.setText(String.format("%s %s", context.getResources().getString(R.string.event_ticket_voucher_number_multiple), (voucherCount + 1)));
                } else {
                    voucherNumber.setText(context.getResources().getString(R.string.event_ticket_voucher_number));
                }
                renderRedeemButton(actionButton);
            }
        }
        redeemVoucher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionButton.getLabel().equalsIgnoreCase("Cek Ulang")) {
                    retryLoadingView.setVisibility(VISIBLE);
                    redeemVoucher.setVisibility(GONE);
                } else {
                    retryLoadingView.setVisibility(GONE);
                    redeemVoucher.setVisibility(VISIBLE);
                }
                setTapActionDeals.tapActionClicked(redeemVoucher, actionButton, item);
            }
        });
    }

    private void renderRedeemButton(ActionButton actionButton) {
        redeemVoucher.setText(actionButton.getLabel());

        GradientDrawable shape = (GradientDrawable) redeemVoucher.getBackground();
        if (HexValidator.validate(actionButton.getActionColor().getBackground())) {
            shape.setColor(android.graphics.Color.parseColor(actionButton.getActionColor().getBackground()));
        } else {
            shape.setColor(context.getResources().getColor(R.color.green_nob));
        }
        if (HexValidator.validate(actionButton.getActionColor().getBorder())) {
            shape.setStroke(1, android.graphics.Color.parseColor(actionButton.getActionColor().getBorder()));
        } else {
            shape.setStroke(0, context.getResources().getColor(R.color.green_nob));
        }
        if (HexValidator.validate(actionButton.getActionColor().getTextColor())) {
            redeemVoucher.setTextColor(android.graphics.Color.parseColor(actionButton.getActionColor().getTextColor()));
        } else {
            redeemVoucher.setTextColor(Color.WHITE);
        }

        shape.setCornerRadius(context.getResources().getDimension(R.dimen.dp_4));
    }

    private void renderRetryButton(ActionButton actionButton, int count) {
        redeemVoucher.setText(actionButton.getLabel());

        if (count == 0) {
            GradientDrawable shape = (GradientDrawable) redeemVoucher.getBackground();
            if (HexValidator.validate(actionButton.getActionColor().getBackground())) {
                shape.setColor(android.graphics.Color.parseColor(actionButton.getActionColor().getBackground()));
            } else {
                shape.setColor(context.getResources().getColor(R.color.green_nob));
            }
            if (HexValidator.validate(actionButton.getActionColor().getBorder())) {
                shape.setStroke(1, android.graphics.Color.parseColor(actionButton.getActionColor().getBorder()));
            } else {
                shape.setStroke(0, context.getResources().getColor(R.color.green_nob));
            }
            if (HexValidator.validate(actionButton.getActionColor().getTextColor())) {
                redeemVoucher.setTextColor(android.graphics.Color.parseColor(actionButton.getActionColor().getTextColor()));
            } else {
                redeemVoucher.setTextColor(context.getResources().getColor(R.color.green_nob));
            }
            shape.setCornerRadius(context.getResources().getDimension(R.dimen.dp_4));
        } else {
            if (actionButton.getControl().equalsIgnoreCase("refresh")) {
                voucherNumber.setText("Tunggu 30 detik");
                redeemVoucher.setBackground(context.getResources().getDrawable(R.drawable.bg_rounded_grey_label));
                redeemVoucher.setTextColor(context.getResources().getColor(R.color.tkpd_transaction_retry_failed_button));
                redeemVoucher.setEnabled(false);
                redeemVoucher.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        redeemVoucher.setEnabled(true);
                    }
                }, 30000);

                Toaster.INSTANCE.showNormalWithAction(view, "E-voucher belum terbit. Tunggu 30 detik untuk cek ulang, ya.", Toaster.LENGTH_LONG, "Oke", v1 -> {
                });
            } else {
                Toaster.INSTANCE.showNormalWithAction(view, "Hore! E-voucher kamu sudah terbit.", Toaster.LENGTH_LONG, "Oke", v1 -> {
                });
            }
        }
    }

    public interface SetTapActionDeals {
        void tapActionClicked(TextView view, ActionButton actionButton, Items item);
    }
}
