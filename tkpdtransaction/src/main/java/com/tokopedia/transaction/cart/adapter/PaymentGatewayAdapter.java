package com.tokopedia.transaction.cart.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.cart.fragment.PaymentGatewayFragment;
import com.tokopedia.transaction.cart.model.cartdata.GatewayList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 11/4/16.
 */

public class PaymentGatewayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final PaymentGatewayFragment fragment;
    private final ActionListener actionListener;
    private List<GatewayList> gatewayLists = new ArrayList<>();

    public interface ActionListener {
        void onSelectedPaymentGateway(GatewayList gateway);
    }

    public PaymentGatewayAdapter(PaymentGatewayFragment fragment, List<GatewayList> gatewayLists) {
        this.fragment = fragment;
        this.gatewayLists = gatewayLists;
        this.actionListener = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(fragment.getActivity()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final GatewayList item = gatewayLists.get(position);
        viewHolder.tvName.setText(item != null ? item.getGatewayName() : "");
        ImageHandler.LoadImage(viewHolder.ivLogo, item != null ? item.getGatewayImage() : "");
        viewHolder.tvFee.setText(item != null ? item.getGatewayDesc() : "");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onSelectedPaymentGateway(item);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.holder_item_payment_gateway_tx_module;
    }

    @Override
    public int getItemCount() {
        return gatewayLists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.img)
        ImageView ivLogo;
        @BindView(R2.id.name)
        TextView tvName;
        @BindView(R2.id.payment_fee)
        TextView tvFee;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
