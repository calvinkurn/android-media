package com.tokopedia.seller.selling.view.viewHolder;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.seller.selling.presenter.ShippingImpl;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Toped10 on 7/29/2016.
 */
public class ShippingViewHolder extends BaseSellingViewHolder<ShippingImpl.Model> {

    @Bind(R2.id.invoice_text_selected)
    public TextView invoice_selected;
    @Bind(R2.id.ref_number)
    public Button vRefNumber;
    @Bind(R2.id.error_msg)
    public TextView vError;
    @Bind(R2.id.receiver_name)
    public TextView vReceiver;
    @Bind(R2.id.insert_receiver_name)
    public TextView vInsertReceiver;
    @Bind(R2.id.dest)
    public TextView vDest;
    @Bind(R2.id.insert_dest)
    public TextView vInsertDest;
    @Bind(R2.id.shipping)
    public TextView vShipping;
    @Bind(R2.id.insert_shipping)
    public TextView vInsertShipping;
    @Bind(R2.id.insert_sender_detail)
    public TextView vInsertSenderDS;
    @Bind(R2.id.sender_detail)
    public TextView vSenderDS;
    @Bind(R2.id.dropshipper)
    public View Dropshipper;
    @Bind(R2.id.insert_dropshipper)
    public View InsertDropshipper;
    @Bind(R2.id.info_view)
    public View InfoView;
    @Bind(R2.id.insert_view)
    public View InsertView;
    @Bind(R2.id.user_name)
    public TextView UserName;
    @Bind(R2.id.insert_user_name)
    public TextView InsertUserName;
    @Bind(R2.id.but_overflow)
    public LinearLayout BtnOverflow;
    @Bind(R2.id.deadline)
    public TextView Deadline;
    @Bind(R2.id.deadline_view)
    public View DeadlineView;
    @Bind(R2.id.invoice_text)
    public TextView Invoice;
    @Bind(R2.id.shipping_price)
    public TextView vShippingPrice;
    @Bind(R2.id.cancel_but)
    public ImageView CancelBut;
    @Bind(R2.id.camera_but)
    public ImageView CameraBut;
    @Bind(R2.id.main_view)
    public View MainView;
//
//    public ShippingViewHolder(View itemView) {
//        super(itemView);
//        ButterKnife.bind(this, itemView);
//    }

    public ShippingViewHolder(View itemView, MultiSelector multiSelector) {
        super(itemView, multiSelector);
        ButterKnife.bind(this, itemView);

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
        vReceiver.setText(Html.fromHtml(model.ReceiverName));
        vInsertReceiver.setText(Html.fromHtml(model.ReceiverName));
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
        CommonUtils.getProcessDay(context, model.Deadline, Deadline, DeadlineView);
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

}
