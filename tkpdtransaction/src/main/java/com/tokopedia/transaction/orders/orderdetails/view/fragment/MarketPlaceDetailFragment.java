package com.tokopedia.transaction.orders.orderdetails.view.fragment;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.UnifiedOrderListRouter;
import com.tokopedia.transaction.orders.common.view.DoubleTextView;
import com.tokopedia.transaction.orders.orderdetails.data.ActionButton;
import com.tokopedia.transaction.orders.orderdetails.data.AdditionalInfo;
import com.tokopedia.transaction.orders.orderdetails.data.AdditionalTickerInfo;
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
import com.tokopedia.transaction.orders.orderdetails.data.recommendationMPPojo.RecommendationResponse;
import com.tokopedia.transaction.orders.orderdetails.di.OrderDetailsComponent;
import com.tokopedia.transaction.orders.orderdetails.view.OrderListAnalytics;
import com.tokopedia.transaction.orders.orderdetails.view.activity.RequestCancelActivity;
import com.tokopedia.transaction.orders.orderdetails.view.activity.SeeInvoiceActivity;
import com.tokopedia.transaction.orders.orderdetails.view.adapter.ProductItemAdapter;
import com.tokopedia.transaction.orders.orderdetails.view.adapter.RecommendationMPAdapter;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailContract;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailPresenter;
import com.tokopedia.transaction.orders.orderlist.common.OrderListContants;
import com.tokopedia.transaction.orders.orderlist.data.ConditionalInfo;
import com.tokopedia.transaction.orders.orderlist.data.PaymentData;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.unifycomponents.ticker.TickerCallback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.inject.Inject;

import static android.content.Context.CLIPBOARD_SERVICE;

public class MarketPlaceDetailFragment extends BaseDaggerFragment implements RefreshHandler.OnRefreshHandlerListener, OrderListDetailContract.View {

    private static final int CREATE_RESCENTER_REQUEST_CODE = 789;

    public static final String KEY_ORDER_ID = "OrderId";
    public static final String ACTION_BUTTON_URL = "action_button_url";
    public static final String KEY_ORDER_CATEGORY = "OrderCategory";
    public static final String KEY_FROM_PAYMENT = "from_payment";
    public static final String ORDER_LIST_URL_ENCODING = "UTF-8";
    public static final String NO_SALIN = "No. Resi";
    public static final String NO_SANIN_NEXT_LINE = "\n\nSalin No. Resi";
    public static final String BELI_LAGI = "Beli Lagi";
    public static final String KEY_TULIS_REVIEW = "give_review";
    public static final String INVOICE_URL = "invoiceUrl";
    public static final String TX_ASK_SELLER = "tx_ask_seller";
    public static final String STATUS_CODE_220 = "220";
    public static final String STATUS_CODE_400 = "400";
    public static final String STATUS_CODE_11 = "11";
    private static final String CLICK_REQUEST_CANCEL = "click request cancel";
    private static final String CLICK_TRACK= "click track";
    private static final String CLICK_ASK_SELLER = "click ask seller";
    private static final String CLICK_ASK_SELLER_CANCELATION = "click ask seller - cancelation";
    private static final String CLICK_KEMBALI = "click kembali - cancelation";
    private static final String  CLICK_SUBMIT_CANCELATION = "click submit cancelation";
    private static final String CLICK_VIEW_COMPLAIN ="click view complain";
    private static final String TOTAL_SHIPPING_PRICE = "Total Ongkos Kirim";
    private static final String CLICK_LIHAT_PRODUK_SERUPA_LEVEL_ORDER = "click lihat produk serupa - order";

    public static final String SIMILAR_PRODUCTS_ACTION_BUTTON_KEY = "see_similar_products";

    public static final int REQUEST_CANCEL_ORDER = 101;
    public static final int REJECT_BUYER_REQUEST = 102;
    public static final int CANCEL_BUYER_REQUEST = 103;
    public static final int TEXT_SIZE_MEDIUM = 12;
    public static final int TEXT_SIZE_LARGE = 14;
    @Inject
    OrderListDetailPresenter presenter;
    private LinearLayout mainView;
    private LinearLayout viewRecomendItems;
    private TextView statusLabel;
    private TextView statusValue;
    private TextView conditionalInfoText;
    private TextView invoiceView;
    private TextView lihat;
    private TextView detailLabel;
    private TextView additionalText;
    private TextView infoLabel;
    private TextView helpLabel;
    private TextView recommendListTitle;
    private FrameLayout stickyButton;
    private LinearLayout statusDetail;
    private LinearLayout detailContent;
    private LinearLayout additionalInfoLayout;
    private LinearLayout infoValue;
    private LinearLayout totalPrice;
    private LinearLayout actionBtnLayout;
    private LinearLayout paymentMethod;
    private TextView primaryActionBtn;
    private TextView secondaryActionBtn;
    private FrameLayout parentLayout;
    private NestedScrollView nestedScrollView;
    private RecyclerView itemsRecyclerView;
    private TextView productInformationTitle;
    private boolean isSingleButton;
    private ClipboardManager myClipboard;
    private CardView driverLayout, dropShipperLayout;
    private TextView statusLihat;
    private FrameLayout progressBarLayout;
    private ClipData myClip;
    private SwipeToRefresh swipeToRefresh;
    private RefreshHandler refreshHandler;
    @Inject
    OrderListAnalytics orderListAnalytics;
    private ShopInfo shopInfo;
    private Status status;
    private Ticker mTickerInfos;
    private RecyclerView recommendationList;


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
        View view = inflater.inflate(R.layout.fragment_marketplace_order_list_detail, container, false);
        parentLayout = view.findViewById(R.id.parentLayout);
        mainView = view.findViewById(R.id.main_view);
        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
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
        mTickerInfos = view.findViewById(R.id.additional_ticker_info);
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
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        stickyButton = view.findViewById(R.id.sticky_btn);
        myClipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
        recommendationList = view.findViewById(R.id.recommendation_list);
        recommendListTitle = view.findViewById(R.id.recommend_title);
        viewRecomendItems = view.findViewById(R.id.recommend_items);
        setMainViewVisible(View.GONE);
        itemsRecyclerView.setNestedScrollingEnabled(false);
        setUpScrollChangeListener();
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshHandler = new RefreshHandler(getActivity(), getView().findViewById(R.id.swipe_refresh_layout), this);
        refreshHandler.startRefresh();
        refreshHandler.setPullEnabled(true);
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
                orderListAnalytics.sendLihatStatusClick(status.status());
                startActivity(((UnifiedOrderListRouter) getActivity().getApplication()).getOrderHistoryIntent(
                        getActivity(), getArguments().getString(KEY_ORDER_ID)
                ));
            }
        });
    }

    @Override
    public void setConditionalInfo(ConditionalInfo conditionalInfo) {

        conditionalInfoText.setVisibility(View.VISIBLE);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.dp_9));
        if (!TextUtils.isEmpty(conditionalInfo.color().background())) {
            shape.setColor(Color.parseColor(conditionalInfo.color().background()));
        }
        if (!TextUtils.isEmpty(conditionalInfo.color().border())) {
            shape.setStroke(getResources().getDimensionPixelOffset(R.dimen.dp_1), Color.parseColor(conditionalInfo.color().border()));
        }
        conditionalInfoText.setBackground(shape);
        conditionalInfoText.setText(conditionalInfo.text());
        if (!TextUtils.isEmpty(conditionalInfo.color().textColor())) {
            conditionalInfoText.setTextColor(Color.parseColor(conditionalInfo.color().textColor()));
        }

    }

    @Override
    public void setTitle(Title title) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(title.label());
        doubleTextView.setBottomText(title.value());
        doubleTextView.setBottomGravity(Gravity.RIGHT);
        if (!TextUtils.isEmpty(title.textColor())) {
            doubleTextView.setBottomTextColor(Color.parseColor(title.textColor()));
        }
        if (title.backgroundColor() != null && !title.backgroundColor().isEmpty()) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.background_deadline);
            doubleTextView.setBottomTextBackground(drawable);
            doubleTextView.setBottomTextRightPadding(getResources().getDimensionPixelSize(R.dimen.dp_20), getResources().getDimensionPixelSize(R.dimen.dp_10), getResources().getDimensionPixelSize(R.dimen.dp_20), getResources().getDimensionPixelSize(R.dimen.dp_10));

            doubleTextView.setBottomTextBackgroundColor(Color.parseColor(title.backgroundColor()));
        }
        statusDetail.addView(doubleTextView);
    }

    @Override
    public void setInvoice(final Invoice invoice) {
        invoiceView.setText(invoice.invoiceRefNum());
        if (!presenter.isValidUrl(invoice.invoiceUrl())) {
            lihat.setVisibility(View.GONE);
        }
        lihat.setOnClickListener(view -> {
            orderListAnalytics.sendViewInvoiceClickEvent();
            orderListAnalytics.sendLihatInvoiceClick(status.status());

            Intent intent = SeeInvoiceActivity.newInstance(getContext(), status, invoice,
                    getString(R.string.title_invoice));
            startActivity(intent);
        });
    }

    @Override
    public void setOrderToken(OrderToken orderToken) {

    }

    @Override
    public void setDetail(Detail detail) {
        detailLabel.setText(getContext().getResources().getString(R.string.detail_product));
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        if (!detail.label().equalsIgnoreCase(NO_SALIN)) {
            doubleTextView.setTopText(detail.label());
            doubleTextView.setTopTextColor(getContext().getResources().getColor(R.color.font_black_secondary_54));
            doubleTextView.setBottomText(detail.value());
            doubleTextView.setBottomTextColor(getContext().getResources().getColor(R.color.black_70_new));
            doubleTextView.setBottomTextStyle("bold");
            doubleTextView.setBottomTextSize(TEXT_SIZE_MEDIUM);
        } else {
            doubleTextView.setTopText(detail.label());
            String text = detail.value() + NO_SANIN_NEXT_LINE;
            SpannableString spannableString = new SpannableString(text);
            doubleTextView.setBottomTextColor(getContext().getResources().getColor(R.color.black_70_new));
            doubleTextView.setBottomTextSize(TEXT_SIZE_MEDIUM);
            int startIndexOfLink = text.indexOf("Salin");
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    try {
                        myClip = ClipData.newPlainText("text", detail.value());
                        myClipboard.setPrimaryClip(myClip);
                        ToasterNormal.showClose(getActivity(), getContext().getResources().getString(R.string.awb_number_copied));
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

        if (detail.label().equalsIgnoreCase("Nama Toko")) {
            doubleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String applink = ApplinkConst.SHOP_INFO.replace("{shop_id}", String.valueOf(shopInfo.getShopId()));
                    RouteManager.route(getContext(), applink);
                }
            });
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
    public void setAdditionalTickerInfo(List<AdditionalTickerInfo> tickerInfos, @Nullable String url) {
        if (getContext()!= null && tickerInfos.size() > 0) {
            mTickerInfos.setTickerTitle(tickerInfos.get(0).getTitle());
            mTickerInfos.setHtmlDescription(tickerInfos.get(0).getNotes());
            mTickerInfos.setVisibility(View.VISIBLE);
            if (url != null) {
                mTickerInfos.setDescriptionClickEvent(new TickerCallback() {
                    @Override
                    public void onDescriptionViewClick(CharSequence charSequence) {
                        RouteManager.route(getContext(),
                                String.format("%s?url=%s", ApplinkConst.WEBVIEW, url));
                    }

                    @Override
                    public void onDismiss() {

                    }
                });
            }
        }
    }

    @Override
    public void setPricing(Pricing pricing) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(pricing.label());
        doubleTextView.setTopTextColor(getContext().getResources().getColor(R.color.font_black_secondary_54));
        doubleTextView.setTopTextSize(TEXT_SIZE_MEDIUM);
        if (pricing.label().contains(TOTAL_SHIPPING_PRICE) && pricing.value().contains("Rp 0"))
            doubleTextView.setBottomText(getResources().getString(R.string.tkpdtransaction_bebas_ongkir));
        else
            doubleTextView.setBottomText(pricing.value());
        doubleTextView.setBottomTextColor(getContext().getResources().getColor(R.color.black_70));
        doubleTextView.setBottomTextSize(TEXT_SIZE_MEDIUM);
        doubleTextView.setBottomGravity(Gravity.RIGHT);
        infoValue.addView(doubleTextView);
    }

    @Override
    public void setPayMethodInfo(PayMethod payMethod) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(payMethod.getLabel());
        doubleTextView.setTopTextColor(getContext().getResources().getColor(R.color.font_black_secondary_54));
        doubleTextView.setBottomText(payMethod.getValue());
        doubleTextView.setBottomTextSize(TEXT_SIZE_MEDIUM);
        doubleTextView.setTopTextSize(TEXT_SIZE_MEDIUM);
        doubleTextView.setBottomTextColor(getContext().getResources().getColor(R.color.black_70));
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
        dropShipperName.setText(Html.fromHtml(dropShipper.getDropShipperName()));
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
                driverName.setText(Html.fromHtml(driverDetails.getDriverName()));
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
        if (!refreshHandler.isRefreshing()) {
            refreshHandler.setRefreshing(true);
            refreshHandler.setPullEnabled(false);
        }
        progressBarLayout.setVisibility(View.VISIBLE);
        swipeToRefresh.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
        progressBarLayout.setVisibility(View.GONE);
        swipeToRefresh.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSucessMessage(String message) {
        ToasterNormal.show(getActivity(), message);
    }

    @Override
    public void showSuccessMessageWithAction(String message) {
        Toaster.INSTANCE.showNormalWithAction(mainView, message, Snackbar.LENGTH_LONG, getString(R.string.bom_check_cart), v -> RouteManager.route(getContext(), ApplinkConst.CART));
    }

    @Override
    public void showErrorMessage(String message) {
        ToasterError.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void clearDynamicViews() {
        statusDetail.removeAllViews();
        detailContent.removeAllViews();
        additionalInfoLayout.removeAllViews();
        infoValue.removeAllViews();
        totalPrice.removeAllViews();
        actionBtnLayout.removeAllViews();
        paymentMethod.removeAllViews();
    }

    @Override
    public void askPermission() {

    }

    @Override
    public void setActionButtons(List<ActionButton> actionButtons) {
        actionBtnLayout.removeAllViews();
        actionBtnLayout.setOrientation(LinearLayout.VERTICAL);
        boolean stickyButtonAdded = false;
        for (ActionButton actionButton : actionButtons) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_0), getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_16));
            TextView textView = new TextView(getContext());
            textView.setText(actionButton.getLabel());
            textView.setPadding(getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_16));
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
            if (actionButton.getLabel().equalsIgnoreCase(BELI_LAGI)) {
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onBuyAgainAllItems(" - order");
                    }
                });
            } else {
                if (!TextUtils.isEmpty(actionButton.getUri())) {
                    textView.setOnClickListener(clickActionButton(actionButton));
                }

                if (actionButton.getKey().equalsIgnoreCase(KEY_TULIS_REVIEW)) {
                    orderListAnalytics.sendTulisReviewEventData(this.status.status());
                }
            }
            actionBtnLayout.addView(textView);
            if(!stickyButtonAdded){
                //Cant add the same textview as it has a parent already so making a new instance of the textview and adding it for the sticky
                TextView stickyTextView = new TextView(getContext());
                stickyTextView.setText(actionButton.getLabel());
                stickyTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_16));
                stickyTextView.setTypeface(Typeface.DEFAULT_BOLD);
                stickyTextView.setGravity(Gravity.CENTER);
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.dp_4));
                if (!actionButton.getActionColor().getBackground().equals("")) {
                    shape.setColor((Color.parseColor(actionButton.getActionColor().getBackground())));
                }
                if (!actionButton.getActionColor().getBorder().equals("")) {
                    shape.setStroke(getResources().getDimensionPixelSize(R.dimen.dp_2), Color.parseColor(actionButton.getActionColor().getBorder()));
                }
                LinearLayout.LayoutParams stickyButtonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                stickyTextView.setBackground(shape);
                stickyTextView.setLayoutParams(stickyButtonParams);
                if (!actionButton.getActionColor().getTextColor().equals("")) {
                    stickyTextView.setTextColor(Color.parseColor(actionButton.getActionColor().getTextColor()));
                }
                if (actionButton.getLabel().equalsIgnoreCase(BELI_LAGI)) {
                    stickyTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            presenter.onBuyAgainAllItems(" - order");
                        }
                    });
                } else {
                    if (!TextUtils.isEmpty(actionButton.getUri())) {
                        stickyTextView.setOnClickListener(clickActionButton(actionButton));
                    }
                }
                stickyButton.addView(stickyTextView);
                stickyButtonAdded = true;
            }
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
//        mainView.requestLayout();
        refreshHandler.startRefresh();
//        presenter.setOrderDetailsContent((String) getArguments().get(KEY_ORDER_ID), (String) getArguments().get(KEY_ORDER_CATEGORY), getArguments().getString(KEY_FROM_PAYMENT));
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
        }
        return view -> {
            if (actionButton.getActionButtonPopUp() != null && !TextUtils.isEmpty(actionButton.getActionButtonPopUp().getTitle())) {
                if (actionButton.getActionButtonPopUp().getActionButtonList().get(1).getLabel().equalsIgnoreCase("Tanya Penjual")) {
                    orderListAnalytics.sendActionButtonClickEvent(CLICK_REQUEST_CANCEL, this.status.status());
                } else if (actionButton.getActionButtonPopUp().getActionButtonList().get(1).getLabel().equalsIgnoreCase("Komplain")) {
                    orderListAnalytics.sendActionButtonClickEvent("click complain");
                } else if (actionButton.getActionButtonPopUp().getActionButtonList().get(1).getLabel().equalsIgnoreCase("selesai")) {
                    orderListAnalytics.sendActionButtonClickEvent("selesai");
                }
                final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE) {
                    @Override
                    public int layoutResId() {
                        if (actionButton.getActionButtonPopUp().getActionButtonList().get(1).getLabel().equalsIgnoreCase("Selesai")) {
                            return R.layout.dialog_seller_finish;
                        } else {
                            return super.layoutResId();
                        }
                    }
                };
                dialog.setTitle(actionButton.getActionButtonPopUp().getTitle());
                dialog.setDesc(actionButton.getActionButtonPopUp().getBody());
                if (actionButton.getActionButtonPopUp().getActionButtonList() != null && actionButton.getActionButtonPopUp().getActionButtonList().size() > 0) {
                    dialog.setBtnOk(actionButton.getActionButtonPopUp().getActionButtonList().get(1).getLabel());
                    dialog.setOnOkClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(actionButton.getActionButtonPopUp().getActionButtonList().get(1).getUri())) {
                                if (actionButton.getActionButtonPopUp().getActionButtonList().get(1).getLabel().equalsIgnoreCase("Selesai")) {
                                    presenter.finishOrder(getArguments().getString(KEY_ORDER_ID), actionButton.getUri());
                                    dialog.dismiss();
                                } else if (actionButton.getActionButtonPopUp().getActionButtonList().get(1).getLabel().equalsIgnoreCase("Komplain")) {
                                    Intent newIntent = InboxRouter.getCreateResCenterActivityIntent(getContext(),
                                            getArguments().getString(KEY_ORDER_ID));
                                    startActivityForResult(newIntent, CREATE_RESCENTER_REQUEST_CODE);
                                    dialog.dismiss();
                                } else if (actionButton.getActionButtonPopUp().getActionButtonList().get(1).getLabel().equalsIgnoreCase("Tanya penjual")) {
                                    orderListAnalytics.sendActionButtonClickEvent(CLICK_ASK_SELLER_CANCELATION, status.status());
                                } else if (actionButton.getActionButtonPopUp().getActionButtonList().get(0).getLabel().equalsIgnoreCase("Kembali")) {
                                    orderListAnalytics.sendActionButtonClickEvent(CLICK_KEMBALI, status.status());
                                } else {
                                    if (actionButton.getActionButtonPopUp().getActionButtonList().get(1).getUri().contains("askseller")) {
                                        startSellerAndAddInvoice(actionButton.getActionButtonPopUp().getActionButtonList().get(1).getUri());
                                    } else
                                        RouteManager.route(getContext(), actionButton.getActionButtonPopUp().getActionButtonList().get(1).getUri());
                                }
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
                    startSellerAndAddInvoice(actionButton.getUri());
                    orderListAnalytics.sendActionButtonClickEvent(CLICK_ASK_SELLER, statusValue.getText().toString());
                } else if (actionButton.getKey().contains("view_complaint")) {
                    orderListAnalytics.sendActionButtonClickEvent(CLICK_VIEW_COMPLAIN, statusValue.getText().toString());
                    RouteManager.route(getContext(), actionButton.getUri());
                } else if (actionButton.getKey().equalsIgnoreCase(SIMILAR_PRODUCTS_ACTION_BUTTON_KEY)) {
                    orderListAnalytics.sendActionButtonClickEvent(CLICK_LIHAT_PRODUK_SERUPA_LEVEL_ORDER, presenter.getFirstProductId());
                    RouteManager.route(getContext(), actionButton.getUri());
                } else if (!TextUtils.isEmpty(actionButton.getUri())) {
                    Intent intent = new Intent(getContext(), RequestCancelActivity.class);
                    intent.putExtra(KEY_ORDER_ID, getArguments().getString(KEY_ORDER_ID));
                    intent.putExtra(ACTION_BUTTON_URL, actionButton.getUri());
                    if (this.status.status().equals(STATUS_CODE_220) || this.status.status().equals(STATUS_CODE_400)) {
                        if (presenter.shouldShowTimeForCancellation()) {
                            Toaster.INSTANCE.showErrorWithAction(mainView,
                                    presenter.getCancelTime(),
                                    Snackbar.LENGTH_LONG,
                                    getResources().getString(R.string.title_ok), v -> {
                                    });
                        } else {
                            startActivityForResult(RequestCancelActivity.getInstance(getContext(), getArguments().getString(KEY_ORDER_ID), actionButton.getUri(), 1), REQUEST_CANCEL_ORDER);
                            orderListAnalytics.sendActionButtonClickEvent(CLICK_REQUEST_CANCEL, this.status.status());
                        }
                    } else if (this.status.status().equals(STATUS_CODE_11)) {
                        startActivityForResult(RequestCancelActivity.getInstance(getContext(), getArguments().getString(KEY_ORDER_ID), actionButton.getUri(), 0), REQUEST_CANCEL_ORDER);
                    } else if (actionButton.getLabel().equalsIgnoreCase("Lacak")) {

                        orderListAnalytics.sendActionButtonClickEvent(CLICK_TRACK);
                        String routingAppLink;
                        routingAppLink = ApplinkConst.ORDER_TRACKING.replace("{order_id}", getArguments().getString(KEY_ORDER_ID));

                        String trackingUrl;
                        Uri uri = Uri.parse(actionButton.getUri());
                        trackingUrl = uri.getQueryParameter("url");

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

    private void startSellerAndAddInvoice(String uriString) {
        if (shopInfo != null) {
            String shopId = String.valueOf(this.shopInfo.getShopId());
            String shopName = this.shopInfo.getShopName();
            String shopLogo = this.shopInfo.getShopLogo();
            String shopUrl = this.shopInfo.getShopUrl();
            String invoiceUrl;
            Uri uri = Uri.parse(uriString);
            invoiceUrl = uri.getQueryParameter(INVOICE_URL);
            String applink = "tokopedia://topchat/askseller/" + shopId;
            Intent intent = RouteManager.getIntent(getContext(), applink);
            presenter.assignInvoiceDataTo(intent);
            intent.putExtra(ApplinkConst.Chat.SOURCE, TX_ASK_SELLER);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CANCEL_ORDER) {
            String reason = "";
            int reasonCode = 1;
            if (resultCode == REJECT_BUYER_REQUEST) {
                reason = data.getStringExtra(OrderListContants.REASON);
                reasonCode = data.getIntExtra(OrderListContants.REASON_CODE, 1);
                presenter.updateOrderCancelReason(reason, getArguments().getString(KEY_ORDER_ID), reasonCode, data.getStringExtra(ACTION_BUTTON_URL));
            } else if (resultCode == CANCEL_BUYER_REQUEST) {
                reason = data.getStringExtra(OrderListContants.REASON);
                reasonCode = data.getIntExtra(OrderListContants.REASON_CODE, 1);
                presenter.updateOrderCancelReason(reason, getArguments().getString(KEY_ORDER_ID), reasonCode, data.getStringExtra(ACTION_BUTTON_URL));
            }
            orderListAnalytics.sendActionButtonClickEvent(CLICK_SUBMIT_CANCELATION, statusValue.getText().toString() + "-" + reason);

//            presenter.setOrderDetailsContent((String) getArguments().get(KEY_ORDER_ID), (String) getArguments().get(KEY_ORDER_CATEGORY), getArguments().getString(KEY_FROM_PAYMENT));
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
        doubleTextView.setTopTextColor(getContext().getResources().getColor(R.color.black_70_new));
        doubleTextView.setTopTextSize(TEXT_SIZE_LARGE);
        doubleTextView.setTopTextStyle("bold");
        doubleTextView.setBottomText(paymentData.value());
        if (!paymentData.textColor().equals(""))
            doubleTextView.setBottomTextColor(Color.parseColor(paymentData.textColor()));
        doubleTextView.setBottomTextSize(TEXT_SIZE_LARGE);
        doubleTextView.setBottomTextColor(getContext().getResources().getColor(R.color.orange));
        doubleTextView.setBottomGravity(Gravity.RIGHT);
        totalPrice.addView(doubleTextView);
    }

    @Override
    public void setContactUs(final ContactUs contactUs, String helpLink) {
        String text = getResources().getString(R.string.contact_us_text);
        SpannableString spannableString = new SpannableString(text);
        int startIndexOfLink = text.indexOf("disini");
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                try {
                    orderListAnalytics.sendHelpEventData(status.status());
                    startActivity(((UnifiedOrderListRouter) getActivity()
                            .getApplication()).getWebviewActivityWithIntent(getContext(),
                            URLEncoder.encode(helpLink, ORDER_LIST_URL_ENCODING)));
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
    public void setItems(List<Items> items, boolean isTradeIn) {
        productInformationTitle.setVisibility(View.VISIBLE);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemsRecyclerView.setAdapter(new ProductItemAdapter(getContext(), items, presenter, isTradeIn, status));
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
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void setMainViewVisible(int visibility) {
        swipeToRefresh.setVisibility(visibility);
    }

    @Override
    public void onRefresh(View view) {
        presenter.setOrderDetailsContent((String) getArguments().get(KEY_ORDER_ID), (String) getArguments().get(KEY_ORDER_CATEGORY), getArguments().getString(KEY_FROM_PAYMENT), null);

    }

    private void setUpScrollChangeListener() {
        Rect scrollBounds = new Rect();
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, scrollX, scrollY, scrollOldX, scrollOldY) -> {
            nestedScrollView.getHitRect(scrollBounds);
            if (actionBtnLayout.getLocalVisibleRect(scrollBounds)) {
                // Any portion of the sticky button, even a single pixel, is within the visible window
                stickyButton.setVisibility(View.GONE);
            } else {
                // NONE of the sticky button is within the visible window
                stickyButton.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void setRecommendation(Object recommendationResponse) {
        RecommendationResponse rechargeWidgetResponse = (RecommendationResponse) recommendationResponse;
        if (rechargeWidgetResponse != null && rechargeWidgetResponse.getRechargeFavoriteRecommendationList() != null) {
            if (!rechargeWidgetResponse.getRechargeFavoriteRecommendationList().getRecommendations().isEmpty() && getContext() != null) {
                viewRecomendItems.setVisibility(View.VISIBLE);
                recommendListTitle.setText(rechargeWidgetResponse.getRechargeFavoriteRecommendationList().getTitle());
                recommendationList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                recommendationList.setAdapter(new RecommendationMPAdapter(rechargeWidgetResponse.getRechargeFavoriteRecommendationList().getRecommendations()));
            }
        }
    }
}
