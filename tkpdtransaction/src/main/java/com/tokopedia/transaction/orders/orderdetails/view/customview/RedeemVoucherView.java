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
import com.tokopedia.transaction.orders.orderdetails.data.Body;
import com.tokopedia.transaction.orders.orderdetails.data.Header;
import com.tokopedia.transaction.orders.orderdetails.data.Items;
import com.tokopedia.transaction.orders.orderdetails.view.adapter.ItemsAdapter;
import com.tokopedia.transaction.orders.orderdetails.view.fragment.OmsDetailFragment;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailPresenter;
import com.tokopedia.transaction.orders.orderlist.common.HexValidator;

public class RedeemVoucherView extends LinearLayout {

    private Context context;
    private TextView voucherNumber, redeemVoucher;
    private int voucherCount;
    private ActionButton actionButton;
    private Items item;
    private SetTapActionDeals setTapActionDeals;
    private FrameLayout retryLoadingView;
    private Body body;
    private int mPos;
    OrderListDetailPresenter presenter;

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

    public RedeemVoucherView(Context context, int voucherNumber, ActionButton actionButton, Items item, Body body, OrderListDetailPresenter presenter, int position, SetTapActionDeals setTapActionDeals) {
        super(context);
        this.context = context;
        this.voucherCount = voucherNumber;
        this.actionButton = actionButton;
        this.item = item;
        this.body = body;
        this.presenter = presenter;
        this.mPos = position;
        this.setTapActionDeals = setTapActionDeals;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.redeem_voucher_deals_layout, this);
        voucherNumber = view.findViewById(R.id.voucher_code_title_deals);
        redeemVoucher = view.findViewById(R.id.redeem_btn_deals);
        retryLoadingView = view.findViewById(R.id.loading_view_retry);
        if (actionButton.getControl().equalsIgnoreCase("refresh")) {
            renderRetryButton(actionButton);
        } else {
            renderRedeemButton(actionButton);
        }
        redeemVoucher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionButton.getLabel().equalsIgnoreCase(ItemsAdapter.KEY_RETRY)) {
                    OmsDetailFragment.RETRY_COUNT++;
                    retryLoadingView.setVisibility(VISIBLE);
                    redeemVoucher.setVisibility(GONE);
                } else {
                    retryLoadingView.setVisibility(GONE);
                    redeemVoucher.setVisibility(VISIBLE);
                }
                setTapActionDeals.tapActionClicked(redeemVoucher, actionButton, item, OmsDetailFragment.RETRY_COUNT, mPos);
            }
        });
    }

    private void renderRedeemButton(ActionButton actionButton) {
        retryLoadingView.setVisibility(GONE);
        redeemVoucher.setVisibility(VISIBLE);
        redeemVoucher.setText(actionButton.getLabel());
        if (!TextUtils.isEmpty(actionButton.getHeader())) {
            Gson gson = new Gson();
            Header header = gson.fromJson(actionButton.getHeader(), Header.class);
            if (!TextUtils.isEmpty(header.getItemLabel())) {
                voucherNumber.setText(header.getItemLabel());
            }
        }else {
            if (voucherCount > 0) {
                voucherNumber.setText(String.format("%s %s", context.getResources().getString(R.string.event_ticket_voucher_number_multiple), (voucherCount + 1)));
            } else {
                voucherNumber.setText(context.getResources().getString(R.string.event_ticket_voucher_number));
            }
        }
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

    private void renderRetryButton(ActionButton actionButton) {
        redeemVoucher.setText(actionButton.getLabel());
        if (OmsDetailFragment.RETRY_COUNT == 0) {
            if (!TextUtils.isEmpty(actionButton.getHeader())) {
                Gson gson = new Gson();
                Header header = gson.fromJson(actionButton.getHeader(), Header.class);
                if (!TextUtils.isEmpty(header.getItemLabel())) {
                    voucherNumber.setText(header.getItemLabel());
                }
            }
            GradientDrawable shape = new GradientDrawable();
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
            redeemVoucher.setBackground(shape);
        } else {
            if (actionButton.getControl().equalsIgnoreCase(ItemsAdapter.KEY_REFRESH)) {
                voucherNumber.setText(context.getResources().getString(R.string.tkpdtransaction_oms_retry_text));
                redeemVoucher.setBackground(context.getResources().getDrawable(R.drawable.bg_rounded_grey_label));
                redeemVoucher.setTextColor(context.getResources().getColor(R.color.tkpd_transaction_retry_failed_button));
                redeemVoucher.setEnabled(false);
                redeemVoucher.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OmsDetailFragment.RETRY_COUNT = 0;
                        redeemVoucher.setEnabled(true);
                        renderRetryButton(actionButton);
                    }
                }, 30000);
                if (item.getCategory().equalsIgnoreCase(ItemsAdapter.categoryDeals)) {
                    presenter.showRetryButtonToaster(context.getResources().getString(R.string.tkpdtransaction_oms_retry_failed_deals));
                } else {
                    presenter.showRetryButtonToaster(context.getResources().getString(R.string.tkpdtransaction_oms_retry_failed_event));
                }
            } else {
                presenter.showRetryButtonToaster(context.getResources().getString(R.string.tkpdtransaction_oms_retry_success));
            }
        }
    }

    public interface SetTapActionDeals {
        void tapActionClicked(TextView view, ActionButton actionButton, Items item, int count, int position);
    }
}
