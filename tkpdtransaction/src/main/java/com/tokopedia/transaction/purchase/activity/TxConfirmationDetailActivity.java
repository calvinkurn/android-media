package com.tokopedia.transaction.purchase.activity;

import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.TokopediaBankAccount;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.purchase.listener.TxConfDetailViewListener;
import com.tokopedia.transaction.purchase.model.response.txconfirmation.TxConfData;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderData;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderProduct;
import com.tokopedia.transaction.purchase.presenter.TxConfDetailPresenter;
import com.tokopedia.transaction.purchase.presenter.TxConfDetailPresenterImpl;

import java.text.MessageFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author by Angga.Prasetiyo on 15/06/2016.
 */
public class TxConfirmationDetailActivity extends BasePresenterActivity<TxConfDetailPresenter>
        implements TxConfDetailViewListener {
    private static final String EXTRA_TX_CONFIRMATION_DATA = "EXTRA_TX_CONFIRMATION_DATA";

    public static final int REQUEST_CONFIRMATION = 1;

    @BindView(R2.id.total_tx)
    TextView tvTotalTx;
    @BindView(R2.id.tx_date)
    TextView tvDateTx;
    @BindView(R2.id.due_date)
    TextView tvDueDateTx;
    @BindView(R2.id.total_item)
    TextView tvTotalItem;
    @BindView(R2.id.total_item_price)
    TextView tvTotalItemPrice;
    @BindView(R2.id.deposit_used)
    TextView tvDepositUsed;
    @BindView(R2.id.lv_cart)
    LinearLayout lvContainer;

    private TxConfData txConfData;
    private TkpdProgressDialog mProgressDialog;

    public static Intent newInstance(Context context, TxConfData data) {
        Intent intent = new Intent(context, TxConfirmationDetailActivity.class);
        intent.putExtra(EXTRA_TX_CONFIRMATION_DATA, data);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.txConfData = extras.getParcelable(EXTRA_TX_CONFIRMATION_DATA);
    }

    @Override
    protected void initialPresenter() {
        presenter = new TxConfDetailPresenterImpl(this);
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_TX_P_CONFIRM_DETAIL;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transaction_confirmation_detail_tx_module;
    }

    @Override
    protected void initView() {
        mProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setViewListener() {
        tvTotalItem.setText(MessageFormat.format("{0} items ({1}kg)",
                txConfData.getConfirmation().getTotalItem(),
                txConfData.getConfirmation().getTotalWeight()));
        tvTotalTx.setText(txConfData.getConfirmation().getLeftAmount());
        tvDateTx.setText(txConfData.getConfirmation().getCreateTime());
        tvDueDateTx.setText(txConfData.getConfirmation().getPayDueDate());
        tvTotalItemPrice.setText(txConfData.getConfirmation().getOpenAmount());
        tvDepositUsed.setText(txConfData.getConfirmation().getDepositAmount());
        renderCartInfo();
    }

    private void renderCartInfo() {
        LayoutInflater vi = LayoutInflater.from(this);
        for (OrderData data : txConfData.getOrderDataList()) {
            View view = vi.inflate(R.layout.listview_shop_cart_payment_conf, lvContainer, false);
            final HolderCartItem holder = new HolderCartItem(view);
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnChosen.setText(data.getOrderDetail().getDetailPartialOrder().equals("0")
                    ? getString(R.string.title_cart_cancel_order)
                    : getString(R.string.title_cart_ship_left));
            holder.viewDetailInfo.setVisibility(View.GONE);
            holder.btnDetailInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (holder.viewDetailInfo.getVisibility() == View.GONE) {
                        holder.viewDetailInfo.setVisibility(View.VISIBLE);
                        holder.ivChevron.setImageResource(R.drawable.ic_chevron_up);
                    } else {
                        holder.viewDetailInfo.setVisibility(View.GONE);
                        holder.ivChevron.setImageResource(R.drawable.ic_chevron_down);
                    }
                }

            });
            holder.tvShopName.setText(MethodChecker.fromHtml(data.getOrderShop().getShopName()));
            holder.tvTotalPrice.setText(data.getOrderDetail().getDetailOpenAmountIdr());
            holder.tvShippingAddress.setText(data.getOrderDestination().getReceiverName());
            holder.tvShippingAgency.setText(MessageFormat.format("{0} - {1}",
                    data.getOrderShipment().getShipmentName(),
                    data.getOrderShipment().getShipmentProduct()));
            holder.tvTotalWeight.setText(MessageFormat.format("{0} {1} ( {2} kg ) ",
                    data.getOrderDetail().getDetailQuantity(),
                    getString(R.string.title_item), data.getOrderDetail().getDetailTotalWeight()));
            holder.tvSubTotal.setText(data.getOrderDetail().getDetailProductPriceIdr());
            holder.tvShippingCost.setText(data.getOrderDetail().getDetailShippingPriceIdr());
            holder.tvInsurancePrice.setText(data.getOrderDetail().getDetailInsurancePriceIdr());
            holder.tvAdditionalCostPrice.setText(data.getOrderDetail().getDetailTotalAddFeeIdr());
            holder.tvInsurance.setText(!data.getOrderDetail().getDetailInsurancePrice().equals("0")
                    || data.getOrderDetail().getDetailForceInsurance().equals("1")
                    ? getString(R.string.yes) : getString(R.string.No));
            renderProductCartInfo(holder.containerProduct, data.getOrderProducts());
            lvContainer.addView(view);
        }
    }

    private void renderProductCartInfo(LinearLayout container, List<OrderProduct> datas) {
        for (OrderProduct data : datas) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.listview_product_cart_payment_conf, container, false);
            final HolderProductCartItem holder = new HolderProductCartItem(view);
            ImageHandler.loadImageRounded2(this, holder.ivPic, data.getProductPicture());
            holder.tvName.setText(MethodChecker.fromHtml(data.getProductName()));
            holder.tvPrice.setText(data.getProductPrice());
            holder.tvWeight.setText(MessageFormat.format(" ( {0} kg ) ",
                    data.getProductWeight()));
            holder.tvPriceTotal.setText(data.getOrderSubtotalPriceIdr());
            holder.tvNotes.setText(MethodChecker.fromHtml(alterNotesData(data.getProductNotes())));
            holder.tvQty.setText(data.getProductQuantity());
            holder.tvNotes.setEnabled(false);
            container.addView(view);
        }
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showProgressLoading() {
        mProgressDialog.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showToastMessage(String message) {
        View view = findViewById(android.R.id.content);
        if (view != null) Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showDialog(Dialog dialog) {
        if (!dialog.isShowing()) dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        if (dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return getString(resId);
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    ) {
        return null;
    }

    @Override
    public void closeView() {
        finish();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyView();
        super.onDestroy();
    }

    @OnClick(R2.id.check_account)
    void actionCheckAccount() {
        TokopediaBankAccount.createShowAccountDialog(this);
    }

    @OnClick(R2.id.cancel_button)
    void actionCancelTransaction() {
        presenter.processCancelTransaction(this, txConfData);
    }

    @OnClick(R2.id.confirm_button)
    void actionConfirmTransaction() {
        presenter.processConfirmTransaction(this, txConfData);
    }

    @Override
    public void setResultActivity(int resultCode, Bundle bundle) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONFIRMATION:
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK);
                    finish();
                } else if (resultCode == ConfirmPaymentActivity.RESULT_FORM_FAILED
                        && data.hasExtra(ConfirmPaymentActivity.EXTRA_MESSAGE_ERROR_GET_FORM)) {
                    NetworkErrorHelper.showSnackbar(this,
                            data.getStringExtra(ConfirmPaymentActivity.EXTRA_MESSAGE_ERROR_GET_FORM));
                }
                break;
        }
    }

    public class HolderCartItem {
        @BindView(R2.id.listview_prod)
        LinearLayout containerProduct;
        @BindView(R2.id.shop_name)
        TextView tvShopName;
        @BindView(R2.id.total_price)
        TextView tvTotalPrice;
        @BindView(R2.id.shipping_address)
        TextView tvShippingAddress;
        @BindView(R2.id.shipping_agency)
        TextView tvShippingAgency;
        @BindView(R2.id.total_weight)
        TextView tvTotalWeight;
        @BindView(R2.id.sub_total)
        TextView tvSubTotal;
        @BindView(R2.id.shipping_cost)
        TextView tvShippingCost;
        @BindView(R2.id.insurance_price)
        TextView tvInsurancePrice;
        @BindView(R2.id.additional_cost)
        TextView tvAdditionalCostPrice;
        @BindView(R2.id.edit)
        ImageView btnEdit;
        @BindView(R2.id.delete)
        ImageView btnDelete;
        @BindView(R2.id.detail_info)
        View viewDetailInfo;
        @BindView(R2.id.detail_info_but)
        View btnDetailInfo;
        @BindView(R2.id.insurance)
        TextView tvInsurance;
        @BindView(R2.id.remaining_stock)
        TextView btnChosen;
        @BindView(R2.id.chevron_sign)
        ImageView ivChevron;

        HolderCartItem(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class HolderProductCartItem {
        @BindView(R2.id.img)
        ImageView ivPic;
        @BindView(R2.id.name)
        TextView tvName;
        @BindView(R2.id.price)
        TextView tvPrice;
        @BindView(R2.id.weight)
        TextView tvWeight;
        @BindView(R2.id.price_total)
        TextView tvPriceTotal;
        @BindView(R2.id.notes)
        TextView tvNotes;
        @BindView(R2.id.qty)
        TextView tvQty;

        HolderProductCartItem(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private String alterNotesData(String notes) {
        if (notes.equals("0")) {
            return "-";
        } else return notes;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
