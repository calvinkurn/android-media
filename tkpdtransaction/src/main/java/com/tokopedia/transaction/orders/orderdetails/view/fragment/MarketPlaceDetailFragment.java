package com.tokopedia.transaction.orders.orderdetails.view.fragment;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.UnifiedOrderListRouter;
import com.tokopedia.transaction.orders.common.view.DoubleTextView;
import com.tokopedia.transaction.orders.orderdetails.data.ActionButton;
import com.tokopedia.transaction.orders.orderdetails.data.AdditionalInfo;
import com.tokopedia.transaction.orders.orderdetails.data.ContactUs;
import com.tokopedia.transaction.orders.orderdetails.data.Detail;
import com.tokopedia.transaction.orders.orderdetails.data.DriverDetails;
import com.tokopedia.transaction.orders.orderdetails.data.DropShipper;
import com.tokopedia.transaction.orders.orderdetails.data.Invoice;
import com.tokopedia.transaction.orders.orderdetails.data.Items;
import com.tokopedia.transaction.orders.orderdetails.data.OrderToken;
import com.tokopedia.transaction.orders.orderdetails.data.PayMethod;
import com.tokopedia.transaction.orders.orderdetails.data.Pricing;
import com.tokopedia.transaction.orders.orderdetails.data.ShopInfo;
import com.tokopedia.transaction.orders.orderdetails.data.Status;
import com.tokopedia.transaction.orders.orderdetails.data.Title;
import com.tokopedia.transaction.orders.orderdetails.di.OrderDetailsComponent;
import com.tokopedia.transaction.orders.orderdetails.view.OrderListAnalytics;
import com.tokopedia.transaction.orders.orderdetails.view.activity.RequestCancelActivity;
import com.tokopedia.transaction.orders.orderdetails.view.adapter.ProductItemAdapter;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailContract;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailPresenter;
import com.tokopedia.transaction.orders.orderlist.data.ConditionalInfo;
import com.tokopedia.transaction.orders.orderlist.data.PaymentData;
import com.tokopedia.transaction.purchase.detail.activity.OrderHistoryActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.inject.Inject;

import static android.content.Context.CLIPBOARD_SERVICE;

public class MarketPlaceDetailFragment extends BaseDaggerFragment implements OrderListDetailContract.View {

    public static final String KEY_ORDER_ID = "OrderId";
    public static final String KEY_ORDER_CATEGORY = "OrderCategory";
    public static final String KEY_FROM_PAYMENT = "from_payment";
    public static final String ORDER_LIST_URL_ENCODING = "UTF-8";
    public static final int REQUEST_CANCEL_ORDER = 101;
    @Inject
    OrderListDetailPresenter presenter;
    LinearLayout mainView;
    TextView statusLabel;
    TextView statusValue;
    TextView conditionalInfoText;
    LinearLayout statusDetail;
    TextView invoiceView;
    TextView lihat;
    TextView detailLabel;
    LinearLayout detailContent;
    TextView additionalText;
    LinearLayout additionalInfoLayout;
    TextView infoLabel;
    LinearLayout infoValue;
    LinearLayout totalPrice;
    TextView helpLabel;
    LinearLayout actionBtnLayout;
    TextView primaryActionBtn;
    TextView secondaryActionBtn;
    FrameLayout parentLayout;
    RecyclerView itemsRecyclerView;
    TextView productInformationTitle;
    LinearLayout paymentMethod;
    private boolean isSingleButton;
    private ClipboardManager myClipboard;
    CardView driverLayout, dropShipperLayout;
    TextView statusLihat;
    FrameLayout progressBarLayout;
    private ClipData myClip;
    @Inject
    OrderListAnalytics orderListAnalytics;
    private ShopInfo shopInfo;
    private Status status;


    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(OrderDetailsComponent.class).inject(this);
    }

    public static Fragment getInstance(String orderId, String orderCategory) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ORDER_ID, orderId);
        bundle.putString(KEY_ORDER_CATEGORY, orderCategory);
        Fragment fragment = new MarketPlaceDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list_detail, container, false);
        parentLayout = view.findViewById(R.id.parentLayout);
        mainView = view.findViewById(R.id.main_view);
        statusLabel = view.findViewById(R.id.status_label);
        statusValue = view.findViewById(R.id.status_value);
        conditionalInfoText = view.findViewById(R.id.conditional_info);
        statusDetail = view.findViewById(R.id.status_detail);
        invoiceView = view.findViewById(R.id.invoice);
        statusLihat = view.findViewById(R.id.lihat_status);
        lihat = view.findViewById(R.id.lihat);
        detailLabel = view.findViewById(R.id.detail_label);
        detailContent = view.findViewById(R.id.detail_content);
        additionalText = view.findViewById(R.id.additional);
        additionalInfoLayout = view.findViewById(R.id.additional_info);
        infoLabel = view.findViewById(R.id.info_label);
        infoValue = view.findViewById(R.id.info_value);
        totalPrice = view.findViewById(R.id.total_price);
        helpLabel = view.findViewById(R.id.help_label);
        actionBtnLayout = view.findViewById(R.id.actionBtnLayout);
        primaryActionBtn = view.findViewById(R.id.langannan);
        secondaryActionBtn = view.findViewById(R.id.beli_lagi);
        itemsRecyclerView = view.findViewById(R.id.rv_items);
        productInformationTitle = view.findViewById(R.id.product_info_label);
        paymentMethod = view.findViewById(R.id.info_payment_method);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        myClipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
        setMainViewVisible(View.GONE);
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.setOrderDetailsContent((String) getArguments().get(KEY_ORDER_ID), (String) getArguments().get(KEY_ORDER_CATEGORY), getArguments().getString(KEY_FROM_PAYMENT));
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
        statusLabel.setText(status.statusLabel());
        statusValue.setText(status.statusText());
        if (!status.textColor().equals(""))
            statusValue.setTextColor(Color.parseColor(status.textColor()));
        statusLihat.setVisibility(View.VISIBLE);
        statusLihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String applink = "http://m.tokopedia.com/myorder/buyer/detail/history";
                applink = applink + getArguments().get(KEY_ORDER_ID);

                Intent intent = OrderHistoryActivity
                        .createInstance(getActivity(), (String) getArguments().get(KEY_ORDER_ID), 1);
                startActivity(intent);
            }
        });
    }

    @Override
    public void setConditionalInfo(ConditionalInfo conditionalInfo) {

        conditionalInfoText.setVisibility(View.VISIBLE);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.dp_9));
        shape.setColor(android.graphics.Color.parseColor(conditionalInfo.color().background()));
        shape.setStroke(getResources().getDimensionPixelOffset(R.dimen.dp_1), android.graphics.Color.parseColor(conditionalInfo.color().border()));
        conditionalInfoText.setBackground(shape);
        conditionalInfoText.setPadding(getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_16));
        conditionalInfoText.setText(conditionalInfo.text());

    }

    @Override
    public void setTitle(Title title) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(title.label());
        doubleTextView.setBottomText(title.value());
        statusDetail.addView(doubleTextView);
    }

    @Override
    public void setInvoice(final Invoice invoice) {
        invoiceView.setText(invoice.invoiceRefNum());
        if (invoice.invoiceUrl().equals("")) {
            lihat.setVisibility(View.GONE);
        }
        lihat.setOnClickListener(view -> {
            orderListAnalytics.sendViewInvoiceClickEvent();
            try {
                startActivity(((UnifiedOrderListRouter) getActivity()
                        .getApplication()).getWebviewActivityWithIntent(getContext(),
                        URLEncoder.encode(invoice.invoiceUrl(), ORDER_LIST_URL_ENCODING)));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void setOrderToken(OrderToken orderToken) {

    }

    @Override
    public void setDetail(Detail detail) {
        detailLabel.setText("Detail Pengiriman");
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        if (!detail.label().equalsIgnoreCase("No. Resi")) {
            doubleTextView.setTopText(detail.label());
            doubleTextView.setBottomText(detail.value());
        } else {
            doubleTextView.setTopText(detail.label());
            String text = detail.value() + "\n\nSalin No. Resi";
            SpannableString spannableString = new SpannableString(text);
            int startIndexOfLink = text.indexOf("Salin");
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    try {
                        myClip = ClipData.newPlainText("text", detail.value());
                        myClipboard.setPrimaryClip(myClip);
                        ToasterNormal.showClose(getActivity(), "Nomor Resi berhasil disalin");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setColor(getResources().getColor(R.color.green_250)); // specific color for this link
                }
            }, startIndexOfLink, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            doubleTextView.setBottomText(spannableString);
        }
        detailContent.addView(doubleTextView);
    }

    @Override
    public void setAdditionInfoVisibility(int visibility) {
        additionalText.setVisibility(visibility);
        additionalText.setOnClickListener(view -> {
            additionalText.setOnClickListener(null);
            additionalText.setText(getResources().getString(R.string.additional_text));
            additionalText.setTypeface(Typeface.DEFAULT_BOLD);
            additionalText.setTextColor(getResources().getColor(R.color.black_70));
            additionalInfoLayout.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(additionalInfo.label());
        doubleTextView.setBottomText(additionalInfo.value());
        additionalInfoLayout.addView(doubleTextView);
    }

    @Override
    public void setPricing(Pricing pricing) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(pricing.label());
        doubleTextView.setBottomText(pricing.value());
        doubleTextView.setBottomTextSize(16);
        doubleTextView.setBottomGravity(Gravity.RIGHT);
        infoValue.addView(doubleTextView);
    }

    @Override
    public void setPayMethodInfo(PayMethod payMethod) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(payMethod.getLabel());
        doubleTextView.setBottomText(payMethod.getValue());
        doubleTextView.setBottomTextSize(16);
        doubleTextView.setBottomGravity(Gravity.RIGHT);
        paymentMethod.addView(doubleTextView);
    }

    @Override
    public void setButtonMargin() {
        this.isSingleButton = true;
    }

    @Override
    public void showDropshipperInfo(DropShipper dropShipper) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dropshipper_info, null);
        TextView dropShipperName = view.findViewById(R.id.dropShipper_name);
        TextView dropShipperNumber = view.findViewById(R.id.dropShipper_phone);
        dropShipperName.setText(dropShipper.getDropShipperName());
        dropShipperNumber.setText(dropShipper.getDropShipperPhone());
        detailContent.addView(view);
    }

    @Override
    public void showDriverInfo(DriverDetails driverDetails) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.driver_info, null);
        driverLayout = view.findViewById(R.id.driverLayout);
        ImageView driverImage = view.findViewById(R.id.driver_img);
        TextView driverName = view.findViewById(R.id.driver_name);
        TextView driverNum = view.findViewById(R.id.driver_phone);
        TextView driverlicense = view.findViewById(R.id.driver_license);
        Button callDriver = view.findViewById(R.id.call_driver);

        if (driverDetails != null) {
            if (!TextUtils.isEmpty(driverDetails.getPhotoUrl())) {
                ImageHandler.loadImageCircle2(getContext(), driverImage, driverDetails.getPhotoUrl());
                driverLayout.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(driverDetails.getDriverName())) {
                driverName.setText(driverDetails.getDriverName());
                driverLayout.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(driverDetails.getDriverPhone())) {
                driverNum.setText(driverDetails.getDriverPhone());
                driverLayout.setVisibility(View.VISIBLE);
                callDriver.setVisibility(View.VISIBLE);
                callDriver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDialCaller(driverDetails.getDriverPhone());
                    }
                });
            }
            if (!TextUtils.isEmpty(driverDetails.getLicenseNumber())) {
                driverlicense.setText(driverDetails.getLicenseNumber());
                driverLayout.setVisibility(View.VISIBLE);
            }
            detailContent.addView(view);
        }
    }

    @Override
    public void showProgressBar() {
        progressBarLayout.setVisibility(View.VISIBLE);
        mainView.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {
        progressBarLayout.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setActionButtons(List<ActionButton> actionButtons) {
        actionBtnLayout.removeAllViews();
        actionBtnLayout.setOrientation(LinearLayout.VERTICAL);
        for (ActionButton actionButton : actionButtons) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_0), getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_16));
            TextView textView = new TextView(getContext());
            textView.setText(actionButton.getLabel());
            textView.setPadding(16, 20, 16, 20);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setGravity(Gravity.CENTER);
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.dp_4));
            if (!actionButton.getActionColor().getBackground().equals("")) {
                shape.setColor((Color.parseColor(actionButton.getActionColor().getBackground())));
            }
            if (!actionButton.getActionColor().getBorder().equals("")) {
                shape.setStroke(getResources().getDimensionPixelSize(R.dimen.dp_2), Color.parseColor(actionButton.getActionColor().getBorder()));
            }
            textView.setBackground(shape);
            textView.setLayoutParams(params);
            if (!actionButton.getActionColor().getTextColor().equals("")) {
                textView.setTextColor(Color.parseColor(actionButton.getActionColor().getTextColor()));
            }
            if (!TextUtils.isEmpty(actionButton.getUri())) {
                textView.setOnClickListener(clickActionButton(actionButton));
            }
            actionBtnLayout.addView(textView);
        }
    }

    @Override
    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    @Override
    public void showReplacementView(List<String> reasons) {
    }

    @Override
    public void finishOrderDetail() {
        getActivity().finish();
    }


    private View.OnClickListener clickActionButton(ActionButton actionButton) {
        if (!TextUtils.isEmpty(actionButton.getKey())) {
            String orderStatusEvent = "";
            switch (actionButton.getKey()) {
                case "ask_seller":
                    orderStatusEvent = "click ask seller";
                    break;
                case "request_cancel":
                    orderStatusEvent = "click request cancel";
                    break;
                case "receive_confirmation":
                    orderStatusEvent = "";
                    break;
                case "track":
                    orderStatusEvent = "click track";
                    break;
                case "complaint":
                    orderStatusEvent = "click complain";
                    break;
                case "finish_order":
                    orderStatusEvent = "click finished";
                    break;
                case "view_complaint":
                    orderStatusEvent = "click view complain";
                    break;
                case "cancel_peluang":
                    orderStatusEvent = "click cancel search";
                    break;
                default:
                    break;
            }
            orderListAnalytics.sendActionButtonClickEvent(orderStatusEvent);
        }
        return view -> {
            if (actionButton.getActionButtonPopUp() != null && !TextUtils.isEmpty(actionButton.getActionButtonPopUp().getTitle())) {
                final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
                dialog.setTitle(actionButton.getActionButtonPopUp().getTitle());
                dialog.setDesc(actionButton.getActionButtonPopUp().getBody());
                if (actionButton.getActionButtonPopUp().getActionButtonList() != null && actionButton.getActionButtonPopUp().getActionButtonList().size() > 0) {
                    dialog.setBtnOk(actionButton.getActionButtonPopUp().getActionButtonList().get(1).getLabel());
                    dialog.setOnOkClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(actionButton.getActionButtonPopUp().getActionButtonList().get(1).getUri())) {
                                if (actionButton.getActionButtonPopUp().getActionButtonList().get(1).getLabel().equalsIgnoreCase("Selesai")) {
                                    presenter.finishOrder((String) getArguments().get(KEY_ORDER_ID));
                                    dialog.dismiss();
                                } else if (actionButton.getActionButtonPopUp().getActionButtonList().get(1).getLabel().equalsIgnoreCase("Komplain")) {
                                    Intent newIntent = InboxRouter.getCreateResCenterActivityIntent(getContext(),
                                            (String) getArguments().get(KEY_ORDER_ID));
                                    startActivityForResult(newIntent, TransactionPurchaseRouter.CREATE_RESCENTER_REQUEST_CODE);
                                } else
                                     RouteManager.route(getContext(), actionButton.getActionButtonPopUp().getActionButtonList().get(1).getUri());
                            } else {
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.setBtnCancel(actionButton.getActionButtonPopUp().getActionButtonList().get(0).getLabel());
                    dialog.setOnCancelClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(actionButton.getActionButtonPopUp().getActionButtonList().get(0).getUri())) {
                                RouteManager.route(getContext(), actionButton.getActionButtonPopUp().getActionButtonList().get(0).getUri());
                            } else {
                                dialog.dismiss();
                            }
                        }
                    });
                }
                dialog.show();
            } else if (!TextUtils.isEmpty(actionButton.getUri())) {
                if (actionButton.getUri().contains("askseller")) {
                    if (shopInfo != null) {
                        String shopId = String.valueOf(this.shopInfo.getShopId());
                        String shopName = this.shopInfo.getShopName();
                        String shopLogo = this.shopInfo.getShopLogo();
                        String shopUrl = this.shopInfo.getShopUrl();
                        String applink = actionButton.getUri();
                        applink = applink.concat("&shopId=" + shopId + "&shopName=" + shopName + "&shopLogo=" + shopLogo + "&shopUrl=" + shopUrl);
                        RouteManager.route(getContext(), applink);
                    }
                } else if (!TextUtils.isEmpty(actionButton.getUri())) {
                    Intent intent = new Intent(getContext(), RequestCancelActivity.class);
                    intent.putExtra(KEY_ORDER_ID, (String) getArguments().get(KEY_ORDER_ID));
                    if (this.status.status().equals("220") || this.status.status().equals("400")) {
                        intent.putExtra("cancelFragment", 1);
                        startActivityForResult(intent, REQUEST_CANCEL_ORDER);
                    } else if (this.status.status().equals("11")) {
                        intent.putExtra("cancelFragment", 0);
                        startActivityForResult(intent, REQUEST_CANCEL_ORDER);
                    } else if (actionButton.getLabel().equalsIgnoreCase("Lacak")) {
                        String routingAppLink;
                        routingAppLink = ApplinkConst.ORDER_TRACKING.replace("{order_id}", (String) getArguments().get(KEY_ORDER_ID));

                        String trackingUrl;
                        Uri uri = Uri.parse(actionButton.getUri());
                        trackingUrl = uri.getQueryParameter("trackingUrl");

                        Uri.Builder uriBuilder = new Uri.Builder();
                        uriBuilder.appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_URL_LIVE_TRACKING, trackingUrl);
                        routingAppLink += uriBuilder.toString();
                        RouteManager.route(getContext(), routingAppLink);
                    } else {
                        RouteManager.route(getContext(), actionButton.getUri());
                    }
                } else {
                    RouteManager.route(getContext(), actionButton.getUri());
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CANCEL_ORDER) {
            if (resultCode == 1) {
                String reason = data.getStringExtra("reason");
                int reasonCode = data.getIntExtra("r_code", 1);
                presenter.updateOrderCancelReason(reason, (String) getArguments().get(KEY_ORDER_ID), reasonCode);
            }
        }
    }

    void openDialCaller(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPaymentData(PaymentData paymentData) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(paymentData.label());
        doubleTextView.setBottomText(paymentData.value());
        if (!paymentData.textColor().equals(""))
            doubleTextView.setBottomTextColor(Color.parseColor(paymentData.textColor()));
        doubleTextView.setBottomTextSize(16);
        doubleTextView.setBottomGravity(Gravity.RIGHT);
        totalPrice.addView(doubleTextView);
    }

    @Override
    public void setContactUs(final ContactUs contactUs) {
        String text = Html.fromHtml(contactUs.helpText()).toString();
        SpannableString spannableString = new SpannableString(text);
        int startIndexOfLink = text.indexOf("disini");
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(((UnifiedOrderListRouter) getActivity()
                            .getApplication()).getWebviewActivityWithIntent(getContext(),
                            URLEncoder.encode(contactUs.helpUrl(), ORDER_LIST_URL_ENCODING)));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(R.color.green_250)); // specific color for this link
            }
        }, startIndexOfLink, startIndexOfLink + "disini".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        helpLabel.setHighlightColor(Color.TRANSPARENT);
        helpLabel.setMovementMethod(LinkMovementMethod.getInstance());

        helpLabel.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void setTopActionButton(ActionButton actionButton) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_0), getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_24));
        primaryActionBtn.setText(actionButton.getLabel());
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.dp_4));
        if (!actionButton.getActionColor().getBackground().equals("")) {
            shape.setColor((Color.parseColor(actionButton.getActionColor().getBackground())));
        }
        if (!actionButton.getActionColor().getBorder().equals("")) {
            shape.setStroke(getResources().getDimensionPixelSize(R.dimen.dp_2), Color.parseColor(actionButton.getActionColor().getBorder()));
        }
        primaryActionBtn.setBackground(shape);
        if (isSingleButton) {
            primaryActionBtn.setLayoutParams(params);
        }
        if (!actionButton.getActionColor().getTextColor().equals("")) {
            primaryActionBtn.setTextColor(Color.parseColor(actionButton.getActionColor().getTextColor()));
        }
        if (!TextUtils.isEmpty(actionButton.getUri())) {
            primaryActionBtn.setOnClickListener(getActionButtonClickListener(actionButton.getUri()));
        }
    }

    @Override
    public void setBottomActionButton(ActionButton actionButton) {
        secondaryActionBtn.setText(actionButton.getLabel());
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.dp_4));
        if (!actionButton.getActionColor().getBackground().equals("")) {
            shape.setColor((Color.parseColor(actionButton.getActionColor().getBackground())));
        }
        if (!actionButton.getActionColor().getBorder().equals("")) {
            shape.setStroke(getResources().getDimensionPixelSize(R.dimen.dp_2), Color.parseColor(actionButton.getActionColor().getBorder()));
        }
        secondaryActionBtn.setBackground(shape);
        if (!actionButton.getActionColor().getTextColor().equals("")) {
            secondaryActionBtn.setTextColor(Color.parseColor(actionButton.getActionColor().getTextColor()));
        }
        if (!TextUtils.isEmpty(actionButton.getUri())) {
            secondaryActionBtn.setOnClickListener(getActionButtonClickListener(actionButton.getUri()));
        }
    }

    private View.OnClickListener getActionButtonClickListener(final String uri) {
        return view -> {
            if (uri != null && !uri.equals("")) {
                try {
                    startActivity(((UnifiedOrderListRouter) getActivity()
                            .getApplication()).getWebviewActivityWithIntent(getContext(),
                            URLEncoder.encode(uri, ORDER_LIST_URL_ENCODING)));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void setActionButtonsVisibility(int topBtnVisibility, int bottomBtnVisibility) {
        primaryActionBtn.setVisibility(topBtnVisibility);
        secondaryActionBtn.setVisibility(bottomBtnVisibility);
    }

    @Override
    public void setItems(List<Items> items) {
        productInformationTitle.setVisibility(View.VISIBLE);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemsRecyclerView.setAdapter(new ProductItemAdapter(getContext(), items, presenter));
    }

    @Override
    public Context getAppContext() {
        if (getActivity() != null)
            return getActivity().getApplicationContext();
        else
            return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void setMainViewVisible(int visibility) {
        mainView.setVisibility(visibility);
    }
}
