package com.tokopedia.transaction.orders.orderdetails.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderdetails.data.ActionButton;
import com.tokopedia.transaction.orders.orderdetails.data.EntityAddress;
import com.tokopedia.transaction.orders.orderdetails.data.Items;
import com.tokopedia.transaction.orders.orderdetails.data.MetaDataInfo;
import com.tokopedia.transaction.orders.orderdetails.data.TapActions;
import com.tokopedia.transaction.orders.orderdetails.view.activity.OrderListDetailActivity;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailContract;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailPresenter;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OrderListDetailContract.TapActionInterface {

    private List<Items> itemsList;
    private Context context;
    private final int ITEM = 1;
    private final int ITEM2 = 2;
    private boolean isShortLayout;
    OrderListDetailPresenter presenter;

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
        ((ItemViewHolder) holder).setIndex(position);
        ((ItemViewHolder) holder).bindData(itemsList.get(position), isShortLayout);
    }

    @Override
    public int getItemViewType(int position) {
        return isShortLayout ? ITEM2 : ITEM;
    }

    @Override
    public void setTapActionButton(int position, List<TapActions> tapActions) {
        itemsList.get(position).setTapActions(tapActions);
        itemsList.get(position).setTapActionsLoaded(true);
        notifyItemChanged(position);
    }

    @Override
    public void setActionButton(int position, List<ActionButton> actionButtons) {
        itemsList.get(position).setActionButtons(actionButtons);
        itemsList.get(position).setActionButtonLoaded(true);
        notifyItemChanged(position);
    }

    private View.OnClickListener getActionButtonClickListener(final String uri) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TkpdCoreRouter) context.getApplicationContext())
                        .actionOpenGeneralWebView((OrderListDetailActivity) context, uri);
            }
        };
    }

    private View.OnClickListener getTapActionButtonClickListener(final String uri) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TkpdCoreRouter) context.getApplicationContext())
                        .actionOpenGeneralWebView((OrderListDetailActivity) context, uri);
            }
        };
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private ImageView dealImage;
        private TextView dealsDetails;
        private TextView brandName;
        private TextView cityName;
        private TextView validDate;
        private ProgressBar progressBar;
        private LinearLayout tapActionLayout;
        private LinearLayout actionLayout;
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
                tapActionLayout = itemView.findViewById(R.id.tapAction);
                actionLayout = itemView.findViewById(R.id.actionButton);
                clCard = itemView.findViewById(R.id.cl_card);
                itemView.findViewById(R.id.divider1).setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            }
            progressBar = itemView.findViewById(R.id.prog_bar);

        }

        public void bindData(final Items item, boolean isShortLayout) {

            MetaDataInfo metaDataInfo = null;

            if (item.getMetaData() != null) {
                Gson gson = new Gson();
                metaDataInfo = gson.fromJson(item.getMetaData(), MetaDataInfo.class);
            }
            if (metaDataInfo != null) {
                if (metaDataInfo.getEntityImage() == null || metaDataInfo.getEntityImage().length() == 0) {
                    ImageHandler.loadImage(context, dealImage, item.getImageUrl(), R.color.grey_1100, R.color.grey_1100);
                } else {
                    ImageHandler.loadImage(context, dealImage, metaDataInfo.getEntityImage(), R.color.grey_1100, R.color.grey_1100);
                }
                if (metaDataInfo.getEntityProductName() == null || metaDataInfo.getEntityProductName().length() == 0) {
                    dealsDetails.setText(item.getTitle());
                } else {
                    dealsDetails.setText(metaDataInfo.getEntityProductName());
                }
                brandName.setText(metaDataInfo.getEntityBrandName());
                if (!isShortLayout) {
                    validDate.setText(String.format(context.getResources().getString(R.string.text_valid_till), metaDataInfo.getEndDate()));
                }
                EntityAddress entityAddress = metaDataInfo.getEntityAddress();
                if (entityAddress != null) {
                    if (entityAddress.getName() != null) {
                        cityName.setText(entityAddress.getName());
                    }
                }
            }

            if (item.getTapActions() != null && item.getTapActions().size() > 0 && !item.isTapActionsLoaded()) {
                progressBar.setVisibility(View.VISIBLE);
                tapActionLayout.setVisibility(View.GONE);
                presenter.setTapActionButton(item.getTapActions(), ItemsAdapter.this, getIndex());
            }

            if (item.isTapActionsLoaded()) {
                progressBar.setVisibility(View.GONE);
                if (item.getTapActions() == null || item.getTapActions().size() == 0) {
                    tapActionLayout.setVisibility(View.GONE);
                } else {
                    tapActionLayout.setVisibility(View.VISIBLE);
                }

                tapActionLayout.removeAllViews();
                for (int i = 0; i < item.getTapActions().size(); i++) {
                    TapActions tapActions = item.getTapActions().get(i);

                    TextView tapActionTextView = new TextView(context);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, (int) context.getResources().getDimension(R.dimen.dp_8), 0, 0);
                    tapActionTextView.setPadding(24, 24, 24, 24);
                    tapActionTextView.setLayoutParams(params);
                    tapActionTextView.setTextColor(Color.WHITE);
                    tapActionTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                    tapActionTextView.setText(tapActions.getLabel().toUpperCase());
                    GradientDrawable shape = new GradientDrawable();
                    shape.setShape(GradientDrawable.RECTANGLE);
                    if (!tapActions.getTapActionColor().getBackground().equals("")) {
                        shape.setColor(android.graphics.Color.parseColor(tapActions.getTapActionColor().getBackground()));
                    } else {
                        shape.setColor(context.getResources().getColor(R.color.green_nob));
                    }
                    if (!tapActions.getTapActionColor().getBorder().equals("")) {
                        shape.setStroke(1, android.graphics.Color.parseColor(tapActions.getTapActionColor().getBorder()));
                    }
                    tapActionTextView.setBackground(shape);
                    if (!tapActions.getTapActionColor().getTextColor().equals("")) {
                        tapActionTextView.setTextColor(android.graphics.Color.parseColor(tapActions.getTapActionColor().getTextColor()));
                    } else {
                        tapActionTextView.setTextColor(Color.WHITE);
                    }


                    if (i == item.getTapActions().size() - 1 && (item.getActionButtons() != null || item.getActionButtons().size() == 0)) {
                        float radius = context.getResources().getDimension(R.dimen.dp_4);
                        shape.setCornerRadii(new float[]{0, 0, 0, 0, radius, radius, radius, radius});

                    } else {

                        shape.setCornerRadius(4);
                    }

                    tapActionTextView.setBackground(shape);
                    if (tapActions.getControl().equalsIgnoreCase("button")) {
                        presenter.setTapActionButton(item.getTapActions(), ItemsAdapter.this, getIndex());
                    } else if (tapActions.getControl().equalsIgnoreCase("redirect")) {
                        if (!tapActions.getBody().equals("")) {
                            if (!tapActions.getBody().getAppURL().equals(""))
                                tapActionTextView.setOnClickListener(getActionButtonClickListener(tapActions.getBody().getAppURL()));
                        }
                    }

                    tapActionLayout.addView(tapActionTextView);
                }


            }


//            if (item.getActionButtons() != null && item.getActionButtons().size() > 0 && !item.isActionButtonLoaded()) {
//                progressBar.setVisibility(View.VISIBLE);
//                actionLayout.setVisibility(View.GONE);
//                presenter.setActionButton(item.getActionButtons(), ItemsAdapter.this, getIndex());
//            }


            if (item.getActionButtons() == null || item.getActionButtons().size() == 0) {
                actionLayout.setVisibility(View.GONE);
            } else {
                actionLayout.setVisibility(View.VISIBLE);
            }
            actionLayout.removeAllViews();

            for (int i = 0; i < item.getActionButtons().size(); i++) {
                ActionButton actionButton = item.getActionButtons().get(i);

                TextView actionTextView = new TextView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, (int) context.getResources().getDimension(R.dimen.dp_8), 0, 0);
                actionTextView.setPadding(24, 24, 24, 24);
                actionTextView.setLayoutParams(params);
                actionTextView.setTextColor(Color.WHITE);
                actionTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                actionTextView.setText(actionButton.getLabel().toUpperCase());
                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                if (!actionButton.getActionColor().getBackground().equals("")) {
                    shape.setColor(android.graphics.Color.parseColor(actionButton.getActionColor().getBackground()));
                } else {
                    shape.setColor(context.getResources().getColor(R.color.green_nob));
                }
                if (!actionButton.getActionColor().getBorder().equals("")) {
                    shape.setStroke(1, android.graphics.Color.parseColor(actionButton.getActionColor().getBorder()));
                }
                actionTextView.setBackground(shape);
                if (!actionButton.getActionColor().getTextColor().equals("")) {
                    actionTextView.setTextColor(android.graphics.Color.parseColor(actionButton.getActionColor().getTextColor()));
                } else {
                    actionTextView.setTextColor(Color.WHITE);
                }


                if (i == item.getTapActions().size() - 1 && (item.getActionButtons() != null || item.getActionButtons().size() == 0)) {
                    float radius = context.getResources().getDimension(R.dimen.dp_4);
                    shape.setCornerRadii(new float[]{0, 0, 0, 0, radius, radius, radius, radius});

                } else {

                    shape.setCornerRadius(4);
                }

                actionTextView.setBackground(shape);
                if (!actionButton.getControl().equalsIgnoreCase("text")) {
                    if (item.isActionButtonLoaded()) {
                        if (actionButton.getControl().equalsIgnoreCase("redirect")) {
                            if (!actionButton.getBody().equals("")) {
                                if (!actionButton.getBody().getAppURL().equals(""))
                                    RouteManager.route(context, actionButton.getBody().getAppURL());
                            }
                        }
                    } else {
                        actionTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (actionButton.getControl().equalsIgnoreCase("button")) {
                                    presenter.setActionButton(item.getActionButtons(), ItemsAdapter.this, getIndex());
                                } else if (actionButton.getControl().equalsIgnoreCase("redirect")) {
                                    if (!actionButton.getBody().equals("")) {
                                        if (!actionButton.getBody().getAppURL().equals(""))
                                            actionTextView.setOnClickListener(getActionButtonClickListener(actionButton.getBody().getAppURL()));
                                    }
                                }

                            }
                        });
                    }
                }

                actionLayout.addView(actionTextView);
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
