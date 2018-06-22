package com.tokopedia.transaction.orders.orderdetails.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderdetails.data.Items;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Items> itemsList;
    private Context context;
    private final int ITEM = 1;
    private final int ITEM2 = 2;
    private boolean isShortLayout;

    public ItemsAdapter(Context context, List<Items> itemsList, boolean isShortLayout) {
        this.context = context;
        this.itemsList = itemsList;
        this.isShortLayout = isShortLayout;
    }

    @Override
    public int getItemCount() {
        if (itemsList != null) {
            return itemsList.size();
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View v;
        switch (viewType) {
            case ITEM:
                v = inflater.inflate(R.layout.voucher_item_card, parent, false);
                holder = new ItemViewHolder(v, isShortLayout);
                break;
            case ITEM2:
                v = inflater.inflate(R.layout.voucher_item_card_short, parent, false);
                holder = new ItemViewHolder(v, isShortLayout);
                break;
            default:
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                ((ItemViewHolder) holder).setIndex(position);
                ((ItemViewHolder) holder).bindData(itemsList.get(position), isShortLayout);
                break;
            case ITEM2:
                ((ItemViewHolder) holder).setIndex(position);
                ((ItemViewHolder) holder).bindData(itemsList.get(position), isShortLayout);
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return isShortLayout ? ITEM2 : ITEM;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private ImageView dealImage;
        private TextView dealsDetails;
        private TextView brandName;
        private TextView cityName;
        private TextView validDate;
        private TextView redeemButton;
        private TextView voucherRedeemedDate;
        private int index;

        public ItemViewHolder(View itemView, boolean isShortLayout) {
            super(itemView);
            this.itemView = itemView;
            dealImage = itemView.findViewById(R.id.imageView);
            dealsDetails = itemView.findViewById(R.id.tv_deal_intro);
            brandName = itemView.findViewById(R.id.tv_brand_name);
            cityName = itemView.findViewById(R.id.tv_redeem_locations);
            if (!isShortLayout) {
                validDate = itemView.findViewById(R.id.tv_valid_till_date);
                redeemButton = itemView.findViewById(R.id.tv_redeem_voucher);
                voucherRedeemedDate=itemView.findViewById(R.id.tv_voucher_redeemed);

            }


        }

        public void bindData(final Items items, boolean isShortLayout) {

            if (!isShortLayout) {
//                validDate.setText();
                redeemButton.setOnClickListener(this);
            }
        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public void onClick(View v) {

        }
    }


}
