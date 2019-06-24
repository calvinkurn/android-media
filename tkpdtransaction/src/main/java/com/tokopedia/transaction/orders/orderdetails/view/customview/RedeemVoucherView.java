package com.tokopedia.transaction.orders.orderdetails.view.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.UnifiedOrderListRouter;
import com.tokopedia.transaction.orders.orderdetails.data.ActionButton;
import com.tokopedia.transaction.orders.orderdetails.data.Items;
import com.tokopedia.transaction.orders.orderdetails.view.adapter.ItemsAdapter;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailPresenter;
import com.tokopedia.transaction.orders.orderlist.common.HexValidator;

public class RedeemVoucherView extends LinearLayout {

    private Context context;
    private TextView voucherNumber, redeemVoucher;
    private int voucherCount;
    private ActionButton actionButton;
    private Items item;
    private SetTapActionDeals setTapActionDeals;

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
        View view = LayoutInflater.from(context).inflate(R.layout.redeem_voucher_deals_layout, this);
        voucherNumber = view.findViewById(R.id.voucher_code_title_deals);
        redeemVoucher = view.findViewById(R.id.redeem_btn_deals);
        voucherNumber.setText(String.format("%s %s", "E-Voucher #" , (voucherCount+ 1)));
        renderRedeemButton(actionButton);
        redeemVoucher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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
        }else{
            shape.setStroke(0, context.getResources().getColor(R.color.green_nob));
        }
        if (HexValidator.validate(actionButton.getActionColor().getTextColor())) {
            redeemVoucher.setTextColor(android.graphics.Color.parseColor(actionButton.getActionColor().getTextColor()));
        } else {
            redeemVoucher.setTextColor(Color.WHITE);
        }

//
//        if (position == item.getTapActions().size() - 1 && (item.getActionButtons() != null || item.getActionButtons().size() == 0)) {
//            float radius = context.getResources().getDimension(R.dimen.dp_4);
//            shape.setCornerRadii(new float[]{0, 0, 0, 0, radius, radius, radius, radius});
//
//        } else {

        shape.setCornerRadius(context.getResources().getDimension(R.dimen.dp_4));
//        }


    }

    public interface SetTapActionDeals {
        void tapActionClicked(TextView view, ActionButton actionButton, Items item);
    }
}
