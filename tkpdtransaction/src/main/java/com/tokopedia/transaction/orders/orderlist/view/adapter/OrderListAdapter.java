package com.tokopedia.transaction.orders.orderlist.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
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
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.UnifiedOrderListRouter;
import com.tokopedia.transaction.orders.common.view.DoubleTextView;
import com.tokopedia.transaction.orders.orderdetails.view.OrderListAnalytics;
import com.tokopedia.transaction.orders.orderlist.common.OrderListContants;
import com.tokopedia.transaction.orders.orderlist.data.ActionButton;
import com.tokopedia.transaction.orders.orderlist.data.Color;
import com.tokopedia.transaction.orders.orderlist.data.DotMenuList;
import com.tokopedia.transaction.orders.orderlist.data.MetaData;
import com.tokopedia.transaction.orders.orderlist.data.Order;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.view.presenter.ListAdapterContract;
import com.tokopedia.transaction.orders.orderlist.view.presenter.ListAdapterPresenterImpl;
import com.tokopedia.transaction.orders.orderlist.view.presenter.OrderListContract;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ListAdapterContract.View {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private static final String KEY_URI = "tokopedia";
    private static final String KEY_URI_PARAMETER = "idem_potency_key";
    private static final String KEY_URI_PARAMETER_EQUAL = "idem_potency_key=";
    private static final String KEY_FROM_PAYMENT = "?from_payment=false";
    private static final String KEY_META_DATA = "a/n";

    private final Context context;
    private ListAdapterContract.Presenter orderListPresenter;
    OrderListViewHolder currentHolder;
    List<Order> mOrderList;

    OnMenuItemListener menuListener;
    private boolean loading = false;
    OrderListAnalytics  orderListAnalytics;


    public OrderListAdapter(Context context, OnMenuItemListener listener) {
        this.context = context;
        menuListener = listener;
        orderListPresenter = new ListAdapterPresenterImpl();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        orderListAnalytics = new OrderListAnalytics(parent.getContext().getApplicationContext());
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
            currentHolder.paymentAvatar.setVisibility(View.INVISIBLE);
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
    public void setActionButtonData(ActionButton leftActionButton, ActionButton rightActionButton, int leftVisibility, int rightVisibility) {
        setButtonData(currentHolder.leftButton, leftVisibility, leftActionButton);
        setButtonData(currentHolder.rightButton, rightVisibility, rightActionButton);

    }

    private void setButtonData(TextView button, int visibility, ActionButton actionButton) {
        button.setVisibility(visibility);
        if (actionButton != null && !actionButton.label().equals("")) {
            button.setText(actionButton.label());
            if (actionButton.color() != null) {
                if (!actionButton.color().background().equals("")) {
                    button.setBackgroundColor(android.graphics.Color.parseColor(actionButton.color().background()));
                }
                if (!actionButton.color().textColor().equals("")) {
                    button.setTextColor(android.graphics.Color.parseColor(actionButton.color().textColor()));
                }
            }
            button.setOnClickListener(view -> {
                String newUri = actionButton.uri();
                if (newUri.startsWith(KEY_URI)) {
                    if (newUri.contains(KEY_URI_PARAMETER)) {
                        Uri url = Uri.parse(newUri);
                        newUri = newUri.replace(url.getQueryParameter(KEY_URI_PARAMETER), "");
                        newUri = newUri.replace(KEY_URI_PARAMETER_EQUAL, "");
                    }
                    RouteManager.route(context, newUri);
                } else if (!newUri.equals("")) {
                    try {
                        context.startActivity(((UnifiedOrderListRouter) context.getApplicationContext()).getWebviewActivityWithIntent(context,
                                URLEncoder.encode(newUri, "UTF-8")));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void setDotMenuVisibility(int visibility) {
        if (currentHolder.orderCategory.equalsIgnoreCase(OrderListContants.BELANJA) || currentHolder.orderCategory.equalsIgnoreCase(OrderListContants.MARKETPLACE)) {
            currentHolder.orderListBtnOverflow.setVisibility(View.GONE);
        } else {
            currentHolder.orderListBtnOverflow.setVisibility(visibility);
        }
    }

    @Override
    public void setCategoryAndTitle(String categoryName, String categoryTitle) {
        if (categoryName.equals(""))
            currentHolder.categoryName.setVisibility(View.GONE);
        else
            currentHolder.categoryName.setText(Html.fromHtml(Html.fromHtml(categoryName).toString()));
        currentHolder.title.setText(categoryTitle);
    }

    @Override
    public void setItemCount(int itemCount) {
        if (currentHolder.orderCategory.equalsIgnoreCase(OrderListContants.BELANJA) || currentHolder.orderCategory.equalsIgnoreCase(OrderListContants.MARKETPLACE)) {
            currentHolder.itemCount.setVisibility(View.VISIBLE);
            currentHolder.title.setVisibility(View.GONE);
            currentHolder.itemCount.setText(String.format(context.getResources().getString(R.string.item_count), itemCount));
        }
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
    public void setFailStatusBgColor(String statusColor) {
        currentHolder.status.setBackgroundColor(android.graphics.Color.parseColor(statusColor));
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
        String value = metaData.value();
        TextView tv = new TextView(context);
        if (value.contains(KEY_META_DATA)) {
            String[] values = value.split(KEY_META_DATA);
            tv.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setTextSize(10);
            tv.setTypeface(Typeface.DEFAULT_BOLD);
            tv.setText("a/n " + values[1]);
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
        if (!TextUtils.isEmpty(imageUrl)) {
            ImageHandler.loadImageThumbs(context, currentHolder.paymentAvatar, imageUrl);
            currentHolder.paymentAvatar.setVisibility(View.VISIBLE);
        }

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

    public void clearItemList() {
        mOrderList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mOrderList == null || mOrderList.size() == 0) return 1;
        return mOrderList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    private void showPopup(View v, final Order order) {
        PopupMenu popup = new PopupMenu(context, v);
        addCancelReplacementMenu(order.dotMenu(), popup);
        popup.setOnMenuItemClickListener(new OnMenuPopupClicked(order.dotMenu(), order.getAppLink()));
        popup.show();
    }

    private void addCancelReplacementMenu(List<DotMenuList> item, PopupMenu popup) {
        popup.getMenu().add(Menu.NONE, R.id.action_bantuan, Menu.NONE, "Bantuan");
        popup.getMenu().add(Menu.NONE, R.id.action_order_detail, Menu.NONE, "Lihat Order Detail");

    }

    private class OnMenuPopupClicked implements PopupMenu.OnMenuItemClickListener {
        private final List<DotMenuList> orderData;
        private final String appLink;

        OnMenuPopupClicked(List<DotMenuList> item, String appLink) {
            this.orderData = item;
            this.appLink = appLink;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.action_bantuan) {
                menuListener.startUri(context.getResources().getString(R.string.contact_us_applink));
                return true;
            } else if (item.getItemId() == R.id.action_order_detail) {
                if (appLink != null && !appLink.equals("")) {
                    RouteManager.route(context, appLink);
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
            progressBar = v.findViewById(R.id.progressBar1);
        }
    }

    class OrderListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView status;
        TextView date;
        TextView invoice;
        LinearLayout orderListBtnOverflow;
        LinearLayout conditionalInfoLayout;
        TextView conditionalInfoText;
        ImageView imgShopAvatar;
        TextView categoryName;
        TextView title;
        TextView itemCount;
        ImageView paymentAvatar;
        TextView totalLabel;
        TextView total;
        TextView leftButton;
        TextView rightButton;
        LinearLayout parentMetadataLayout;

        View itemView;
        String orderId;
        String orderCategory;
        String appLink;

        public OrderListViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            status = itemView.findViewById(R.id.list_element_status);
            date = itemView.findViewById(R.id.date);
            invoice = itemView.findViewById(R.id.invoice);
            orderListBtnOverflow = itemView.findViewById(R.id.order_list_but_overflow);
            conditionalInfoLayout = itemView.findViewById(R.id.conditional_info_layout);
            conditionalInfoText = itemView.findViewById(R.id.conditional_info);
            imgShopAvatar = itemView.findViewById(R.id.shop_avatar);
            categoryName = itemView.findViewById(R.id.category_name);
            title = itemView.findViewById(R.id.title);
            itemCount = itemView.findViewById(R.id.itemCount);
            paymentAvatar = itemView.findViewById(R.id.status_shop_avatar);
            totalLabel = itemView.findViewById(R.id.total_price_label);
            total = itemView.findViewById(R.id.total);
            leftButton = itemView.findViewById(R.id.left_button);
            rightButton = itemView.findViewById(R.id.right_button);
            parentMetadataLayout = itemView.findViewById(R.id.metadata);
        }

        @Override
        public void onClick(View view) {
            if (appLink != null && !appLink.equals("")) {
                orderListAnalytics.sendProductClickEvent(currentHolder.status.getText().toString());
                RouteManager.route(context, appLink);
            }
        }

        public void bindData(Order order, int position) {
            if (order != null) {
                orderId = order.id();
                orderCategory = order.category();
                appLink = order.getAppLink();
                if (!(orderCategory.equals(OrderCategory.DIGITAL) || orderCategory.equals(OrderCategory.FLIGHTS))) {
                    appLink = appLink + KEY_FROM_PAYMENT;

                }
                parentMetadataLayout.removeAllViews();
                orderListPresenter.setViewData(order);
                orderListPresenter.setActionButtonData(order.actionButtons());
                orderListPresenter.setDotMenuVisibility(order.dotMenu());
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
