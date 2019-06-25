package com.tokopedia.transaction.orders.orderdetails.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.UnifiedOrderListRouter;
import com.tokopedia.transaction.orders.common.view.DoubleTextView;
import com.tokopedia.transaction.orders.orderdetails.data.ActionButton;
import com.tokopedia.transaction.orders.orderdetails.data.AdditionalInfo;
import com.tokopedia.transaction.orders.orderdetails.data.ContactUs;
import com.tokopedia.transaction.orders.orderdetails.data.Detail;
import com.tokopedia.transaction.orders.orderdetails.data.DriverDetails;
import com.tokopedia.transaction.orders.orderdetails.data.DropShipper;
import com.tokopedia.transaction.orders.orderdetails.data.EntityPessenger;
import com.tokopedia.transaction.orders.orderdetails.data.Invoice;
import com.tokopedia.transaction.orders.orderdetails.data.Items;
import com.tokopedia.transaction.orders.orderdetails.data.MetaDataInfo;
import com.tokopedia.transaction.orders.orderdetails.data.OrderToken;
import com.tokopedia.transaction.orders.orderdetails.data.PayMethod;
import com.tokopedia.transaction.orders.orderdetails.data.Pricing;
import com.tokopedia.transaction.orders.orderdetails.data.ShopInfo;
import com.tokopedia.transaction.orders.orderdetails.data.Status;
import com.tokopedia.transaction.orders.orderdetails.data.Title;
import com.tokopedia.transaction.orders.orderdetails.di.OrderDetailsComponent;
import com.tokopedia.transaction.orders.orderdetails.view.adapter.ItemsAdapter;
import com.tokopedia.transaction.orders.orderdetails.view.customview.BookingCodeView;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailContract;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailPresenter;
import com.tokopedia.transaction.orders.orderlist.data.ConditionalInfo;
import com.tokopedia.transaction.orders.orderlist.data.PaymentData;
import com.tokopedia.unifycomponents.Toaster;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.tokopedia.transaction.orders.orderdetails.view.fragment.OrderListDetailFragment.ORDER_LIST_URL_ENCODING;

/**
 * Created by baghira on 09/05/18.
 */

public class OmsDetailFragment extends BaseDaggerFragment implements OrderListDetailContract.View, ItemsAdapter.SetEventDetails {

    public static final String KEY_BUTTON = "button";
    public static final String KEY_TEXT = "text";
    public static final String KEY_REDIRECT = "redirect";
    public static final String KEY_ORDER_ID = "OrderId";
    public static final String KEY_ORDER_CATEGORY = "OrderCategory";
    private static final String KEY_FROM_PAYMENT = "from_payment";
    private static final String KEY_URI = "tokopedia";
    private static final String KEY_URI_PARAMETER = "idem_potency_key";
    private static final String KEY_URI_PARAMETER_EQUAL = "idem_potency_key=";
    public static final String CATEGORY_GIFT_CARD = "Gift-card";

    @Inject
    OrderListDetailPresenter presenter;

    OrderDetailsComponent orderListComponent;
    private LinearLayout mainView;
    private TextView statusLabel;
    private TextView statusValue;
    private TextView conditionalInfoText;
    private LinearLayout statusDetail;
    private TextView invoiceView;
    private TextView lihat;
    private TextView detailLabel;
    private LinearLayout detailsLayout;
    private TextView infoLabel;
    private LinearLayout infoValue;
    private LinearLayout totalPrice;
    private TextView helpLabel;
    private TextView primaryActionBtn;
    private TextView secondaryActionBtn;
    private RecyclerView recyclerView;
    LinearLayout paymentMethodInfo;
    FrameLayout progressBarLayout;
    private boolean isSingleButton;
    private PermissionCheckerHelper permissionCheckerHelper;
    RelativeLayout actionButtonLayout;
    TextView actionButtonText;
    LinearLayout userInfo;
    TextView userInfoLabel;
    private String categoryName;
    View dividerUserInfo, dividerActionBtn;


    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(OrderDetailsComponent.class).inject(this);
    }

    public static Fragment getInstance(String orderId, String orderCategory, String fromPayment) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ORDER_ID, orderId);
        bundle.putString(KEY_ORDER_CATEGORY, orderCategory);
        bundle.putString(KEY_FROM_PAYMENT, fromPayment);
        Fragment fragment = new OmsDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oms_list_detail, container, false);
        mainView = view.findViewById(R.id.main_view);
        statusLabel = view.findViewById(R.id.status_label);
        statusValue = view.findViewById(R.id.status_value);
        conditionalInfoText = view.findViewById(R.id.conditional_info);
        statusDetail = view.findViewById(R.id.status_detail);
        invoiceView = view.findViewById(R.id.invoice);
        lihat = view.findViewById(R.id.lihat);
        detailLabel = view.findViewById(R.id.detail_label);
        detailsLayout = view.findViewById(R.id.details_section);
        infoLabel = view.findViewById(R.id.info_label);
        infoValue = view.findViewById(R.id.info_value);
        totalPrice = view.findViewById(R.id.total_price);
        helpLabel = view.findViewById(R.id.help_label);
        primaryActionBtn = view.findViewById(R.id.langannan);
        secondaryActionBtn = view.findViewById(R.id.beli_lagi);
        recyclerView = view.findViewById(R.id.recycler_view);
        paymentMethodInfo = view.findViewById(R.id.info_payment);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        actionButtonLayout = view.findViewById(R.id.actionButton);
        userInfo = view.findViewById(R.id.user_information_layout);
        userInfoLabel = view.findViewById(R.id.user_label);
        dividerUserInfo = view.findViewById(R.id.divider_above_userInfo);
        dividerActionBtn = view.findViewById(R.id.divider_above_actionButton);
        actionButtonText = view.findViewById(R.id.actionButton_text);
        recyclerView.setNestedScrollingEnabled(false);


        initInjector();
        setMainViewVisible(View.GONE);

        presenter.attachView(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.setOrderDetailsContent((String) getArguments().get(KEY_ORDER_ID), (String) getArguments().get(KEY_ORDER_CATEGORY), getArguments().getString("from_payment"));
    }

    @Override
    public void setStatus(Status status) {
        statusLabel.setText(status.statusLabel());
        statusValue.setText(status.statusText());
        if (!status.textColor().equals(""))
            statusValue.setTextColor(Color.parseColor(status.textColor()));
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(4);
        if (!status.backgroundColor().equals("")) {
            shape.setColor(Color.parseColor(status.backgroundColor()));
        }
        statusValue.setBackground(shape);
    }

    @Override
    public void setConditionalInfo(ConditionalInfo conditionalInfo) {

        conditionalInfoText.setVisibility(View.VISIBLE);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(9);
        if (!TextUtils.isEmpty(conditionalInfo.color().background())) {
            shape.setColor(Color.parseColor(conditionalInfo.color().background()));
        }
        if (!TextUtils.isEmpty(conditionalInfo.color().border())) {
            shape.setStroke(getResources().getDimensionPixelOffset(R.dimen.dp_1), Color.parseColor(conditionalInfo.color().border()));
        }
        conditionalInfoText.setBackground(shape);
        conditionalInfoText.setPadding(getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_16), getResources().getDimensionPixelSize(R.dimen.dp_16));
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
        statusDetail.addView(doubleTextView);
    }

    @Override
    public void setInvoice(final Invoice invoice) {
        invoiceView.setText(invoice.invoiceRefNum());
        if (invoice.invoiceUrl().equals("")) {
            lihat.setVisibility(View.GONE);
        }
        lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(((UnifiedOrderListRouter) getActivity()
                        .getApplication()).getWebviewActivityWithIntent(getContext(),
                        invoice.invoiceUrl()));
            }
        });
    }

    @Override
    public void setOrderToken(OrderToken orderToken) {

    }

    @Override
    public void setDetail(Detail detail) {

    }

    @Override
    public void setAdditionInfoVisibility(int visibility) {
    }

    @Override
    public void setAdditionalInfo(AdditionalInfo additionalInfo) {

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
    public void setPaymentData(PaymentData paymentData) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(paymentData.label());
        doubleTextView.setBottomText(paymentData.value());
        if (!paymentData.textColor().equals("")) {
            doubleTextView.setBottomTextColor(Color.parseColor(paymentData.textColor()));
        }
        doubleTextView.setBottomTextSize(16);
        doubleTextView.setBottomGravity(Gravity.RIGHT);
        totalPrice.addView(doubleTextView);
    }

    @Override
    public void setContactUs(final ContactUs contactUs) {
        String text = getResources().getString(R.string.contact_us_text);
        SpannableString spannableString = new SpannableString(text);
        int startIndexOfLink = text.indexOf(getResources().getString(R.string.help_text));
        if (startIndexOfLink != -1) {
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    try {
                        startActivity(((UnifiedOrderListRouter) getActivity().getApplication())
                                .getWebviewActivityWithIntent(getContext(), URLEncoder.encode(
                                        getResources().getString(R.string.contact_us_applink), ORDER_LIST_URL_ENCODING)));
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
            }, startIndexOfLink, startIndexOfLink + getResources().getString(R.string.help_text).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            helpLabel.setHighlightColor(Color.TRANSPARENT);
            helpLabel.setMovementMethod(LinkMovementMethod.getInstance());

            helpLabel.setText(spannableString, TextView.BufferType.SPANNABLE);
        }
    }

    @Override
    public void setTopActionButton(ActionButton actionButton) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getResources().getDimensionPixelSize(R.dimen.dp_0), getResources().getDimensionPixelSize(R.dimen.dp_0), getResources().getDimensionPixelSize(R.dimen.dp_0), getResources().getDimensionPixelSize(R.dimen.dp_24));
        primaryActionBtn.setText(actionButton.getLabel());
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(4);
        shape.setColor(getResources().getColor(R.color.white));
        shape.setStroke(2, getResources().getColor(R.color.grey_300));
        primaryActionBtn.setBackground(shape);
        if (isSingleButton) {
            primaryActionBtn.setLayoutParams(params);
        }
        if (!TextUtils.isEmpty(actionButton.getBody().getAppURL())) {
            primaryActionBtn.setOnClickListener(getActionButtonClickListener(actionButton.getBody().getAppURL()));
        }
    }

    @Override
    public void setBottomActionButton(ActionButton actionButton) {
        secondaryActionBtn.setText(actionButton.getLabel());
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(4);
        shape.setColor(getResources().getColor(R.color.deep_orange_500));
        secondaryActionBtn.setBackground(shape);
        secondaryActionBtn.setTextColor(getResources().getColor(R.color.white));
        if (!TextUtils.isEmpty(actionButton.getBody().getAppURL())) {
            secondaryActionBtn.setOnClickListener(getActionButtonClickListener(actionButton.getBody().getAppURL()));
        }
    }

    private View.OnClickListener getActionButtonClickListener(final String uri) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUri = uri;
                if (uri.startsWith(KEY_URI)) {
                    if (newUri.contains(KEY_URI_PARAMETER)) {
                        Uri url = Uri.parse(newUri);
                        newUri = newUri.replace(url.getQueryParameter(KEY_URI_PARAMETER), "");
                        newUri = newUri.replace(KEY_URI_PARAMETER_EQUAL, "");
                    }
                    RouteManager.route(getActivity(), newUri);
                } else if (uri != null && !uri.equals("")) {
                    try {
                        startActivity(((UnifiedOrderListRouter) getActivity().getApplication())
                                .getWebviewActivityWithIntent(getContext(), URLEncoder.encode(
                                        uri, ORDER_LIST_URL_ENCODING)));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
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
        List<Items> itemsList = new ArrayList<>();
        for (Items item : items) {
            if (!CATEGORY_GIFT_CARD.equalsIgnoreCase(item.getCategory())) {
                itemsList.add(item);
            }
        }
        if (itemsList.size() > 0) {
            recyclerView.setAdapter(new ItemsAdapter(getContext(), items, false, presenter, OmsDetailFragment.this, getArguments().getString(KEY_ORDER_ID)));
        } else {
            detailsLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public Context getAppContext() {
        if (getActivity() != null)
            return getActivity().getApplicationContext();
        else
            return null;
    }

    @Override
    public void setPayMethodInfo(PayMethod payMethod) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(payMethod.getLabel());
        doubleTextView.setBottomText(payMethod.getValue());
        doubleTextView.setBottomGravity(Gravity.RIGHT);
        paymentMethodInfo.addView(doubleTextView);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void setButtonMargin() {
        isSingleButton = true;
    }

    @Override
    public void showDropshipperInfo(DropShipper dropShipper) {

    }

    @Override
    public void showDriverInfo(DriverDetails driverDetails) {

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
        actionButtonLayout.setVisibility(View.VISIBLE);
        actionButtonText.setText(actionButtons.get(0).getLabel());
        actionButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionButtons.get(0).getControl().equalsIgnoreCase(KEY_BUTTON)) {
                    presenter.setActionButton(actionButtons, null, 0, false);
                } else if (actionButtons.get(0).getControl().equalsIgnoreCase(KEY_REDIRECT)){
                    RouteManager.route(getContext(), actionButtons.get(0).getBody().getAppURL());
                }
            }
        });
    }

    @Override
    public void setShopInfo(ShopInfo shopInfo) {

    }

    @Override
    public void showReplacementView(List<String> reasons) {

    }

    @Override
    public void finishOrderDetail() {

    }

    @Override
    public void showSucessMessage(String message) {
        Toast.makeText(getAppContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage(String message) {

    }

    @Override
    public void clearDynamicViews() {

    }

    @Override
    public void askPermission() {
        permissionCheckerHelper = new PermissionCheckerHelper();
        permissionCheckerHelper.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCheckerHelper.PermissionCheckListener() {
            @Override
            public void onPermissionDenied(@NotNull String permissionText) {

            }

            @Override
            public void onNeverAskAgain(@NotNull String permissionText) {

            }

            @Override
            public void onPermissionGranted() {
                presenter.permissionGrantedContinueDownload();
            }
        }, "");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(getAppContext(), requestCode, permissions, grantResults);
        }
    }

    @Override
    public void setMainViewVisible(int visibility) {
        mainView.setVisibility(visibility);
    }

    @Override
    public void setEventDetails(ActionButton actionButton, Items item) {

        MetaDataInfo metaDataInfo = new Gson().fromJson(item.getMetaData(), MetaDataInfo.class);
        if (item.getActionButtons() == null || item.getActionButtons().size() == 0) {
            actionButtonLayout.setVisibility(View.GONE);
            dividerActionBtn.setVisibility(View.GONE);
        } else {
            dividerActionBtn.setVisibility(View.VISIBLE);
            actionButtonLayout.setVisibility(View.VISIBLE);
            actionButtonText.setText(actionButton.getLabel());
            actionButtonLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (actionButton.getControl().equalsIgnoreCase(KEY_BUTTON)) {
                        if (!TextUtils.isEmpty(item.getCategory()) && "Deal".equalsIgnoreCase(item.getCategory())) {
                            Toaster.Companion.showNormalWithAction((Activity) getContext(), String.format("%s %s", "Berhasil! Silakan cek voucher di", metaDataInfo.getEntityaddress().getEmail()), Snackbar.LENGTH_LONG, "Ok", v1 -> {
                            });
                        } else {
                            Toaster.Companion.showNormalWithAction((Activity) getContext(), String.format("%s %s", "Berhasil! Silakan cek e-tiket di", metaDataInfo.getEntityaddress().getEmail()), Snackbar.LENGTH_LONG, "Ok", v1 -> {
                            });
                        }
                        presenter.setActionButton(item.getActionButtons(), null, 0, false);
                    } else if (actionButton.getControl().equalsIgnoreCase(KEY_REDIRECT)){
                        RouteManager.route(getContext(), actionButton.getBody().getAppURL());
                    }
                }
            });

        }

        if (metaDataInfo != null && metaDataInfo.getEntityPessengers() != null && metaDataInfo.getEntityPessengers().size() > 0) {
            userInfoLabel.setVisibility(View.VISIBLE);
            userInfo.setVisibility(View.VISIBLE);
            dividerUserInfo.setVisibility(View.VISIBLE);
            userInfo.removeAllViews();
            for (EntityPessenger entityPessenger: metaDataInfo.getEntityPessengers()) {
                DoubleTextView doubleTextView = new DoubleTextView(getContext(), LinearLayout.VERTICAL);
                doubleTextView.setTopText(entityPessenger.getTitle());
                doubleTextView.setTopTextColor(ContextCompat.getColor(getContext(), R.color.clr_ae31353b));
                doubleTextView.setBottomText(entityPessenger.getValue());
                doubleTextView.setBottomTextColor(ContextCompat.getColor(getContext(), R.color.clr_f531353b));
                doubleTextView.setBottomTextStyle("bold");

                userInfo.addView(doubleTextView);
            }
        } else {
            userInfoLabel.setVisibility(View.GONE);
            userInfo.setVisibility(View.GONE);
            dividerUserInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public void openShowQRFragment(ActionButton actionButton, Items item) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.scan_qr_code_layout, mainView, false);
        Dialog dialog = new Dialog(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        View v = dialog.getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);


        ImageView qrCode = view.findViewById(R.id.qrCode);
        LinearLayout voucherCodeLayout = view.findViewById(R.id.booking_code_layout);
        TextView closeButton = view.findViewById(R.id.redeem_ticket);

        ImageHandler.loadImage(getContext(), qrCode, actionButton.getBody().getAppURL(), R.color.grey_1100, R.color.grey_1100);

        if (!TextUtils.isEmpty(item.getTrackingNumber())) {
            String[] voucherCodes = item.getTrackingNumber().split(",");
            for (int i = 0; i < voucherCodes.length; i++) {
                voucherCodeLayout.setVisibility(View.VISIBLE);
                BookingCodeView bookingCodeView = new BookingCodeView(getContext(), voucherCodes[i], i, getContext().getResources().getString(R.string.voucher_code_title), voucherCodes.length);
                bookingCodeView.setBackground(getContext().getResources().getDrawable(R.drawable.bg_search_input_text_area));
                voucherCodeLayout.addView(bookingCodeView);
            }
        }

        closeButton.setOnClickListener(v1->{
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void setDetailTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            detailLabel.setText(title);
        }
    }
}
