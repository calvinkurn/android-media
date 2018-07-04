package com.tokopedia.transaction.orders.orderdetails.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderdetails.data.ActionButton;
import com.tokopedia.transaction.orders.orderdetails.data.EntityAddress;
import com.tokopedia.transaction.orders.orderdetails.data.Items;
import com.tokopedia.transaction.orders.orderdetails.data.MetaDataInfo;
import com.tokopedia.transaction.orders.orderdetails.data.TapActions;
import com.tokopedia.transaction.orders.orderdetails.view.activity.OrderListDetailActivity;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailContract;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailPresenter;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OrderListDetailContract.TapActionInterface {

    private List<Items> itemsList;
    private Context context;
    private final int ITEM = 1;
    private final int ITEM2 = 2;
    private boolean isShortLayout;
    OrderListDetailPresenter presenter;
    ItemViewHolder viewHolder;

    public ItemsAdapter(Context context, List<Items> itemsList, boolean isShortLayout, OrderListDetailPresenter presenter) {
        this.context = context;
        this.itemsList = itemsList;
        this.isShortLayout = isShortLayout;
        this.presenter = presenter;
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
        viewHolder = (ItemViewHolder) holder;
        /*switch (getItemViewType(position)) {
            case ITEM:*/
        ((ItemViewHolder) holder).setIndex(position);
        ((ItemViewHolder) holder).bindData(itemsList.get(position), isShortLayout);
        // break;
//            case ITEM2:
//                ((ItemViewHolder) holder).setIndex(position);
//                ((ItemViewHolder) holder).bindData(itemsList.get(position), isShortLayout);
//                break;
           /* default:
                break;
        }*/

    }

    @Override
    public int getItemViewType(int position) {
        return isShortLayout ? ITEM2 : ITEM;
    }

    @Override
    public void setTapActionButton(int position, TapActions tapActions) {
        TextView tapActionTextView = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tapActionTextView.setPadding(24, 24, 24, 24);
        tapActionTextView.setLayoutParams(params);
        tapActionTextView.setTextColor(Color.WHITE);
        tapActionTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        tapActionTextView.setText(tapActions.getLabel().toUpperCase());
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(context.getResources().getColor(R.color.green_nob));
        shape.setCornerRadius(4);
        shape.setStroke(1, Color.BLACK);
        tapActionTextView.setBackground(shape);
        if (!tapActions.getBody().equals(""))
            tapActionTextView.setOnClickListener(getActionButtonClickListener(tapActions.getBody().getAppURL()));
        viewHolder.tapActionLayout.addView(tapActionTextView);
    }

    private View.OnClickListener getActionButtonClickListener(final String uri) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TkpdCoreRouter) context.getApplicationContext())
                        .actionOpenGeneralWebView((OrderListDetailActivity)context, uri);
            }
        };
    }

    @Override
    public void tapActionLayoutVisible() {
        viewHolder.tapActionLayout.setVisibility(View.VISIBLE);
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private ImageView dealImage;
        private TextView dealsDetails;
        private TextView brandName;
        private TextView cityName;
        private TextView validDate;

        //        private TextView redeemButton;
        private LinearLayout tapActionLayout;
        private LinearLayout actionLayout;
        //        private TextView voucherRedeemedDate;
        private View clCard;
        private int index;

        public ItemViewHolder(View itemView, boolean isShortLayout) {
            super(itemView);
            this.itemView = itemView;
            dealImage = itemView.findViewById(R.id.iv_deal);
            dealsDetails = itemView.findViewById(R.id.tv_deal_intro);
            brandName = itemView.findViewById(R.id.tv_brand_name);
            cityName = itemView.findViewById(R.id.tv_redeem_locations);
            if (!isShortLayout) {
                validDate = itemView.findViewById(R.id.tv_valid_till_date);
//                redeemButton = itemView.findViewById(R.id.tv_redeem_voucher);
                tapActionLayout = itemView.findViewById(R.id.tapAction);
                actionLayout = itemView.findViewById(R.id.actionButton);
//                voucherRedeemedDate = itemView.findViewById(R.id.tv_voucher_redeemed);
                clCard = itemView.findViewById(R.id.cl_card);
                itemView.findViewById(R.id.divider1).setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            }

        }

        public void bindData(final Items item, boolean isShortLayout) {

            MetaDataInfo metaDataInfo = null;

            if (item.getMetaData() != null) {
                Gson gson = new Gson();
                metaDataInfo = gson.fromJson(item.getMetaData(), MetaDataInfo.class);
            }
            if (metaDataInfo != null) {
                ImageHandler.loadImage(context, dealImage, metaDataInfo.getEntityImage(), R.color.grey_1100, R.color.grey_1100);
                dealsDetails.setText(metaDataInfo.getEntityProductName());
                brandName.setText(metaDataInfo.getEntityBrandName());
                if (!isShortLayout) {
//                validDate.setText();
//                    redeemButton.setOnClickListener(this);
                    validDate.setText(String.format(context.getResources().getString(R.string.text_valid_till), metaDataInfo.getEndDate()));
                }
                EntityAddress entityAddress = metaDataInfo.getEntityAddress();
                if (entityAddress != null) {
                    cityName.setText(entityAddress.getAddress() + ", " + entityAddress.getCity() + ", " + entityAddress.getDistrict() + ", " + entityAddress.getState());
                }
            }
            if (item.getActionButtons().size() > 0) {
                actionLayout.setVisibility(View.VISIBLE);
            }
            for (ActionButton actionButton : item.getActionButtons()) {
                TextView smsButton = new TextView(context);
                smsButton.setText(actionButton.getLabel().toUpperCase());
                smsButton.setGravity(Gravity.CENTER_HORIZONTAL);
                smsButton.setPadding(24, 24, 24, 24);
                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setCornerRadius(4);
                shape.setStroke(1, Color.BLACK);
                smsButton.setBackground(shape);
                smsButton.setOnClickListener(getActionButtonClickListener(actionButton.getBody().getAppURL()));
                actionLayout.addView(smsButton);
            }

            if (item.getTapActions().size() > 0) {
                presenter.setTapActionButton(item.getTapActions(), ItemsAdapter.this, getIndex());
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
