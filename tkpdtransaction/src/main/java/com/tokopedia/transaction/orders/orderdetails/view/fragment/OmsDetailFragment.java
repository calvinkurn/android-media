package com.tokopedia.transaction.orders.orderdetails.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.applink.RouteManager;
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
import com.tokopedia.transaction.orders.orderdetails.view.adapter.ItemsAdapter;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailContract;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailPresenter;
import com.tokopedia.transaction.orders.orderlist.data.ConditionalInfo;
import com.tokopedia.transaction.orders.orderlist.data.PaymentData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.transaction.orders.orderdetails.view.fragment.OrderListDetailFragment.ORDER_LIST_URL_ENCODING;

/**
 * Created by baghira on 09/05/18.
 */

public class OmsDetailFragment extends BaseDaggerFragment implements OrderListDetailContract.View {

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
    public void setItems(List<Items> items) {
        List<Items> itemsList = new ArrayList<>();
        for (Items item : items) {
            if (!CATEGORY_GIFT_CARD.equalsIgnoreCase(item.getCategory())) {
                itemsList.add(item);
            }
        }
        if (itemsList.size() > 0) {
            recyclerView.setAdapter(new ItemsAdapter(getContext(), items, false, presenter));
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
        super.onDestroyView();
        presenter.detachView();
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
    public void showErrorMessage (String message) {

    }

    @Override
    public void clearDynamicViews() {

    }

    @Override
    public void setMainViewVisible(int visibility) {
        mainView.setVisibility(visibility);
    }

}
