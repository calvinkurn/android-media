package com.tokopedia.transaction.orders.orderdetails.view.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.orders.orderdetails.data.ActionButton;
import com.tokopedia.transaction.orders.orderdetails.data.AdditionalInfo;
import com.tokopedia.transaction.orders.orderdetails.data.ContactUs;
import com.tokopedia.transaction.orders.orderdetails.data.Detail;
import com.tokopedia.transaction.orders.orderdetails.data.Invoice;
import com.tokopedia.transaction.orders.orderdetails.data.OrderToken;
import com.tokopedia.transaction.orders.orderdetails.data.Pricing;
import com.tokopedia.transaction.orders.orderdetails.data.Status;
import com.tokopedia.transaction.orders.orderdetails.data.Title;
import com.tokopedia.transaction.orders.orderdetails.di.OrderDetailsComponent;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailContract;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailPresenter;
import com.tokopedia.transaction.orders.orderlist.data.ConditionalInfo;
import com.tokopedia.transaction.orders.orderlist.data.PaymentData;

import javax.inject.Inject;

import com.tokopedia.transaction.orders.orderdetails.di.DaggerOrderDetailsComponent;
import com.tokopedia.transaction.orders.common.view.DoubleTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by baghira on 09/05/18.
 */

public class OrderListDetailFragment extends BaseDaggerFragment implements OrderListDetailContract.View {

    public static final String KEY_ORDER_ID = "OrderId";
    @Inject
    OrderListDetailPresenter presenter;
    OrderDetailsComponent orderListComponent;

    @BindView(R2.id.main_view)
    LinearLayout mainView;
    @BindView(R2.id.status_label)
    TextView statusLabel;
    @BindView(R2.id.status_value)
    TextView statusValue;
    @BindView(R2.id.conditional_info)
    TextView conditionalInfoText;
    @BindView(R2.id.status_detail)
    LinearLayout statusDetail;
    @BindView(R2.id.invoice)
    TextView invoiceView;
    @BindView(R2.id.lihat)
    TextView lihat;
    @BindView(R2.id.detail_label)
    TextView detailLabel;
    @BindView(R2.id.detail_content)
    LinearLayout detailContent;
    @BindView(R2.id.additional)
    TextView additionalText;
    @BindView(R2.id.additional_info)
    LinearLayout additionalInfoLayout;
    @BindView(R2.id.info_label)
    TextView infoLabel;
    @BindView(R2.id.info_value)
    LinearLayout infoValue;
    @BindView(R2.id.total_price)
    LinearLayout totalPrice;
    @BindView(R2.id.help_label)
    TextView helpLabel;
    @BindView(R2.id.langannan)
    TextView langannan;
    @BindView(R2.id.beli_lagi)
    TextView beliLagi;


    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        orderListComponent = DaggerOrderDetailsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        orderListComponent.inject(this);
    }

    public static Fragment getInstance(String orderId) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ORDER_ID, orderId);
        Fragment fragment = new OrderListDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list_detail, container, false);
        initInjector();
        ButterKnife.bind(this, view);
        setMainViewVisible(View.GONE);
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.setOrderDetailsContent((String) getArguments().get(KEY_ORDER_ID));
    }

    @Override
    public void setStatus(Status status) {
        statusLabel.setText(status.statusLabel());
        statusValue.setText(status.statusText());
        statusValue.setTextColor(Color.parseColor(status.textColor()));
    }

    @Override
    public void setConditionalInfo(ConditionalInfo conditionalInfo) {

        conditionalInfoText.setVisibility(View.VISIBLE);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(9);
        shape.setColor(android.graphics.Color.parseColor(conditionalInfo.color().background()));
        shape.setStroke(1, android.graphics.Color.parseColor(conditionalInfo.color().border()));
        conditionalInfoText.setBackground(shape);
        conditionalInfoText.setPadding(16, 16, 16, 16);
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
        lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransactionPurchaseRouter.startWebViewActivity(getContext(), invoice.invoiceUrl());
            }
        });
    }

    @Override
    public void setOrderToken(OrderToken orderToken) {

    }

    @Override
    public void setDetail(Detail detail) {
        //detailLabel.setText(detail.label());
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(detail.label());
        doubleTextView.setBottomText(detail.value());
        detailContent.addView(doubleTextView);
    }

    @Override
    public void setAdditionInfoVisibility(int visibility) {
        additionalText.setVisibility(visibility);
        additionalText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additionalText.setOnClickListener(null);
                additionalText.setText(getResources().getString(R.string.additional_text));
                additionalText.setTypeface(Typeface.DEFAULT_BOLD);
                additionalText.setTextColor(getResources().getColor(R.color.black_70));
                additionalInfoLayout.setVisibility(View.VISIBLE);
            }
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
        infoValue.addView(doubleTextView);

    }

    @Override
    public void setPaymentData(PaymentData paymentData) {
        DoubleTextView doubleTextView = new DoubleTextView(getActivity(), LinearLayout.HORIZONTAL);
        doubleTextView.setTopText(paymentData.label());
        doubleTextView.setBottomText(paymentData.value());
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
                TransactionPurchaseRouter.startWebViewActivity(getContext(), contactUs.helpUrl());
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
        langannan.setText(actionButton.label());
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(4);
        shape.setColor(getResources().getColor(R.color.white));
        shape.setStroke(2, getResources().getColor(R.color.grey_300));
        langannan.setBackground(shape);
        langannan.setOnClickListener(getActionButtonClickListener(actionButton.uri()));
    }

    @Override
    public void setBottomActionButton(ActionButton actionButton) {
        beliLagi.setText(actionButton.label());
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(4);
        shape.setColor(getResources().getColor(R.color.deep_orange_500));
        beliLagi.setBackground(shape);
        beliLagi.setTextColor(getResources().getColor(R.color.white));
        beliLagi.setOnClickListener(getActionButtonClickListener(actionButton.uri()));
    }

    private View.OnClickListener getActionButtonClickListener(final String uri) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUri = uri;
                if (uri.startsWith("tokopedia")) {
                    Uri url = Uri.parse(newUri);
                    newUri = newUri.replace(url.getQueryParameter("idem_potency_key"), "");
                    newUri = newUri.replace("idem_potency_key=", "");
                    RouteManager.route(getActivity(), newUri);
                } else {
                    TransactionPurchaseRouter.startWebViewActivity(getActivity(), uri);
                }
            }
        };
    }

    @Override
    public void setActionButtonsVisibility(int topBtnVisibility, int bottomBtnVisibility) {
        langannan.setVisibility(topBtnVisibility);
        beliLagi.setVisibility(bottomBtnVisibility);
    }

    @Override
    public void setMainViewVisible(int visibility) {
        mainView.setVisibility(visibility);
    }

}
