package com.tokopedia.seller.selling.view.viewHolder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core2.R;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.seller.selling.presenter.ShippingImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Toped10 on 7/29/2016.
 */
public class ShippingViewHolder extends BaseSellingViewHolder<ShippingImpl.Model> {

    public TextView invoice_selected;
    public Button vRefNumber;
    public TextView vError;
    public TextView vReceiver;
    public TextView vInsertReceiver;
    public TextView vDest;
    public TextView vInsertDest;
    public TextView vShipping;
    public TextView vInsertShipping;
    public TextView vInsertSenderDS;
    public TextView vSenderDS;
    public View Dropshipper;
    public View InsertDropshipper;
    public View InfoView;
    public View InsertView;
    public TextView UserName;
    public TextView InsertUserName;
    public LinearLayout BtnOverflow;
    public TextView Deadline;
    public View DeadlineView;
    public TextView Invoice;
    public TextView vShippingPrice;
    public ImageView CancelBut;
    public ImageView CameraBut;
    public View MainView;
    View deadlineColoredBorder;
//
//    public ShippingViewHolder(View itemView) {
//        super(itemView);
//        ButterKnife.bind(this, itemView);
//    }

    public ShippingViewHolder(View itemView, MultiSelector multiSelector) {
        super(itemView, multiSelector);
        invoice_selected = (TextView) itemView.findViewById(R.id.invoice_text_selected);
        vRefNumber = (Button) itemView.findViewById(R.id.ref_number);
        vError = (TextView) itemView.findViewById(R.id.error_msg);
        vReceiver = (TextView) itemView.findViewById(R.id.receiver_name);
        vInsertReceiver = (TextView) itemView.findViewById(R.id.insert_receiver_name);
        vDest = (TextView) itemView.findViewById(R.id.dest);
        vInsertDest = (TextView) itemView.findViewById(R.id.insert_dest);
        vShipping = (TextView)itemView.findViewById(R.id.shipping);
        vInsertShipping = (TextView) itemView.findViewById(R.id.insert_shipping);
        vInsertSenderDS = (TextView) itemView.findViewById(R.id.insert_sender_detail);
        vSenderDS = (TextView) itemView.findViewById(R.id.sender_detail);
        Dropshipper = itemView.findViewById(R.id.dropshipper);
        InsertDropshipper = itemView.findViewById(R.id.insert_dropshipper);
        InfoView = itemView.findViewById(R.id.info_view);
        InsertView = itemView.findViewById(R.id.insert_view);
        UserName = (TextView) itemView.findViewById(R.id.user_name);
        InsertUserName = (TextView) itemView.findViewById(R.id.insert_user_name);
        BtnOverflow = (LinearLayout) itemView.findViewById(R.id.but_overflow);
        Deadline = (TextView) itemView.findViewById(R.id.deadline);
        DeadlineView = itemView.findViewById(R.id.deadline_view);
        Invoice = (TextView) itemView.findViewById(R.id.invoice_text);
        vShippingPrice = (TextView) itemView.findViewById(R.id.shipping_price);
        CancelBut = (ImageView) itemView.findViewById(R.id.cancel_but);
        CameraBut = (ImageView) itemView.findViewById(R.id.camera_but);
        MainView = itemView.findViewById(R.id.main_view);
        deadlineColoredBorder = itemView.findViewById(R.id.colored_border);

        if(Build.VERSION.SDK_INT >= 21) {
            setSelectionModeStateListAnimator(null);
            setDefaultModeStateListAnimator(itemView.getStateListAnimator());
        }

        setSelectionModeBackgroundDrawable(itemView.getBackground());
        setDefaultModeBackgroundDrawable(itemView.getBackground());
    }

    private void checkError(ShippingImpl.Model model, Context context) {
        if (model.ErrorRow) {
            vError.setVisibility(View.VISIBLE);
            vError.setText(model.ErrorMsg);
        } else {
            try {
                InsertView.setBackground(context.getResources().getDrawable(R.drawable.cards_ui_selected));
            } catch (NoSuchMethodError e) {
                InsertView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cards_ui_selected));
            }
            vError.setVisibility(View.GONE);
        }
    }


    private void setChecked(ShippingImpl.Model model) {
        if (model.Checked) {
            InfoView.setVisibility(View.GONE);
            InsertView.setVisibility(View.VISIBLE);
        } else {
            InfoView.setVisibility(View.VISIBLE);
            InsertView.setVisibility(View.GONE);
        }
    }

    private void setViewData(ShippingImpl.Model model, Context context) {
        vReceiver.setText(MethodChecker.fromHtml(model.ReceiverName));
        vInsertReceiver.setText(MethodChecker.fromHtml(model.ReceiverName));
        vDest.setText(model.Dest);
        vInsertDest.setText(model.Dest);
        vShipping.setText(model.Shipping);
        vInsertShipping.setText(model.Shipping);
        UserName.setText(model.UserName);
        InsertUserName.setText(model.UserName);
        vShippingPrice.setText(model.ShippingPrice);
        if (model.RefNum.equals(""))
            vRefNumber.setText(context.getString(R.string.hint_fill_ref));
        else
            vRefNumber.setText(model.RefNum);
        Invoice.setText(model.Invoice);
        invoice_selected.setText(model.Invoice);
    }

    private void setSenderDetail(ShippingImpl.Model model) {
        if (!CommonUtils.checkNullForZeroJson(model.SenderDetail)) {
            setSender(model);
        } else {
            setNoSender();
        }
    }

    private void setSender(ShippingImpl.Model model) {
        Dropshipper.setVisibility(View.VISIBLE);
        InsertDropshipper.setVisibility(View.VISIBLE);
        vSenderDS.setText(model.SenderDetail);
        vInsertSenderDS.setText(model.SenderDetail);
    }

    private void setNoSender() {
        Dropshipper.setVisibility(View.GONE);
        InsertDropshipper.setVisibility(View.GONE);
    }

    private void checkPermission(String permission) {
        if (!permission.equals("0")) {
            setHasPermission();
        } else {
            setNoPermission();
        }
    }

    private void setNoPermission() {
        BtnOverflow.setVisibility(View.INVISIBLE);
    }

    private void setHasPermission() {
        BtnOverflow.setVisibility(View.VISIBLE);
    }

    @Override
    public void bindDataModel(Context context, ShippingImpl.Model model) {
        setChecked(model);
        checkPermission(model.Permission);
        setSenderDetail(model);
        setViewData(model, context);
        checkError(model, context);
        setDeadLine(model.orderShippingList);
        //CommonUtils.getProcessDay(context, model.deadline, deadline, deadlineView);
    }

    @Override
    public void setOnItemClickListener(final OnItemClickListener clickListener) {
        MainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClicked(getAdapterPosition());
            }
        });
        MainView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clickListener.onLongClicked(getAdapterPosition());
                return true;
            }
        });
    }

    private void setDeadLine(OrderShippingList model) {
        Deadline.setText(model.getOrderDeadline().getDeadlineShipping());
        try {
            deadlineColoredBorder.setBackgroundColor(Color.parseColor(model
                    .getOrderDeadline()
                    .getDeadlineColor()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
