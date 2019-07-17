package com.tokopedia.transaction.orders.orderdetails.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.ApplinkOMSConstant;
import com.tokopedia.transaction.orders.UnifiedOrderListRouter;
import com.tokopedia.transaction.orders.orderdetails.data.ActionButton;
import com.tokopedia.transaction.orders.orderdetails.data.EntityAddress;
import com.tokopedia.transaction.orders.orderdetails.data.Header;
import com.tokopedia.transaction.orders.orderdetails.data.Items;
import com.tokopedia.transaction.orders.orderdetails.data.MetaDataInfo;
import com.tokopedia.transaction.orders.orderdetails.view.activity.OrderListwebViewActivity;
import com.tokopedia.transaction.orders.orderdetails.view.customview.BookingCodeView;
import com.tokopedia.transaction.orders.orderdetails.view.customview.CustomTicketView;
import com.tokopedia.transaction.orders.orderdetails.view.customview.RedeemVoucherView;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailContract;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailPresenter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OrderListDetailContract.ActionInterface, RedeemVoucherView.SetTapActionDeals {

    public static final String KEY_BUTTON = "button";
    public static final String KEY_TEXT = "text";
    public static final String KEY_REDIRECT = "redirect";
    public static final String CONTENT_TYPE = "application/pdf";
    public static final String KEY_QRCODE = "qrcode";
    private static final int DEALS_CATEGORY_ID = 35;
    private static final int EVENTS_CATEGORY_ID = 32;
    private boolean isShortLayout;
    private List<Items> itemsList;
    private Context context;
    public static final int ITEM_DEALS = 1;
    public static final int ITEM_DEALS_SHORT = 2;
    public static final int ITEM_EVENTS = 3;
    private static final int ITEM_DEFAULT = 4;
    private String Insurance_File_Name = "E-policy Asuransi";
    OrderListDetailPresenter presenter;
    private String categoryDeals = "deal";
    private String categoryEvents = "event";
    SetEventDetails setEventDetails;
    private int position;
    private String orderId;
    private PermissionCheckerHelper permissionCheckerHelper;


    public ItemsAdapter(Context context, List<Items> itemsList, boolean isShortLayout, OrderListDetailPresenter presenter, SetEventDetails setEventDetails, String orderId) {
        this.context = context;
        this.itemsList = itemsList;
        this.isShortLayout = isShortLayout;
        this.presenter = presenter;
        this.setEventDetails = setEventDetails;
        this.orderId = orderId;
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
            case ITEM_DEALS:
                v = inflater.inflate(R.layout.voucher_item_card_deals, parent, false);
                holder = new ItemViewHolder(v, viewType);
                break;
            case ITEM_DEALS_SHORT:
                v = inflater.inflate(R.layout.voucher_item_card_deals_short, parent, false);
                holder = new ItemViewHolder(v, viewType);
                break;
            case ITEM_EVENTS:
                v = inflater.inflate(R.layout.voucher_item_card_events, parent, false);
                holder = new ItemViewHolder(v, viewType);
                break;
            case ITEM_DEFAULT:
                v = inflater.inflate(R.layout.voucher_item_default, parent, false);
                holder = new DefaultViewHolder(v, viewType);
                break;
            default:
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        this.position = position;
        if(holder instanceof  ItemViewHolder) {
            ((ItemViewHolder) holder).setIndex(position);
            ((ItemViewHolder) holder).bindData(itemsList.get(position), holder.getItemViewType());
        } else{
            ((DefaultViewHolder) holder).setIndex(position);
            ((DefaultViewHolder) holder).bindData(itemsList.get(position), holder.getItemViewType());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (itemsList.get(position).getCategory().equalsIgnoreCase(categoryDeals) || itemsList.get(position).getCategoryID() == DEALS_CATEGORY_ID) {
            if (isShortLayout)
                return ITEM_DEALS_SHORT;
            else
                return ITEM_DEALS;
        } else if(itemsList.get(position).getCategoryID() == EVENTS_CATEGORY_ID){
            return ITEM_EVENTS;
        } else {
            return ITEM_DEFAULT;
        }
    }

    @Override
    public void setActionButton(int position, List<ActionButton> actionButtons) {
        itemsList.get(position).setActionButtons(actionButtons);
        itemsList.get(position).setActionButtonLoaded(true);
        notifyItemChanged(position);
    }

    @Override
    public void setTapActionButton(int position, List<ActionButton> actionButtons) {
        itemsList.get(position).setTapActions(actionButtons);
        itemsList.get(position).setTapActionsLoaded(true);
        notifyItemChanged(position);
    }

    private View.OnClickListener getActionButtonClickListener(final String uri) {
        return view -> presenter.onClick(uri);
    }

    @Override
    public void tapActionClicked(TextView view, ActionButton actionButton, Items item) {
        if (actionButton.getControl().equalsIgnoreCase(KEY_BUTTON)) {
            presenter.setActionButton(item.getTapActions(), ItemsAdapter.this, position, true);
        } else {
            if (actionButton.getControl().equalsIgnoreCase(KEY_REDIRECT)) {
                if (!actionButton.getBody().equals("") && !actionButton.getBody().getAppURL().equals("")) {
                    if (view == null)
                        RouteManager.route(context, actionButton.getBody().getAppURL());
                    else {
                        Intent intent = null;
                        try {
                            intent = OrderListwebViewActivity.getWebViewIntent(context, URLDecoder.decode(
                                    actionButton.getBody().getAppURL(), "UTF-8"), "Redeem Voucher");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        context.startActivity(intent);
                    }
//                        ((UnifiedOrderListRouter) context.getApplicationContext())
//                                .actionOpenGeneralWebView((Activity) context, actionButton.getBody().getAppURL());
                }
            }
        }
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private ImageView dealImage;
        private TextView dealsDetails;
        private TextView brandName;
        private TextView cityName;
        private TextView validDate;
        private ProgressBar progressBar;
        private LinearLayout tapActionLayoutDeals, tapActionLayoutEvents;
        private LinearLayout actionLayout;
        private LinearLayout voucherCodeLayout;
        private CustomTicketView customTicketView;
        private View clCard;
        private View llValid;
        private View llTanggalEvent;
        private TextView tvEventDate;
        private TextView tvRightTypeofEvents;
        private TextView tvRightAddress;
        private TextView tvRightCategoryTicket;
        private TextView tvRightNumberOfBooking;
        private TextView tvValidTill;
        private TextView tanggalEventsTitle, tanggalEvents, eventCity, eventAddress;
        private int index;

        public ItemViewHolder(View itemView, int itemType) {
            super(itemView);
            this.itemView = itemView;
            if (itemType == ITEM_DEALS || itemType == ITEM_DEALS_SHORT || itemType == ITEM_EVENTS) {
                dealImage = itemView.findViewById(R.id.iv_deal);
                dealsDetails = itemView.findViewById(R.id.tv_deal_intro);
                brandName = itemView.findViewById(R.id.tv_brand_name);
                cityName = itemView.findViewById(R.id.tv_redeem_locations);
                tanggalEventsTitle = itemView.findViewById(R.id.tanggal_events_title);
                tanggalEvents = itemView.findViewById(R.id.tanggal_events);
                eventCity = itemView.findViewById(R.id.city_event);
                eventAddress = itemView.findViewById(R.id.address_event);
                voucherCodeLayout = itemView.findViewById(R.id.voucerCodeLayout);
                customTicketView = itemView.findViewById(R.id.customView2);
            }

            if (itemType == ITEM_DEALS || itemType == ITEM_EVENTS) {
                tvValidTill = itemView.findViewById(R.id.tv_valid_till);
                validDate = itemView.findViewById(R.id.tv_valid_till_date);
                tapActionLayoutDeals = itemView.findViewById(R.id.tapAction_deals);
                tapActionLayoutEvents = itemView.findViewById(R.id.tapAction_events);
                clCard = itemView.findViewById(R.id.cl_card);
                llValid = itemView.findViewById(R.id.ll_valid);
            }
            itemView.findViewById(R.id.divider1).setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            progressBar = itemView.findViewById(R.id.prog_bar);

        }

        void bindData(final Items item, int itemType) {

            MetaDataInfo metaDataInfo = null;

            if (item.getMetaData() != null) {
                Gson gson = new Gson();
                metaDataInfo = gson.fromJson(item.getMetaData(), MetaDataInfo.class);
            }
            if (metaDataInfo != null) {
                presenter.sendThankYouEvent(metaDataInfo);
                if (itemType == ITEM_DEALS || itemType == ITEM_DEALS_SHORT || itemType == ITEM_EVENTS) {
                    if (TextUtils.isEmpty(metaDataInfo.getEntityImage())) {
                        ImageHandler.loadImage(context, dealImage, item.getImageUrl(), R.color.grey_1100, R.color.grey_1100);
                    } else {
                        ImageHandler.loadImage(context, dealImage, metaDataInfo.getEntityImage(), R.color.grey_1100, R.color.grey_1100);
                    }
                    if (TextUtils.isEmpty(metaDataInfo.getEntityProductName())) {
                        dealsDetails.setText(item.getTitle());
                    } else {
                        dealsDetails.setText(metaDataInfo.getEntityProductName());
                    }
                }
                if (itemType == ITEM_DEALS) {
                    final MetaDataInfo metaDataInfo1 = metaDataInfo;
                    if (!TextUtils.isEmpty(metaDataInfo.getEndDate())) {
                        validDate.setText(" ".concat(metaDataInfo.getEndDate()));
                        llValid.setVisibility(View.VISIBLE);
                    } else {
                        llValid.setVisibility(View.GONE);
                    }
                    if (item.getActionButtons() != null && item.getActionButtons().size() > 0) {
                        setEventDetails.setEventDetails(item.getActionButtons().get(0), item);
                    }
                    brandName.setText(metaDataInfo.getEntityBrandName());
                    setEventDetails.setDetailTitle(context.getResources().getString(R.string.detail_label));
                    dealImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RouteManager.route(context, ApplinkOMSConstant.INTERNAL_DEALS + metaDataInfo1.getSeoUrl());
                        }
                    });
                }

                if (itemType == ITEM_EVENTS) {
                    final MetaDataInfo metaDataInfo1 = metaDataInfo;
                    if (metaDataInfo.getEntityPackages() != null && !TextUtils.isEmpty(metaDataInfo.getEntityPackages().get(0).getCity())) {
                        eventCity.setText(metaDataInfo.getEntityPackages().get(0).getCity());
                    }
                    if (metaDataInfo.getEntityPackages() != null && !TextUtils.isEmpty(metaDataInfo.getEntityPackages().get(0).getAddress())) {
                        eventAddress.setText(metaDataInfo.getEntityPackages().get(0).getAddress());
                    }

                    if (metaDataInfo.getIsHiburan() == 1) {
                        if (!TextUtils.isEmpty(metaDataInfo.getEndDate())) {
                            tanggalEventsTitle.setVisibility(View.VISIBLE);
                            tanggalEventsTitle.setText(context.getResources().getString(R.string.text_valid_till));
                            tanggalEvents.setText(metaDataInfo.getEndDate());
                        }
                    } else if (metaDataInfo.getIsHiburan() == 0) {
                        if (!TextUtils.isEmpty(metaDataInfo.getEndDate()) && !TextUtils.isEmpty(metaDataInfo.getStartDate())) {
                            tanggalEventsTitle.setVisibility(View.VISIBLE);
                            tanggalEventsTitle.setText(context.getResources().getString(R.string.tanggal_events));
                            tanggalEvents.setText(metaDataInfo.getStartDate() + " - " + metaDataInfo.getEndDate());
                        }
                    }

                    if (!TextUtils.isEmpty(item.getCategory())) {
                        validDate.setText(" ".concat(metaDataInfo.getEntityPackages().get(0).getDisplayName()));
                        llValid.setVisibility(View.VISIBLE);
                    } else {
                        llValid.setVisibility(View.GONE);
                    }
                    if (item.getActionButtons() != null && item.getActionButtons().size() > 0) {
                        setEventDetails.setEventDetails(item.getActionButtons().get(0), item);
                    }
                    setEventDetails.setDetailTitle(context.getResources().getString(R.string.detail_label_events));
                    dealImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RouteManager.route(context, ApplinkOMSConstant.INTERNAL_EVENTS + metaDataInfo1.getSeoUrl());
                        }
                    });
                }
                EntityAddress entityAddress = metaDataInfo.getEntityAddress();
                if (entityAddress != null) {
                    if (itemType == ITEM_DEALS || itemType == ITEM_DEALS_SHORT)
                        if (entityAddress.getName() != null) {
                            cityName.setText(entityAddress.getName());
                        }
                }
            }

            if (itemType == ITEM_DEALS) {
                if (item.getTapActions() != null && item.getTapActions().size() > 0 && !item.isTapActionsLoaded()) {
                    progressBar.setVisibility(View.VISIBLE);
                    tapActionLayoutDeals.setVisibility(View.GONE);
                    customTicketView.setVisibility(View.GONE);
                    presenter.setActionButton(item.getTapActions(), ItemsAdapter.this, getIndex(), true);
                } else if (item.getTapActions() == null || item.getTapActions().size() == 0) {
                    if (!TextUtils.isEmpty(item.getTrackingNumber())) {
                        String[] voucherCodes = item.getTrackingNumber().split(",");
                        if (voucherCodes.length > 0) {
                            voucherCodeLayout.setVisibility(View.VISIBLE);
                            for (int i = 0; i < voucherCodes.length; i++) {
                                BookingCodeView bookingCodeView = new BookingCodeView(context, voucherCodes[i], i, context.getResources().getString(R.string.voucher_code_title), voucherCodes.length);
                                bookingCodeView.setBackground(null);
                                voucherCodeLayout.addView(bookingCodeView);
                            }
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        customTicketView.setVisibility(View.GONE);
                        tapActionLayoutDeals.setVisibility(View.GONE);
                    }
                }
                if (item.isTapActionsLoaded()) {
                    progressBar.setVisibility(View.GONE);
                    customTicketView.setVisibility(View.VISIBLE);
                    tapActionLayoutDeals.setVisibility(View.VISIBLE);
                    tapActionLayoutDeals.removeAllViews();
                    int size = item.getTapActions().size();
                    for (int i = 0; i < size; i++) {
                        ActionButton actionButton = item.getTapActions().get(i);
                        RedeemVoucherView redeemVoucherView = new RedeemVoucherView(context, i, actionButton, item, ItemsAdapter.this);
                        tapActionLayoutDeals.addView(redeemVoucherView);
                    }
                }

            } else if (itemType == ITEM_EVENTS) {
                if (item.getTapActions() != null && item.getTapActions().size() > 0 && !item.isTapActionsLoaded()) {
                    progressBar.setVisibility(View.VISIBLE);
                    tapActionLayoutEvents.setVisibility(View.GONE);
                    customTicketView.setVisibility(View.GONE);
                    presenter.setActionButton(item.getTapActions(), ItemsAdapter.this, getIndex(), true);
                } else if (item.getTapActions() == null || item.getTapActions().size() == 0) {
                    if (!TextUtils.isEmpty(item.getTrackingNumber())) {
                        String[] voucherCodes = item.getTrackingNumber().split(",");
                        if (voucherCodes.length > 0) {
                            if (metaDataInfo.getTotalTicketCount() > 0) {
                                brandName.setText(String.format("%s %s", metaDataInfo.getTotalTicketCount(), context.getResources().getString(R.string.event_ticket_booking_multiple)));
                            } else {
                                brandName.setText(context.getResources().getString(R.string.event_ticket_booking_count));
                            }
                            voucherCodeLayout.setVisibility(View.VISIBLE);
                            for (int i = 0; i < voucherCodes.length; i++) {
                                BookingCodeView bookingCodeView = new BookingCodeView(context, voucherCodes[i], i, context.getResources().getString(R.string.voucher_code_title), voucherCodes.length);
                                bookingCodeView.setBackground(null);
                                voucherCodeLayout.addView(bookingCodeView);
                            }
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        customTicketView.setVisibility(View.GONE);
                        tapActionLayoutEvents.setVisibility(View.GONE);
                    }
                }
                if (item.isTapActionsLoaded()) {
                    progressBar.setVisibility(View.GONE);
                    customTicketView.setVisibility(View.VISIBLE);
                    tapActionLayoutEvents.setVisibility(View.VISIBLE);
                    tapActionLayoutEvents.removeAllViews();
                    int size = item.getTapActions().size();
                    for (int i = 0; i < size; i++) {
                        ActionButton actionButton = item.getTapActions().get(i);
                        TextView tapActionTextView = renderActionButtons(i, actionButton, item);
                        if (actionButton.getControl().equalsIgnoreCase(KEY_BUTTON)) {
                            presenter.setActionButton(item.getTapActions(), ItemsAdapter.this, getIndex(), true);
                        } else {
                            setActionButtonClick(tapActionTextView, actionButton, item, metaDataInfo.getTotalTicketCount());
                        }
                        tapActionLayoutEvents.addView(tapActionTextView);
                    }
                }
            }
        }


        private void setActionButtonClick(TextView view, ActionButton actionButton, Items item, int totalTicketCount) {
            if (actionButton.getControl().equalsIgnoreCase(KEY_REDIRECT)) {
                if (totalTicketCount > 0) {
                    brandName.setText(String.format("%s %s", totalTicketCount, context.getResources().getString(R.string.event_ticket_voucher_multiple)));
                } else {
                    brandName.setText(context.getResources().getString(R.string.event_ticket_voucher_count));
                }
                if (!actionButton.getBody().equals("") && !actionButton.getBody().getAppURL().equals("")) {
                    if (view == null) {
                        RouteManager.route(context, actionButton.getBody().getAppURL());
                    } else if (isDownloadable(actionButton)) {
                        presenter.setDownloadableFlag(true);
                        presenter.setDownloadableFileName("Tokopedia E-Ticket");
                        view.setOnClickListener(getActionButtonClickListener(actionButton.getBody().getAppURL()));
                    } else {
                        presenter.setDownloadableFlag(false);
                        view.setOnClickListener(getActionButtonClickListener(actionButton.getBody().getAppURL()));
                    }
                }
            } else if (actionButton.getControl().equalsIgnoreCase(KEY_QRCODE)) {
                if (totalTicketCount > 0) {
                    brandName.setText(String.format("%s %s", totalTicketCount, context.getResources().getString(R.string.event_ticket_qrcode_multiple)));
                } else {
                    brandName.setText(context.getResources().getString(R.string.event_ticket_qrcode_count));
                }
                view.setOnClickListener(v -> {
                    setEventDetails.openShowQRFragment(actionButton, item);
                });
            }
        }

        private boolean isDownloadable(ActionButton actionButton) {

            Header header = null;

            if (!TextUtils.isEmpty(actionButton.getHeader())) {
                Gson gson = new Gson();
                header = gson.fromJson(actionButton.getHeader(), Header.class);

                if (header.getContentType().equalsIgnoreCase(CONTENT_TYPE)) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        private TextView renderActionButtons(int position, ActionButton actionButton, Items item) {

            TextView tapActionTextView = new TextView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, (int) context.getResources().getDimension(R.dimen.dp_8), 0, 0);
            tapActionTextView.setPadding((int) context.getResources().getDimension(R.dimen.dp_16), (int) context.getResources().getDimension(R.dimen.dp_16), (int) context.getResources().getDimension(R.dimen.dp_16), (int) context.getResources().getDimension(R.dimen.dp_16));
            tapActionTextView.setLayoutParams(params);
            tapActionTextView.setTextColor(Color.WHITE);
            tapActionTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            tapActionTextView.setText(actionButton.getLabel());
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
            tapActionTextView.setBackground(shape);
            if (!actionButton.getActionColor().getTextColor().equals("")) {
                tapActionTextView.setTextColor(android.graphics.Color.parseColor(actionButton.getActionColor().getTextColor()));
            } else {
                tapActionTextView.setTextColor(Color.WHITE);
            }


            if (position == item.getTapActions().size() - 1 && (item.getActionButtons() != null || item.getActionButtons().size() == 0)) {
                float radius = context.getResources().getDimension(R.dimen.dp_4);
                shape.setCornerRadii(new float[]{0, 0, 0, 0, radius, radius, radius, radius});

            } else {

                shape.setCornerRadius(context.getResources().getDimension(R.dimen.dp_4));
            }

            tapActionTextView.setBackground(shape);

            return tapActionTextView;
        }

        /*
        private void setButtonLayout(List<ActionButton> tapActions, )*/


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

    public interface SetEventDetails {
        void setEventDetails(ActionButton actionButton, Items item);

        void openShowQRFragment(ActionButton actionButton, Items item);

        void setDetailTitle(String title);

    }

    private class DefaultViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView validDate;
        private ProgressBar progressBar;
        private CustomTicketView customTicketView1;
        private CustomTicketView customTicketView2;
        private View clCard;
        private View llValid;
        private View llTanggalEvent;
        private TextView tvEventDate;
        private TextView tvRightTypeofEvents;
        private TextView tvRightAddress;
        private TextView tvRightCategoryTicket;
        private TextView tvRightNumberOfBooking;
        private LinearLayout tapActionLayout;
        private LinearLayout actionLayout;
        private TextView tvValidTill;
        private int index;

        public DefaultViewHolder(View itemView, int itemType) {
            super(itemView);
            this.itemView = itemView;
            customTicketView1 = itemView.findViewById(R.id.customView1);
            customTicketView2 = itemView.findViewById(R.id.customView2);

            tvValidTill = itemView.findViewById(R.id.tv_valid_till);
            validDate = itemView.findViewById(R.id.tv_valid_till_date);
            clCard = itemView.findViewById(R.id.cl_card);
            llValid = itemView.findViewById(R.id.ll_valid);
            itemView.findViewById(R.id.divider1).setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            progressBar = itemView.findViewById(R.id.prog_bar);
            tvRightTypeofEvents = itemView.findViewById(R.id.right_text1);
            tvRightAddress = itemView.findViewById(R.id.right_text2);
            tvRightCategoryTicket = itemView.findViewById(R.id.right_text3);
            tvRightNumberOfBooking = itemView.findViewById(R.id.right_text4);
            llTanggalEvent = itemView.findViewById(R.id.ll_tanggal_event);
            tvEventDate = itemView.findViewById(R.id.tv_start_date);
            tapActionLayout = itemView.findViewById(R.id.tapAction);
            actionLayout = itemView.findViewById(R.id.actionButton);
        }

        void bindData(final Items item, int itemType) {
            MetaDataInfo metaDataInfo = null;
            boolean hasViews=false;

            if (item.getMetaData() != null) {
                Gson gson = new Gson();
                metaDataInfo = gson.fromJson(item.getMetaData(), MetaDataInfo.class);
            }

            if (metaDataInfo != null) {
                presenter.sendThankYouEvent(metaDataInfo);
                setEventDetails.setDetailTitle(context.getResources().getString(R.string.purchase_detail));
                if (!TextUtils.isEmpty(metaDataInfo.getEndDate())) {
                    validDate.setText(" ".concat(metaDataInfo.getEndDate()));
                    llValid.setVisibility(View.VISIBLE);
                    hasViews = true;
                } else {
                    llValid.setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(metaDataInfo.getEntityProductName())) {
                    itemView.findViewById(R.id.ll_details1).setVisibility(View.GONE);
                } else {
                    hasViews = true;
                    tvRightTypeofEvents.setText(metaDataInfo.getEntityProductName());
                }
                if (metaDataInfo.getEntityPackages() != null && metaDataInfo.getEntityPackages().size() > 0) {
                    if (TextUtils.isEmpty(metaDataInfo.getEntityPackages().get(0).getAddress())) {
                        itemView.findViewById(R.id.ll_details2).setVisibility(View.GONE);
                    } else {
                        hasViews = true;
                        tvRightAddress.setText(metaDataInfo.getEntityPackages().get(0).getAddress());
                    }
                } else {
                    itemView.findViewById(R.id.ll_details2).setVisibility(View.GONE);
                }
                if (metaDataInfo.getEntityPackages() != null && metaDataInfo.getEntityPackages().size() > 0) {
                    if (TextUtils.isEmpty(metaDataInfo.getEntityPackages().get(0).getDisplayName())) {
                        itemView.findViewById(R.id.ll_details3).setVisibility(View.GONE);
                    } else {
                        tvRightCategoryTicket.setText(metaDataInfo.getEntityPackages().get(0).getDisplayName());
                        hasViews = true;
                    }
                } else {
                    itemView.findViewById(R.id.ll_details3).setVisibility(View.GONE);
                }
                if (item.getQuantity() == 0) {
                    itemView.findViewById(R.id.ll_details4).setVisibility(View.GONE);
                } else {
                    hasViews = true;
                    tvRightNumberOfBooking.setText(String.valueOf(metaDataInfo.getTotalTicketCount()));
                }
                if (!TextUtils.isEmpty(metaDataInfo.getStartDate())) {
                    hasViews = true;
                    tvEventDate.setText(" ".concat(metaDataInfo.getStartDate()));
                    llTanggalEvent.setVisibility(View.VISIBLE);
                } else {
                    llTanggalEvent.setVisibility(View.GONE);
                }
                if (item.getTapActions() != null && item.getTapActions().size() > 0 && !item.isTapActionsLoaded()) {
                    progressBar.setVisibility(View.VISIBLE);
                    tapActionLayout.setVisibility(View.GONE);
                    presenter.setActionButton(item.getTapActions(), ItemsAdapter.this, getIndex(), true);
                }
                if(!hasViews){
                    customTicketView1.setVisibility(View.GONE);
                    itemView.findViewById(R.id.divider1).setVisibility(View.GONE);
                }

                if (item.isTapActionsLoaded()) {
                    progressBar.setVisibility(View.GONE);
                    if (item.getTapActions() == null || item.getTapActions().size() == 0) {
                        tapActionLayout.setVisibility(View.GONE);
                    } else {
                        tapActionLayout.setVisibility(View.VISIBLE);
                        tapActionLayout.removeAllViews();
                        int size = item.getTapActions().size();
                        for (int i = 0; i < size; i++) {
                            ActionButton actionButton = item.getTapActions().get(i);
                            TextView tapActionTextView = renderActionButtons(i, actionButton, item);
                            if (actionButton.getControl().equalsIgnoreCase(KEY_BUTTON)) {
                                presenter.setActionButton(item.getTapActions(), ItemsAdapter.this, getIndex(), true);
                            } else {
                                setActionButtonClick(tapActionTextView, actionButton);
                            }
                            tapActionLayout.addView(tapActionTextView);
                        }
                    }
                } else if (item.getTapActions() == null || item.getTapActions().size() == 0) {
                    progressBar.setVisibility(View.GONE);
                }

                if (item.getActionButtons() == null || item.getActionButtons().size() == 0) {
                    actionLayout.setVisibility(View.GONE);
                } else {
                    actionLayout.setVisibility(View.VISIBLE);
                    actionLayout.removeAllViews();
                    int size = item.getActionButtons().size();
                    for (int i = 0; i < size; i++) {
                        ActionButton actionButton = item.getActionButtons().get(i);

                        TextView actionTextView = renderActionButtons(i, actionButton, item);
                        if (!actionButton.getControl().equalsIgnoreCase(KEY_TEXT)) {
                            if (item.isActionButtonLoaded()) {
                                setActionButtonClick(null, actionButton);
                            } else {
                                actionTextView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (actionButton.getControl().equalsIgnoreCase(KEY_BUTTON)) {
                                            presenter.setActionButton(item.getActionButtons(), ItemsAdapter.this, getIndex(), false);
                                        } else {
                                            setActionButtonClick(actionTextView, actionButton);
                                        }

                                    }
                                });
                            }
                        }
                        actionLayout.addView(actionTextView);
                    }
                }
            }
        }

        private void setActionButtonClick(TextView view, ActionButton actionButton) {
            if (actionButton.getControl().equalsIgnoreCase(KEY_REDIRECT)) {
                if (!actionButton.getBody().equals("") && !actionButton.getBody().getAppURL().equals("")) {
                    if (view == null)
                        RouteManager.route(context, actionButton.getBody().getAppURL());
                    else
                        view.setOnClickListener(getActionButtonClickListener(actionButton.getBody().getAppURL()));
                }
            }
        }

        private TextView renderActionButtons(int position, ActionButton actionButton, Items item) {

            TextView tapActionTextView = new TextView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, (int) context.getResources().getDimension(R.dimen.dp_8), 0, 0);
            tapActionTextView.setPadding((int) context.getResources().getDimension(R.dimen.dp_16), (int) context.getResources().getDimension(R.dimen.dp_16), (int) context.getResources().getDimension(R.dimen.dp_16), (int) context.getResources().getDimension(R.dimen.dp_16));
            tapActionTextView.setLayoutParams(params);
            tapActionTextView.setTextColor(Color.WHITE);
            tapActionTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            tapActionTextView.setText(actionButton.getLabel());
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
            tapActionTextView.setBackground(shape);
            if (!actionButton.getActionColor().getTextColor().equals("")) {
                tapActionTextView.setTextColor(android.graphics.Color.parseColor(actionButton.getActionColor().getTextColor()));
            } else {
                tapActionTextView.setTextColor(Color.WHITE);
            }


            if (position == item.getTapActions().size() - 1 && (item.getActionButtons() != null || item.getActionButtons().size() == 0)) {
                float radius = context.getResources().getDimension(R.dimen.dp_4);
                shape.setCornerRadii(new float[]{0, 0, 0, 0, radius, radius, radius, radius});

            } else {

                shape.setCornerRadius(context.getResources().getDimension(R.dimen.dp_4));
            }

            tapActionTextView.setBackground(shape);

            return tapActionTextView;
        }


        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
