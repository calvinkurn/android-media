package com.tokopedia.seller.orderstatus.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ListViewHelper;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.customView.OrderStatusView;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.purchase.model.response.txlist.OrderHistory;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.tracking.activity.TrackingActivity;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.seller.OrderHistoryView;
import com.tokopedia.seller.customadapter.ListViewShopTxDetailProdListV2;
import com.tokopedia.seller.orderstatus.presenter.StatusDetailPresenter;
import com.tokopedia.seller.orderstatus.presenter.StatusDetailPresenterImpl;
import com.tokopedia.seller.selling.model.orderShipping.OrderCustomer;
import com.tokopedia.seller.selling.model.orderShipping.OrderDetail;
import com.tokopedia.seller.selling.model.orderShipping.OrderPayment;
import com.tokopedia.seller.selling.model.orderShipping.OrderShipment;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.seller.selling.view.fragment.CustomScannerBarcodeActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 2/14/17. Tokopedia
 */

@RuntimePermissions
public class FragmentShopTxStatusDetailV2 extends TkpdBaseV4Fragment
        implements ShopStatusDetailView {

    public FragmentShopTxStatusDetailV2() {

    }

    public static FragmentShopTxStatusDetailV2 createInstance(OrderShippingList orderData,
                                                              String permission) {
        FragmentShopTxStatusDetailV2 fragment = new FragmentShopTxStatusDetailV2();
        Bundle bundle = new Bundle();
        bundle.putParcelable("order_data", Parcels.wrap(orderData));
        bundle.putString("permission", permission);
        fragment.setArguments(bundle);
        return fragment;
    }

    private View rootView;
    private ViewHolder holder;
    private TkpdProgressDialog progressDialog;
    private Dialog editRefDialog;
    private CompositeSubscription _subscriptions = new CompositeSubscription();
    private StatusDetailPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new StatusDetailPresenterImpl(this);
        if (rootView == null) {
            initCreateView(inflater, container);
        } else
            holder = (ViewHolder) rootView.getTag();

        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);
        fetchDataFromArguments();
        setAdapter();
        setListener();
        if (getActivity() != null) {
            ScreenTracking.screen(getScreenName());
        }
        return rootView;
    }

    @Override
    public void setPaymentData(OrderPayment payment) {
        holder.PaymentMethod
                .setText(MethodChecker
                        .fromHtml(getString(R.string.title_payment_method) + " : <b>" +
                                payment.getPaymentGatewayName() + "</b>"));

        holder.Transaction.setText(payment.getPaymentVerifyDate());
    }

    @Override
    public void setCustomerDataToView(OrderCustomer customer) {
        holder.BuyerName.setText(MethodChecker.fromHtml(customer.getCustomerName()));
    }

    @Override
    public void setOrderDetailData(OrderDetail orderDetailData) {
        holder.Invoice.setText(orderDetailData.getDetailInvoice());
        if (CommonUtils.checkNullForZeroJson(orderDetailData.getDetailDropshipName())) {
            holder.SenderName.setText(MethodChecker.fromHtml(orderDetailData.getDetailDropshipName()));
            holder.SenderPhone.setText(orderDetailData.getDetailDropshipTelp());
            holder.SenderForm.setVisibility(View.VISIBLE);
        } else {
            holder.SenderForm.setVisibility(View.GONE);
        }
        holder.AdditionalCost.setText(orderDetailData.getDetailTotalAddFeeIdr());
        holder.ShippingCost.setText(orderDetailData.getDetailShippingPriceIdr());
        holder.Quantity.setText(orderDetailData.getDetailQuantity() + " item (" +
                orderDetailData.getDetailTotalWeight() + " kg)");
        holder.GrandTotal.setText(orderDetailData.getDetailOpenAmountIdr());
    }

    @Override
    public void setDeliveryLocationDetail() {
        holder.deliveryLocationDetail.setText(MethodChecker
                .fromHtml(presenter.generatedDestinationString()));
        holder.DestinationDetail.setText(MethodChecker
                .fromHtml(presenter.generatedDestinationString()));
    }

    @Override
    public void setShipmentDetailToView(OrderShipment shipping) {
        holder.Destination.setText(MethodChecker.fromHtml(shipping.getShipmentName() + " - " + shipping.getShipmentProduct()));
    }

    @Override
    public void showSnackbarError(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void setPickUpAddressToView(String pickupAddress) {
        holder.pickupLocationDetail.setText(pickupAddress);
    }

    @Override
    public String getRefNumber() {
        return holder.RefNumber.getText().toString();
    }

    @Override
    public void removesOrderStatusLayoutView() {
        holder.OrderStatusLayout.removeAllViews();
    }

    @Override
    public void addOrderStatusView(OrderHistory orderHistory) {
        holder.OrderStatusLayout
                .addView(OrderStatusView.createInstance(getActivity(), orderHistory).getView());
    }

    @Override
    public void showInfoSnackbar(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        progressDialog.showDialog();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    private class ViewHolder {
        private TextView ShippingCost;
        private TextView AdditionalCost;
        private TextView Destination;
        private TextView DestinationDetail;
        private TextView Quantity;
        private TextView GrandTotal;
        private TextView Transaction;
        private TextView ErrorMessage;
        private TextView RefNumber;
        private TextView EditRef;
        private TextView SeeAll;
        private TextView Track;
        private LinearLayout OrderStatusLayout;
        private TextView PaymentMethod;
        private TextView Invoice;
        private TextView BuyerName;
        private ListView ProductListView;
        private TextView SenderName;
        private TextView SenderPhone;
        private View SenderForm;
        private View viewDefaultDestination;
        private View viewPickupLocationCourier;
        private TextView pickupLocationDetail;
        private TextView deliveryLocationDetail;
        private TextView retryPickupButton;
        private TextView askBuyer;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_TX_PEOPLE_DETAIL;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onViewDestroyed();
    }

    private void initCreateView(LayoutInflater inflater, ViewGroup container) {
        holder = new ViewHolder();
        rootView = inflater.inflate(R.layout.activity_shop_shipping_status_detail, container, false);
        initView();
        rootView.setTag(holder);
    }

    private void initView() {
        holder.OrderStatusLayout = (LinearLayout) rootView.findViewById(R.id.order_status_layout);
        holder.PaymentMethod = (TextView) rootView.findViewById(R.id.payment_method);
        holder.Invoice = (TextView) rootView.findViewById(R.id.invoice_text);
        holder.ShippingCost = (TextView) rootView.findViewById(R.id.shipping_cost);
        holder.SeeAll = (TextView) rootView.findViewById(R.id.see_all);
        holder.AdditionalCost = (TextView) rootView.findViewById(R.id.additional_cost);
        holder.Destination = (TextView) rootView.findViewById(R.id.destination);
        holder.Transaction = (TextView) rootView.findViewById(R.id.transaction);
        holder.DestinationDetail = (TextView) rootView.findViewById(R.id.destination_detail);
        holder.Quantity = (TextView) rootView.findViewById(R.id.quantity);
        holder.EditRef = (TextView) rootView.findViewById(R.id.edit_ref);
        holder.Track = (TextView) rootView.findViewById(R.id.track);
        holder.GrandTotal = (TextView) rootView.findViewById(R.id.grand_total);
        holder.RefNumber = (TextView) rootView.findViewById(R.id.stat_ref_number);
        holder.BuyerName = (TextView) rootView.findViewById(R.id.buyer_name);
        holder.ErrorMessage = (TextView) rootView.findViewById(R.id.error_message);
        holder.SenderName = (TextView) rootView.findViewById(R.id.sender_name);
        holder.SenderPhone = (TextView) rootView.findViewById(R.id.sender_phone);
        holder.SenderForm = rootView.findViewById(R.id.sender_form);
        holder.ProductListView = (ListView) rootView.findViewById(R.id.product_list);
        holder.viewDefaultDestination = rootView.findViewById(R.id.layout_destination_default);
        holder.viewPickupLocationCourier = rootView
                .findViewById(R.id.layout_pickup_instant_shipping_courier);
        holder.pickupLocationDetail = (TextView) rootView.findViewById(R.id.pickup_detail_location);
        holder.deliveryLocationDetail = (TextView) rootView
                .findViewById(R.id.destination_detail_location);
        holder.retryPickupButton = (TextView) rootView.findViewById(R.id.retry_pickup_button);
        holder.askBuyer = (TextView) rootView.findViewById(R.id.ask_buyer);
    }

    private void setAdapter() {
        ListViewShopTxDetailProdListV2 productAdapter = ListViewShopTxDetailProdListV2
                .createInstance(getActivity(),
                        presenter.getOrderData().getOrderProducts());
        holder.ProductListView.setAdapter(productAdapter);
        holder.ProductListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(
                        ProductDetailRouter.createInstanceProductDetailInfoActivity(
                                getActivity(),
                                getProductDataToPass(position)));
            }
        });
        ListViewHelper.getListViewSize(holder.ProductListView);
    }

    private void setListener() {
        holder.BuyerName.setOnClickListener(onBuyerNameClick());
        holder.SeeAll.setOnClickListener(onSeeAllClick());
        holder.Invoice.setOnClickListener(onInvoiceClick());
        holder.EditRef.setOnClickListener(onEditRefClick());
        holder.Track.setOnClickListener(onTrackClick());
        holder.askBuyer.setOnClickListener(onAskBuyerClickListener());
    }

    private View.OnClickListener onAskBuyerClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainApplication.getAppContext() instanceof TkpdInboxRouter) {
                    Intent intent = ((TkpdInboxRouter) MainApplication.getAppContext())
                            .getAskBuyerIntent(getActivity(),
                                    presenter.getOrderData().getOrderCustomer().getCustomerId(),
                                    presenter.getOrderData().getOrderCustomer().getCustomerName(),
                                    presenter.getOrderData().getOrderDetail().getDetailInvoice(),
                                    MethodChecker.fromHtml(
                                            getString(R.string.custom_content_message_ask_seller)
                                                    .replace("XXX",
                                                            presenter.getOrderData().getOrderDetail()
                                                                    .getDetailPdfUri())).toString(),
                                    TkpdInboxRouter.TX_ASK_BUYER,
                                    presenter.getOrderData().getOrderCustomer().getCustomerImage());
                    startActivity(intent);
                }
            }
        };
    }

    private View.OnClickListener onBuyerNameClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(((TkpdCoreRouter) getActivity().getApplicationContext())
                        .getTopProfileIntent(getActivity(),
                                presenter.getInvoiceData().getUserId()));
            }
        };
    }

    private View.OnClickListener onSeeAllClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(OrderHistoryView.createInstanceSeller(getActivity(),
                        presenter.getInvoiceData().getStatusList()));
            }
        };
    }

    private View.OnClickListener onInvoiceClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.InvoiceDialog(getActivity(),
                        presenter.getInvoiceData().getInvoiceUrl(),
                        presenter.getInvoiceData().getInvoicePdf(),
                        holder.Invoice.getText().toString());
            }
        };
    }

    private View.OnClickListener onEditRefClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEditRefDialog();
            }
        };
    }

    private View.OnClickListener onTrackClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTracking();
            }
        };
    }

    private void openTracking() {
        Intent intent = new Intent(getActivity(), TrackingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("OrderID", presenter.getInvoiceData().getOrderId());
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    private void createEditRefDialog() {
        editRefDialog = new Dialog(getActivity());
        editRefDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editRefDialog.setContentView(R.layout.dialog_edit_ref);
        final EditText Ref = (EditText) editRefDialog.findViewById(R.id.ref_number);
        Ref.setText(presenter.getInvoiceData().getReferenceNumber());
        TextView ConfirmButton = (TextView) editRefDialog.findViewById(R.id.confirm_button);
        View vScan = editRefDialog.findViewById(R.id.scan);
        vScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentShopTxStatusDetailV2PermissionsDispatcher
                        .onScanBarcodeClickedWithCheck(FragmentShopTxStatusDetailV2.this);
            }
        });
        ConfirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkEditRef(Ref)) {
                    actionEditRefNum(Ref.getText().toString());
                    editRefDialog.dismiss();
                }
            }
        });

        editRefDialog.show();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onScanBarcodeClicked() {
        CommonUtils.requestBarcodeScanner(this, CustomScannerBarcodeActivity.class);
    }

    private boolean checkEditRef(EditText ref) {
        if (ref.getText().toString().equals(presenter.getInvoiceData().getReferenceNumber())) {
            ref.setError(getActivity().getString(R.string.edit_ref_error));
            return false;
        }

        if (ref.length() > 7 && ref.length() < 18) {
            return true;
        } else {
            if (ref.length() == 0)
                ref.setError(getString(R.string.error_field_required));
            else
                ref.setError(getString(R.string.error_receipt_number));
            return false;
        }
    }

    private void actionEditRefNum(String refNum) {
        presenter.getInvoiceData().setReferenceNumber(refNum);
        holder.ErrorMessage.setVisibility(View.GONE);
        progressDialog.showDialog();
        presenter.editRefNumber(getActivity(), refNum);
    }

    @Override
    public void setRefNum(String refNum) {
        if (refNum.length() > 0)
            holder.RefNumber.setText(MethodChecker.fromHtml(getString(R.string.title_reference_number)
                    + " : <b>" + refNum + "</b>"));
    }

    @Override
    public void hideTrackButton() {
        holder.Track.setVisibility(View.GONE);
    }

    @Override
    public void showTrackButton() {
        holder.Track.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPickUpVisibility() {
        holder.viewDefaultDestination.setVisibility(View.GONE);
        holder.viewPickupLocationCourier.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePickUpVisibility() {
        holder.viewDefaultDestination.setVisibility(View.VISIBLE);
        holder.viewPickupLocationCourier.setVisibility(View.GONE);
    }

    @Override
    public void hideEditRefNum() {
        holder.EditRef.setVisibility(View.GONE);
    }

    @Override
    public void showEditRefNum() {
        holder.EditRef.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoConnectionError() {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_network_error_1));
    }

    @Override
    public void hideRetryPickUpButton() {
        holder.retryPickupButton.setVisibility(View.GONE);
    }

    @Override
    public void showRetryPickUpButton() {
        holder.retryPickupButton.setVisibility(View.VISIBLE);
        holder.retryPickupButton.setOnClickListener(onRetryPickUpButtonClickedListener());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar();
    }

    private void fetchDataFromArguments() {
        try {
            OrderShippingList order = Parcels.unwrap(getArguments().getParcelable("order_data"));
            String permission = getArguments().getString("permission");
            presenter.setOrderDataToInvoiceModel(order);
            presenter.setPermission(permission);
            presenter.setOrderDataToView();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(),
                    getActivity().getString(R.string.title_verification_timeout)
                            + "\n"
                            + getActivity().getString(R.string.message_verification_timeout),
                    Toast.LENGTH_LONG).show();
            if(!GlobalConfig.DEBUG) Crashlytics.log(0,
                    "NullPointerException "
                            + getActivity().getClass().getSimpleName(), e.toString());
            getActivity().finish();
        }
    }

    private void initVar() {
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final EditText Ref = (EditText) editRefDialog.findViewById(R.id.ref_number);
        Ref.setText(CommonUtils.getBarcode(requestCode, resultCode, data));
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FragmentShopTxStatusDetailV2PermissionsDispatcher.onRequestPermissionsResult(
                FragmentShopTxStatusDetailV2.this, requestCode, grantResults);
    }


    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onShowRationale(getActivity(), request, listPermission);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(getActivity(),Manifest.permission.CAMERA);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(),Manifest.permission.CAMERA);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onPermissionDenied(getActivity(),listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onNeverAskAgain(getActivity(),listPermission);
    }

    private ProductPass getProductDataToPass(int position) {
        return ProductPass.Builder.aProductPass()
                .setProductPrice(presenter.getOrderData()
                        .getOrderProducts().get(position).getProductPrice())
                .setProductId(presenter
                        .getOrderData().getOrderProducts().get(position).getProductId())
                .setProductName(presenter
                        .getOrderData().getOrderProducts().get(position).getProductName())
                .setProductImage(presenter
                        .getOrderData().getOrderProducts().get(position).getProductPicture())
                .build();
    }

    private View.OnClickListener onRetryPickUpButtonClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRetryPickupDialog();
            }
        };
    }

    private void createRetryPickupDialog() {
        AlertDialog.Builder retryPickupDialog = new AlertDialog.Builder(getActivity());
        retryPickupDialog.setView(R.layout.retry_pickup_dialog);
        retryPickupDialog.setPositiveButton(getString(R.string.title_yes),
                onConfirmRetryPickup());
        retryPickupDialog.setNegativeButton(getString(R.string.title_cancel), null);
        retryPickupDialog.show();
    }

    private DialogInterface.OnClickListener onConfirmRetryPickup() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.retryPickup(getActivity());
            }
        };
    }
}
