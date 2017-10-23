package com.tokopedia.seller.selling.view.viewHolder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    TextView userName;
    ImageView userAvatar;
    TextView deadline;
    View deadlineView;
    TextView invoice;
    View mainView;
    TextView vOrderDate;
    TextView buyerRequestCancel;
    View deadlineColoredBorder;

    public OrderViewHolder(View itemView) {
        super(itemView);
        userName = (TextView) itemView.findViewById(R.id.user_name);
        userAvatar = (ImageView) itemView.findViewById(R.id.user_avatar);
        deadline = (TextView) itemView.findViewById(R.id.deadline);
        deadlineView = itemView.findViewById(R.id.deadline_view);
        invoice = (TextView) itemView.findViewById(R.id.invoice);
        mainView = itemView.findViewById(R.id.main_view);
        vOrderDate = (TextView) itemView.findViewById(R.id.order_date);
        buyerRequestCancel = (TextView) itemView.findViewById(R.id.buyer_request_cancel);
        deadlineColoredBorder = itemView.findViewById(R.id.colored_border);
    }

    @Override
    public void bindDataModel(Context context, OrderShippingList model) {
        userName.setText(model.getOrderCustomer().getCustomerName());
        setDeadLine(model);
        invoice.setText(model.getOrderDetail().getDetailInvoice());
        vOrderDate.setText(model.getOrderDetail().getDetailOrderDate());
        ImageHandler.loadImageCircle2(context, userAvatar, model.getOrderCustomer().getCustomerImage());
        if(model.getOrderDetail().getDetailCancelRequest() != null && model.getOrderDetail().getDetailCancelRequest().getCancelRequest() == 1){
            buyerRequestCancel.setVisibility(View.VISIBLE);
        }else{
            buyerRequestCancel.setVisibility(View.GONE);
        }
    }

    private void setDeadLine(OrderShippingList model) {
        deadline.setText(model.getOrderDeadline().getDeadLineProcess());
        deadlineColoredBorder.setBackgroundColor(Color
                .parseColor(model.getOrderDeadline().getDeadlineColor()));
    }

    @Override
    public void setOnItemClickListener(final OnItemClickListener clickListener) {
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClicked(getAdapterPosition());
            }
        });
    }
}
