package com.tokopedia.transaction.orders.orderlist.view.adapter;

import android.app.Activity;
import android.content.Context;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AlertDialog;
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
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.orders.orderlist.data.ActionButton;
import com.tokopedia.transaction.orders.orderlist.data.Color;
import com.tokopedia.transaction.orders.orderlist.data.DotMenuList;
import com.tokopedia.transaction.orders.orderlist.data.MetaData;
import com.tokopedia.transaction.orders.orderlist.data.Order;
import com.tokopedia.transaction.orders.orderlist.data.Popup;
import com.tokopedia.transaction.orders.orderlist.view.customview.DoubleTextView;
import com.tokopedia.transaction.orders.orderlist.view.presenter.ListAdapterListenter;
import com.tokopedia.transaction.orders.orderlist.view.presenter.ListAdapterPresenter;
import com.tokopedia.transaction.orders.orderlist.view.presenter.ListAdapterPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ListAdapterListenter {
    private static final String HAS_BUTTON = "1";
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private final LayoutInflater inflater;
    private final Context context;
    private ListAdapterPresenter orderListPresenter;
    OrderListViewHolder currentHolder;
    ArrayList<Order> mOrderList;

    OnMenuItemListener menuListener;
    private boolean loading = false;


    public OrderListAdapter(Context context, OnMenuItemListener listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        menuListener = listener;
        orderListPresenter = new ListAdapterPresenterImpl(this);

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
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OrderListViewHolder) {
            currentHolder = (OrderListViewHolder) holder;
            if (mOrderList != null) {
                Order order = mOrderList.get(position);
                orderListPresenter.setViewData(order);
                currentHolder.parentMetadataLayout.removeAllViews();
                orderListPresenter.setActionButtonData(order.actionButtons());
                orderListPresenter.setDotMenuVisibility(order.dotMenuList());
                ImageHandler.loadImageThumbs(context, currentHolder.imgShopAvatar, order.items().get(0).imageUrl());
                registerViewClickListener(currentHolder, order);
            }
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    private void registerViewClickListener(OrderListViewHolder holder, final Order order) {
        holder.orderListBtnOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view, order.dotMenuList());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrderList != null ? mOrderList.size() : 0;
    }

    public void addAll(List<Order> orderDataList) {
        Log.e("sandeep", (orderDataList != null ? orderDataList.toString() : null));
        mOrderList = new ArrayList<>(orderDataList);
        Log.e("sandeep", "adding listener");
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
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(1);
        if(bgColor != null){
            if(!bgColor.background().equals("")){
                shape.setColor(android.graphics.Color.parseColor(bgColor.background()));
            }
            if(!bgColor.border().equals("")){
                shape.setStroke(2, android.graphics.Color.parseColor(bgColor.border()));
            }
            if(!(bgColor.background().equals("") && bgColor.border().equals(""))) {
                button.setBackground(shape);
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonUri.startsWith("tokopedia")){
                    Log.e("sandeep","deeplink "+buttonUri);
                    RouteManager.route(context,buttonUri);
                }else{
                    Log.e("sandeep","webview = "+buttonUri);
                    context.startActivity(SimpleWebViewWithFilePickerActivity.getIntent(context, buttonUri));
                }
            }
        });
        if (popup != null) {
        }
    }

    private void createPopupWindow(String title, String popuptext, List<ActionButton> button) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.order_list_popup_item, null);
        dialogBuilder.setView(dialogView);

        TextView popup_title = dialogView.findViewById(R.id.title);
        TextView popup_text = dialogView.findViewById(R.id.popup_text);
        TextView primary_button = dialogView.findViewById(R.id.primary_button);
        TextView secondary_button = dialogView.findViewById(R.id.secondary_button);
        popup_title.setText(title);
        popup_text.setText(popuptext);
        primary_button.setText(button.get(0).label());
        secondary_button.setText(button.get(1).label());

        final AlertDialog alertDialog = dialogBuilder.create();
        primary_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        secondary_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
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
        if(textColor.length() > 0){
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
        if(conditionalInfoText != null) {
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
    public void setStatusBgColor(int statusColor) {
        currentHolder.status.setBackgroundColor(statusColor);
    }

    @Override
    public void setStatus(String statusText) {
        currentHolder.status.setText(statusText);
    }

    @Override
    public void setMetaDataToCustomView(MetaData metaData) {
        DoubleTextView childLayout = new DoubleTextView(context, null);
        childLayout.setTopText(metaData.label());
        childLayout.setBottomText(metaData.value());
        currentHolder.parentMetadataLayout.addView(childLayout);

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

    private void showPopup(View v, final List<DotMenuList> item) {
        PopupMenu popup = new PopupMenu(context, v);
        addCancelReplacementMenu(item, popup);
        popup.setOnMenuItemClickListener(new OnMenuPopupClicked(item));
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

        OnMenuPopupClicked(List<DotMenuList> item) {
            this.orderData = item;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.action_cancel_replacement) {
                menuListener.startUri(orderData.get(0).uri());
                return true;
            } else if (item.getItemId() == R.id.action_next_replacement) {
                menuListener.startUri(orderData.get(1).uri());
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

    class OrderListViewHolder extends RecyclerView.ViewHolder {
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

        public OrderListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnMenuItemListener {
        void startUri(String uri);
    }
}
