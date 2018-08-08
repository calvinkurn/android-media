package com.tokopedia.seller.selling.view.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;

import com.tokopedia.seller.R;
import com.tokopedia.seller.selling.model.SellingStatusTxModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zulfikarrahman on 11/10/16.
 */

public class TransactionViewHolder extends BaseSellingViewHolder<SellingStatusTxModel> {
    ImageView icon;
    TextView subtitle;
    TextView title;
    public LinearLayout overflow_btn;
    LinearLayout deadLineContainer;
    TextView status;
    TextView deadlineDate;
    TextView invoice;
    LinearLayout itemLayout;

    public TransactionViewHolder(View itemView) {
        super(itemView);
        icon = (ImageView) itemView.findViewById(R.id.icon);
        subtitle = (TextView) itemView.findViewById(R.id.subtitle);
        title = (TextView) itemView.findViewById(R.id.title);
        overflow_btn = (LinearLayout) itemView.findViewById(R.id.overflow_btn);
        deadLineContainer = (LinearLayout) itemView.findViewById(R.id.deadline_view);
        status = (TextView) itemView.findViewById(R.id.status);
        deadlineDate = (TextView) itemView.findViewById(R.id.deadline_date);
        invoice = (TextView) itemView.findViewById(R.id.invoice);
        itemLayout = (LinearLayout) itemView.findViewById(R.id.list_item);
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
