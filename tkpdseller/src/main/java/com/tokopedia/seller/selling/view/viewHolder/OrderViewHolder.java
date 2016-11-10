package com.tokopedia.core.selling.view.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R2;
import com.tokopedia.core.selling.model.orderShipping.OrderShippingList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Toped10 on 8/5/2016.
 */
public class OrderViewHolder extends BaseSellingViewHolder<OrderShippingList> {
    @Bind(R2.id.user_name)
    TextView UserName;
    @Bind(R2.id.user_avatar)
    ImageView UserAvatar;
    @Bind(R2.id.deadline)
    TextView Deadline;
    @Bind(R2.id.deadline_view)
    View DeadlineView;
    @Bind(R2.id.invoice)
    TextView Invoice;
    @Bind(R2.id.bounty)
    TextView TotalTransaksi;
    @Bind(R2.id.main_view)
    View MainView;
    @Bind(R2.id.order_date)
    TextView vOrderDate;

    public OrderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindDataModel(Context context, OrderShippingList model) {
        UserName.setText(model.getOrderCustomer().getCustomerName());
        CommonUtils.getProcessDay(context, model.getOrderPayment().getPaymentProcessDayLeft()+"", Deadline, DeadlineView);
        Invoice.setText(model.getOrderDetail().getDetailInvoice());
        vOrderDate.setText(model.getOrderDetail().getDetailOrderDate());
        TotalTransaksi.setText(model.getOrderPayment().getPaymentKomisi());
        ImageHandler.loadImageCircle2(context, UserAvatar, model.getOrderCustomer().getCustomerImage());
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
