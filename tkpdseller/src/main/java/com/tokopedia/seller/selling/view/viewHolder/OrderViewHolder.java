package com.tokopedia.seller.selling.view.viewHolder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R2;
import com.tokopedia.seller.R;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Toped10 on 8/5/2016.
 */
public class OrderViewHolder extends BaseSellingViewHolder<OrderShippingList> {
    @BindView(R2.id.user_name)
    TextView UserName;
    @BindView(R2.id.user_avatar)
    ImageView UserAvatar;
    @BindView(R2.id.deadline)
    TextView Deadline;
    @BindView(R2.id.deadline_view)
    View DeadlineView;
    @BindView(R2.id.invoice)
    TextView Invoice;
    @BindView(R2.id.bounty)
    TextView TotalTransaksi;
    @BindView(R2.id.main_view)
    View MainView;
    @BindView(R2.id.order_date)
    TextView vOrderDate;
    @BindView(R2.id.buyer_request_cancel)
    TextView buyerRequestCancel;
    @BindView(R2.id.colored_border)
    View deadlineColoredBorder;

    public OrderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindDataModel(Context context, OrderShippingList model) {
        UserName.setText(model.getOrderCustomer().getCustomerName());
        setDeadLine(model);
        Invoice.setText(model.getOrderDetail().getDetailInvoice());
        vOrderDate.setText(model.getOrderDetail().getDetailOrderDate());
        TotalTransaksi.setText(model.getOrderPayment().getPaymentKomisi());
        ImageHandler.loadImageCircle2(context, UserAvatar, model.getOrderCustomer().getCustomerImage());
        if(model.getOrderDetail().getDetailCancelRequest() != null && model.getOrderDetail().getDetailCancelRequest().getCancelRequest() == 1){
            buyerRequestCancel.setVisibility(View.VISIBLE);
        }else{
            buyerRequestCancel.setVisibility(View.GONE);
        }
    }

    private void setDeadLine(OrderShippingList model) {
        Deadline.setText(model.getOrderDeadline().getDeadLineProcess());
        deadlineColoredBorder.setBackgroundColor(Color
                .parseColor(model.getOrderDeadline().getDeadlineColor()));
    }

    @Override
    public void setOnItemClickListener(final OnItemClickListener clickListener) {
        MainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClicked(getAdapterPosition());
            }
        });
    }
}
