package com.tokopedia.transaction.orders.orderlist.view.adapter;

import android.content.Context;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.orders.orderdetails.view.activity.OrderListDetailActivity;
import com.tokopedia.transaction.orders.orderlist.data.Color;
import com.tokopedia.transaction.orders.orderlist.data.DotMenuList;
import com.tokopedia.transaction.orders.orderlist.data.MetaData;
import com.tokopedia.transaction.orders.orderlist.data.Order;
import com.tokopedia.transaction.orders.orderlist.data.Popup;
import com.tokopedia.transaction.orders.common.view.DoubleTextView;
import com.tokopedia.transaction.orders.orderlist.view.presenter.ListAdapterContract;
import com.tokopedia.transaction.orders.orderlist.view.presenter.ListAdapterPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ListAdapterContract.View {
    private static final String HAS_BUTTON = "1";
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private final LayoutInflater inflater;
    private final Context context;
    private ListAdapterContract.Presenter orderListPresenter;
    OrderListViewHolder currentHolder;
    ArrayList<Order> mOrderList;

    OnMenuItemListener menuListener;
    private boolean loading = false;


    public OrderListAdapter(Context context, OnMenuItemListener listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        menuListener = listener;
        orderListPresenter = new ListAdapterPresenterImpl();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list, parent, false);
            vh = new OrderListViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.footer_list_view, parent, false);

            vh = new ProgressViewHolder(v);
        }
        orderListPresenter.attachView(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OrderListViewHolder) {
            currentHolder = (OrderListViewHolder) holder;
            if (mOrderList != null) {
                currentHolder.bindData(mOrderList.get(position), position);
            }
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    private void registerViewClickListener(OrderListViewHolder holder, final Order order) {
        holder.orderListBtnOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view, order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrderList != null ? mOrderList.size() : 0;
    }

    public void addAll(List<Order> orderDataList) {
        mOrderList = new ArrayList<>(orderDataList);
    }

    @Override
    public void setButtonData(int leftVisibility, int rightVisibility, String leftText, String rightText,
                              final String leftButtonUri, final String rightButtonUri,
                              Popup leftPopup, Popup rightPopup,
                              Color leftButtonColor, Color rightButtonColor) {
        setButtonData(currentHolder.leftButton, leftText, leftVisibility, leftButtonUri, leftPopup, leftButtonColor);
        setButtonData(currentHolder.rightButton, rightText, rightVisibility, rightButtonUri, rightPopup, rightButtonColor);
    }

    private void setButtonData(TextView button, String text, int visibility, final String buttonUri, Popup popup, Color bgColor) {
        button.setVisibility(visibility);
        button.setText(text);

        if(bgColor!= null && !bgColor.background().equals("")){
            button.setBackgroundColor(android.graphics.Color.parseColor(bgColor.background()));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUri = buttonUri;
                if (buttonUri.startsWith("tokopedia")) {
                    Uri url = Uri.parse(newUri);
                    newUri = newUri.replace(url.getQueryParameter("idem_potency_key"), "");
                    newUri = newUri.replace("idem_potency_key=", "");
                    RouteManager.route(context, newUri);
                } else {
                    TransactionPurchaseRouter.startWebViewActivity(context, buttonUri);
                }
            }
        });
        if (popup != null) {
        }
    }

    @Override
    public void setDotMenuVisibility(int visibility) {
        currentHolder.orderListBtnOverflow.setVisibility(visibility);
    }

    @Override
    public void setCategoryAndTitle(String categoryName, String categoryTitle) {
        currentHolder.categoryName.setText(categoryName);
        currentHolder.title.setText(categoryTitle);
    }

    @Override
    public void setTotal(String totalLabel, String totalValue, String textColor) {
        currentHolder.totalLabel.setText(totalLabel);
        currentHolder.total.setText(totalValue);
        if (textColor.length() > 0) {
            currentHolder.total.setTextColor(android.graphics.Color.parseColor(textColor));
        }
    }

    @Override
    public void setDate(String date) {
        currentHolder.date.setText(date);
    }

    @Override
    public void setInvoice(String invoice) {
        currentHolder.invoice.setText(invoice);
    }

    @Override
    public void setConditionalInfo(int condInfoVisiblity, String conditionalInfoText, Color conditionalInfoBg) {
        currentHolder.conditionalInfoLayout.setVisibility(condInfoVisiblity);
        if (conditionalInfoText != null) {
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(9);
            shape.setColor(android.graphics.Color.parseColor(conditionalInfoBg.background()));
            shape.setStroke(1, android.graphics.Color.parseColor(conditionalInfoBg.border()));
            currentHolder.conditionalInfoText.setBackground(shape);
            currentHolder.conditionalInfoText.setPadding(16, 16, 16, 16);
            currentHolder.conditionalInfoText.setText(conditionalInfoText);
        }


    }

    @Override
    public void setFailStatusBgColor(boolean statusFail) {
            if(statusFail){
                currentHolder.status.setBackgroundColor(context.getResources().getColor(R.color.colorPink));
            } else {
                currentHolder.status.setBackgroundColor(context.getResources().getColor(R.color.green_template));
            }
    }

    @Override
    public void setStatus(String statusText) {
        currentHolder.status.setText(statusText);
    }

    @Override
    public void setMetaDataToCustomView(MetaData metaData) {
        DoubleTextView childLayout = new DoubleTextView(context, LinearLayout.VERTICAL);
        childLayout.setTopText(metaData.label());
        childLayout.setTopTextSize(11);
        String value  = metaData.value();
        TextView tv=new TextView(context);
        if(value.contains("a/n")){
            String[] values= value.split("a/n");
            tv.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setTextSize(10);
            tv.setTypeface(Typeface.DEFAULT_BOLD);
            tv.setText("a/n "+values[1]);
            childLayout.setBottomText(values[0]);
            currentHolder.parentMetadataLayout.addView(childLayout);
            currentHolder.parentMetadataLayout.addView(tv);
        } else {
            childLayout.setBottomText(value);
            currentHolder.parentMetadataLayout.addView(childLayout);
        }

    }

    @Override
    public void setPaymentAvatar(String imageUrl) {
        ImageHandler.loadImageThumbs(context, currentHolder.paymentAvatar, imageUrl);

    }

    public void clearItems() {
        mOrderList.clear();
        notifyDataSetChanged();
    }

    public boolean getLoading() {
        return loading;
    }

    public void addItemAtLast(Order order) {
        mOrderList.add(order);
        notifyItemInserted(mOrderList.size());
    }

    public void removeItemAtLast(int size) {
        mOrderList.remove(size);
        notifyItemRemoved(size);
    }

    @Override
    public int getItemViewType(int position) {
        if (mOrderList == null || mOrderList.size() == 0) return 1;
        return mOrderList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    private void showPopup(View v, final Order order) {
        PopupMenu popup = new PopupMenu(context, v);
        addCancelReplacementMenu(order.dotMenuList(), popup);
        popup.setOnMenuItemClickListener(new OnMenuPopupClicked(order.dotMenuList(), order.id()));
        popup.show();
    }

    private void addCancelReplacementMenu(List<DotMenuList> item, PopupMenu popup) {
        if (true) {
            popup.getMenu().add(Menu.NONE, R.id.action_cancel_replacement, Menu.NONE, item.get(0).name());
            popup.getMenu().add(Menu.NONE, R.id.action_next_replacement, Menu.NONE, item.get(1).name());
        }
    }

    private int getMenuId(List<DotMenuList> item) {
        int MenuID = R.menu.order_status_menu_list;
        return MenuID;
    }

    private class OnMenuPopupClicked implements PopupMenu.OnMenuItemClickListener {
        private final List<DotMenuList> orderData;
        private String orderId;

        OnMenuPopupClicked(List<DotMenuList> item, String id) {
            this.orderData = item;
            this.orderId = id;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.action_cancel_replacement) {
                menuListener.startUri(orderData.get(0).uri());
                return true;
            } else if (item.getItemId() == R.id.action_next_replacement) {
                if(!orderData.get(1).uri().equals("")){
                    menuListener.startUri(orderData.get(1).uri());
                } else{
                    context.startActivity(OrderListDetailActivity.createInstance(context, orderId));
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    class OrderListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R2.id.list_element_status)
        TextView status;
        @BindView(R2.id.date)
        TextView date;
        @BindView(R2.id.invoice)
        TextView invoice;
        @BindView(R2.id.order_list_but_overflow)
        LinearLayout orderListBtnOverflow;
        @BindView(R2.id.conditional_info_layout)
        LinearLayout conditionalInfoLayout;
        @BindView(R2.id.conditional_info)
        TextView conditionalInfoText;
        @BindView(R2.id.shop_avatar)
        ImageView imgShopAvatar;
        @BindView(R2.id.category_name)
        TextView categoryName;
        @BindView(R2.id.title)
        TextView title;
        @BindView(R2.id.status_shop_avatar)
        ImageView paymentAvatar;
        @BindView(R2.id.total_price_label)
        TextView totalLabel;
        @BindView(R2.id.total)
        TextView total;
        @BindView(R2.id.left_button)
        TextView leftButton;
        @BindView(R2.id.right_button)
        TextView rightButton;
        @BindView(R2.id.metadata)
        LinearLayout parentMetadataLayout;

        View itemView;
        String orderId;

        public OrderListViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            context.startActivity(OrderListDetailActivity.createInstance(context, orderId));
        }

        public void bindData(Order order, int position) {
            if (order != null) {
                orderId = order.id();
                parentMetadataLayout.removeAllViews();
                orderListPresenter.setViewData(order);
                orderListPresenter.setActionButtonData(order.actionButtons());
                orderListPresenter.setDotMenuVisibility(order.dotMenuList());
                ImageHandler.loadImageThumbs(context, imgShopAvatar, order.items().get(0).imageUrl());
                registerViewClickListener(this, order);
                itemView.setOnClickListener(this);
            }
        }
    }

    public interface OnMenuItemListener {
        void startUri(String uri);
    }
}
