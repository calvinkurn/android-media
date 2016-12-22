package com.tokopedia.seller.selling.view.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R2;
import com.tokopedia.seller.R;
import com.tokopedia.seller.selling.model.SellingStatusTxModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zulfikarrahman on 11/10/16.
 */

public class StatusViewHolder extends BaseSellingViewHolder<SellingStatusTxModel> {

    @BindView(R2.id.icon)
    ImageView icon;
    @BindView(R2.id.subtitle)
    TextView subtitle;
    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.overflow_btn)
    public LinearLayout overflow_btn;
    @BindView(R2.id.deadline_view)
    LinearLayout deadLineContainer;
    @BindView(R2.id.status)
    TextView status;
    @BindView(R2.id.deadline_date)
    TextView deadlineDate;
    @BindView(R2.id.invoice)
    TextView invoice;
    @BindView(R2.id.list_item)
    LinearLayout itemLayout;

    public StatusViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setOnItemClickListener(final OnItemClickListener clickListener) {
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClicked(getAdapterPosition());
            }
        });
    }

    public void bindDataModel(Context context, SellingStatusTxModel model) {
        ImageHandler.loadImageCircle2(context, icon, model.AvatarUrl);
        title.setText(model.UserName);
        subtitle.setText(model.LastStatus);
        invoice.setText(model.Invoice);
        if (!CommonUtils.checkNullForZeroJson(model.DeadlineFinish)) {
            deadLineContainer.setVisibility(View.GONE);
        } else if (status.getText().toString().trim().equals("Transaksi selesai")) {
            deadLineContainer.setVisibility(View.GONE);
        } else {
            deadLineContainer.setVisibility(View.VISIBLE);
            deadlineDate.setText(model.DeadlineFinish);
        }
        setOverflow(model);
    }

    private void setOverflow(SellingStatusTxModel model) {
        if (!model.Permission.equals("0") && (model.OrderStatus.equals("500") || model.OrderStatus.equals("501") || model.OrderStatus.equals("520") || model.OrderStatus.equals("530"))) {
            overflow_btn.setVisibility(View.VISIBLE);
        } else {
            overflow_btn.setVisibility(View.GONE);
        }
    }
}
