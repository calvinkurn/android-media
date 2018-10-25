package com.tokopedia.seller.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ListViewHelper;
import com.tkpd.library.utils.SimpleSpinnerAdapter;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.ShippingConfirmationDetail;
import com.tokopedia.seller.customadapter.ListViewShopTxDetailProdListV2;
import com.tokopedia.seller.facade.FacadeActionShopTransaction;
import com.tokopedia.seller.selling.SellingService;
import com.tokopedia.seller.selling.constant.shopshippingdetail.ShopShippingDetailView;
import com.tokopedia.seller.selling.model.ModelParamSelling;
import com.tokopedia.seller.selling.model.modelConfirmShipping.Data;
import com.tokopedia.seller.selling.model.orderShipping.OrderDestination;
import com.tokopedia.seller.selling.model.orderShipping.OrderDetail;
import com.tokopedia.seller.selling.model.orderShipping.OrderShipment;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.seller.selling.model.orderShipping.OrderShop;
import com.tokopedia.seller.selling.presenter.listener.SellingView;
import com.tokopedia.seller.selling.view.activity.SellingDetailActivity;
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
 * Created by Tkpd_Eka on 2/17/2015.
 * modified by m.normansyah on 11/05/2016, ButterKnife and change json String to model
 */
@RuntimePermissions
public class FragmentShopShippingDetailV2 extends Fragment implements ShopShippingDetailView, SellingView {

    private static final String DATAPROCESSORDER = "data_process_order";
    private Bundle bundle;
    private boolean isConfirmDone = false;
    public static final int REQUEST_CODE_BARCODE = 1;

    public FragmentShopShippingDetailV2() {

    }

    public static FragmentShopShippingDetailV2 createInstance(Param param, int position) {
        FragmentShopShippingDetailV2 fragment = new FragmentShopShippingDetailV2();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID, param.orderId);
        bundle.putParcelable(ORDER_DATA, Parcels.wrap(param.orderShippingList));
        bundle.putString(USER_ID, param.userId);
        bundle.putString(PERMISSION, param.permission);

        bundle.putInt(POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static class Param {
        public String orderId;
        public OrderShippingList orderShippingList;
        public String userId;
        public String permission;
    }

    private View rootView;
    private ListViewShopTxDetailProdListV2 adapter;
    private TkpdProgressDialog progressDialog;

    OrderShippingList orderShippingList;
    private String orderId;
    private String userId;
    private String invoicePdf;
    private String invoiceUrl;
    private String permission;
    private FacadeActionShopTransaction facadeAction;
    private List<ShippingServices> serviceList;
    private String shippingID;
    private String pickupAddress;
    private int position;
    private CompositeSubscription _subscriptions = new CompositeSubscription();

    TextView buyerName;
    TextView invoice;
    TextView courier;
    TextView totalItem;
    TextView value;
    TextView receiverName;
    TextView destination;
    TextView errorMessage;
    ImageView scanBarcode;
    TextView confirmButton;
    TextView detailButton;
    EditText referenceNumber;
    TextView cancelButton;
    TextView senderName;
    TextView senderPhone;
    TextView errorSpinner;
    CheckBox switchCourier;
    Spinner spinnerAgency;
    Spinner spinnerService;
    View senderForm;
    ListView productListView;
    LinearLayout shippingLayout;
    ScrollView mainScroll;
    ProgressBar editFormProgress;
    public View viewDefaultDestination;
    public View viewPickupLocationCourier;
    public TextView pickupLocationDetail;
    public TextView deliveryLocationDetail;
    TextView askBuyer;

    public static class ShippingServices {
        public String serviceName;
        public String serviceId;
        public List<String> packageList;
        public List<String> packageId;

        public ShippingServices() {
            packageId = new ArrayList<>();
            packageList = new ArrayList<>();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar(savedInstanceState);
        getOrderData();
    }

    private void initVar(Bundle savedInstanceState) {
        position = getArguments().getInt(POSITION);
        orderId = getArguments().getString(ORDER_ID);
        orderShippingList = Parcels.unwrap(getArguments().getParcelable(ORDER_DATA));
        userId = getArguments().getString(USER_ID);
        permission = getArguments().getString(PERMISSION);
        serviceList = new ArrayList<>();
        facadeAction = FacadeActionShopTransaction.createInstance(getActivity(), orderId);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    private void getOrderData() {
        orderId = orderShippingList.getOrderDetail().getDetailOrderId() + "";
        invoicePdf = orderShippingList.getOrderDetail().getDetailPdf();
        invoiceUrl = orderShippingList.getOrderDetail().getDetailPdfUri();
    }

    private void getEditShippingForm() {
        editFormProgress.setVisibility(View.VISIBLE);
        facadeAction.setCompositeSubscription(_subscriptions);
        facadeAction.getShippingForm(onGetEditShippingForm());
    }

    private FacadeActionShopTransaction.OnGetEditShippingListener onGetEditShippingForm() {
        return new FacadeActionShopTransaction.OnGetEditShippingListener() {
            @Override
            public void onSuccess(List<ShippingServices> serviceLists) {
                serviceList = serviceLists;
                editFormProgress.setVisibility(View.GONE);
                initEditShipping();
            }

            @Override
            public void onFailed() {

            }
        };
    }

    private void initEditShipping() {
        List<String> serviceName = new ArrayList<>();
        for (int i = 0; i < serviceList.size(); i++) {
            serviceName.add(serviceList.get(i).serviceName);
        }
        SimpleSpinnerAdapter servicePackageAdapter = SimpleSpinnerAdapter.createAdapter(getActivity(), serviceName);
        spinnerAgency.setAdapter(servicePackageAdapter);
        spinnerAgency.setVisibility(View.VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initCreateView(inflater, container);
        if (savedInstanceState != null) {
            isConfirmDone = savedInstanceState.getBoolean(DATAPROCESSORDER, false);
            if (isConfirmDone) {
                finishShipping(true);
            }
        }
        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxUtils.unsubscribeIfNotNull(_subscriptions);
    }

    private void initCreateView(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.activity_shipping_confirmation_prod_conf, container, false);
        initView(rootView);
        setViewDataV4();
        setAdapter();
        getEditShippingForm();
    }

    private void setViewDataV4() {
        OrderDetail orderDetail = orderShippingList.getOrderDetail();
        totalItem.setText(MethodChecker.fromHtml(getString(R.string.title_total_item) + ": <b>" + orderDetail.getDetailQuantity() + " ( " + orderDetail.getDetailTotalWeight() + "kg )</b>"));
        invoice.setText(orderDetail.getDetailInvoice());
        value.setText(MethodChecker.fromHtml(getString(R.string.title_transaction_value) + " : <b>" + orderDetail.getDetailOpenAmountIdr() + "</b>"));

        if (CommonUtils.checkNullForZeroJson(orderDetail.getDetailDropshipName())
                && CommonUtils.checkNullForZeroJson(orderDetail.getDetailDropshipTelp())) {
            senderName.setText(orderDetail.getDetailDropshipName());
            senderPhone.setText(orderDetail.getDetailDropshipTelp());
            senderForm.setVisibility(View.VISIBLE);
        } else {
            senderForm.setVisibility(View.GONE);
        }

        buyerName.setText(orderShippingList.getOrderCustomer().getCustomerName());

        OrderShipment orderShipment = orderShippingList.getOrderShipment();
        courier.setText(orderShipment.getShipmentName() + "( " + orderShipment.getShipmentProduct() + " )");
        shippingID = orderShipment.getShipmentId();
        if (orderShippingList.getIsPickUp() == 1) {
            confirmButton.setText(getString(R.string.title_pickup_button));
            viewDefaultDestination.setVisibility(View.GONE);
            viewPickupLocationCourier.setVisibility(View.VISIBLE);
        } else {
            confirmButton.setText(getString(R.string.title_confirm_button));
            viewDefaultDestination.setVisibility(View.VISIBLE);
            viewPickupLocationCourier.setVisibility(View.GONE);
        }

        OrderDestination orderDestination = orderShippingList.getOrderDestination();
        receiverName.setText(MethodChecker.fromHtml(orderDestination.getReceiverName()));
        String vDest = MethodChecker.fromHtml(orderDestination.getAddressStreet()).toString()
                + "\n" + orderDestination.getAddressDistrict() + "    " + orderDestination.getAddressCity() + ", " + orderDestination.getAddressPostal()
                + "\n" + orderDestination.getAddressProvince() + "\n" + getString(R.string.title_phone) + " : " + orderDestination.getReceiverPhone();
        vDest = vDest.replaceAll("&#39;", "'");
        vDest = vDest.replaceAll("&amp;", "'");
        destination.setText(vDest);
        deliveryLocationDetail.setText(vDest);

        OrderShop orderShop = orderShippingList.getOrderShop();
        pickupAddress = MethodChecker.fromHtml(orderShop.getAddressStreet())
                + "\n" + MethodChecker.fromHtml(orderShop.getAddressCity()).toString() + ", " + MethodChecker.fromHtml(orderShop.getAddressPostal())
                + "\n" + orderShop.getAddressProvince()
                + "\n" + getString(R.string.title_phone) + ":" + orderShop.getShipperPhone();

        pickupLocationDetail.setText(pickupAddress);
        if (isConfirmDone) {
            finishShipping(true);
        }
    }

    @SuppressWarnings("EmptyCatchBlock")
    private void setAdapter() {
        adapter = ListViewShopTxDetailProdListV2.createInstance(getActivity(), orderShippingList.getOrderProducts());
        productListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(ProductDetailRouter
                        .createInstanceProductDetailInfoActivity(
                                getActivity(), getProductDataToPass(position)));
            }
        });
        askBuyer.setOnClickListener(onAskBuyerClickListener());
        ListViewHelper.getListViewSize(productListView);
    }

    private View.OnClickListener onAskBuyerClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainApplication.getAppContext() instanceof TkpdInboxRouter) {
                    Intent intent = ((TkpdInboxRouter) MainApplication.getAppContext())
                            .getAskBuyerIntent(getActivity(),
                                    orderShippingList.getOrderCustomer().getCustomerId(),
                                    orderShippingList.getOrderCustomer().getCustomerName(),
                                    orderShippingList.getOrderDetail().getDetailInvoice(),
                                    MethodChecker.fromHtml(
                                            getString(R.string.custom_content_message_ask_seller)
                                                    .replace("XXX",
                                                            orderShippingList.getOrderDetail()
                                                                    .getDetailPdfUri())).toString(),
                                    TkpdInboxRouter.TX_ASK_BUYER,
                                    orderShippingList.getOrderCustomer().getCustomerImage());
                    startActivity(intent);
                }
            }
        };
    }


    private void initView(View view) {
        invoice = (TextView) view.findViewById(R.id.invoice_text);
        buyerName = (TextView) view.findViewById(R.id.buyer_name);
        courier = (TextView) view.findViewById(R.id.courier);
        totalItem = (TextView) view.findViewById(R.id.total_item);
        value = (TextView) view.findViewById(R.id.value);
        receiverName = (TextView) view.findViewById(R.id.receiver_name);
        destination = (TextView) view.findViewById(R.id.destination);
        errorMessage = (TextView) view.findViewById(R.id.error_message);
        scanBarcode = (ImageView) view.findViewById(R.id.scan);
        confirmButton = (TextView) view.findViewById(R.id.confirm_button);
        detailButton = (TextView) view.findViewById(R.id.detail_button);
        referenceNumber = (EditText) view.findViewById(R.id.ship_ref_number);
        cancelButton = (TextView) view.findViewById(R.id.cancel_button);
        senderName = (TextView) view.findViewById(R.id.sender_name);
        senderPhone = (TextView) view.findViewById(R.id.sender_phone);
        errorSpinner = (TextView) view.findViewById(R.id.error_spinner);
        switchCourier = (CheckBox) view.findViewById(R.id.checkBoxSwitchCourier);
        spinnerAgency = (Spinner) view.findViewById(R.id.spinner_kurir);
        spinnerService = (Spinner) view.findViewById(R.id.spinner_type);
        senderForm = view.findViewById(R.id.sender_form);
        productListView = (ListView) view.findViewById(R.id.product_list);
        shippingLayout = (LinearLayout) view.findViewById(R.id.layout);
        mainScroll = (ScrollView) view.findViewById(R.id.scroll_view);
        editFormProgress = (ProgressBar) view.findViewById(R.id.loadingSpinner);
        viewDefaultDestination = view.findViewById(R.id.layout_destination_default);
        viewPickupLocationCourier = view.findViewById(R.id.layout_pickup_instant_shipping_courier);
        pickupLocationDetail = (TextView) view.findViewById(R.id.pickup_detail_location);
        deliveryLocationDetail = (TextView) view.findViewById(R.id.destination_detail_location);
        askBuyer = (TextView) view.findViewById(R.id.ask_buyer);

        buyerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBuyerName();
            }
        });

        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDetailClick();
            }
        });

        spinnerAgency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onAgencySelect(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        switchCourier.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onSwitchCourierChecked(compoundButton, b);
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConfirmClick();
            }
        });
        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInvoiceClick();
            }
        });
        scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBarCode();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog();
            }
        });
    }

    public void onBuyerName() {
        if (getActivity().getApplicationContext() instanceof SellerModuleRouter) {
            startActivity(((SellerModuleRouter) getActivity().getApplicationContext())
                    .getTopProfileIntent(getActivity(), userId));
        }
    }

    public void onDetailClick() {
        UnifyTracking.eventConfirmShippingDetails(getActivity());
        startActivity(ShippingConfirmationDetail.createInstance(getActivity(), orderShippingList, permission, userId, invoiceUrl, invoicePdf));
    }

    public void onAgencySelect(int position) {
        if (position == 0) {
            spinnerService.setVisibility(View.INVISIBLE);
        } else {
            setSpinnerService(position);
        }
    }

    public void onSwitchCourierChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            shippingLayout.setVisibility(View.VISIBLE);
        } else {
            resetError();
            shippingLayout.setVisibility(View.GONE);
            spinnerAgency.setSelection(0);
            spinnerService.setSelection(0);
        }
    }

    public void onConfirmClick() {
        if (checkConfirmationError()) {
            confirmShipping();
        }
    }

    public void onInvoiceClick() {
        AppUtils.InvoiceDialog(getActivity(), invoiceUrl, invoicePdf, invoice.getText().toString());
    }

    public void scanBarCode() {
        FragmentShopShippingDetailV2PermissionsDispatcher.onScanBarcodeWithCheck(FragmentShopShippingDetailV2.this);
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onScanBarcode() {
        CommonUtils.requestBarcodeScanner(this, CustomScannerBarcodeActivity.class);
    }

    public void cancelDialog() {
        createCancelDialog();
    }

    private void createCancelDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cancel_order);
        final EditText Remark = (EditText) dialog.findViewById(R.id.remark);
        TextView ConfirmButton = (TextView) dialog.findViewById(R.id.confirm_button);
        ConfirmButton.setOnClickListener(onCancelListener(dialog, Remark));
        dialog.show();
    }

    private View.OnClickListener onCancelListener(final Dialog dialog, final EditText remark) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remark.length() == 0) {
                    remark.setError(getActivity().getString(R.string.error_field_required));
                } else if (remark.length() < 5 && remark.length() > 0) {
                    remark.setError("Minimal 5 karakter");
                } else if (remark.length() >= 5) {
                    cancelShipping(remark.getText().toString());
                    dialog.dismiss();
                }
            }
        };
    }

    private void cancelShipping(String remark) {
        UnifyTracking.eventConfirmShippingCancel(getActivity());
        bundle = new Bundle();
        ModelParamSelling modelParamSelling = new ModelParamSelling();
        modelParamSelling.setActionType("reject");
        modelParamSelling.setOrderId(orderId);
        modelParamSelling.setReason(remark);

        modelParamSelling.setPosition(position);
        bundle.putParcelable(SellingService.MODEL_PARAM_SELLING_KEY, Parcels.wrap(modelParamSelling));

        ((SellingDetailActivity) getActivity()).SellingAction(SellingService.CANCEL_SHIPPING, bundle);

//        progressDialog.showDialog();
//        facadeAction.cancelShipping(remark, onProcessShippingListener());
    }

    private void confirmShipping() {
        bundle = new Bundle();
        ModelParamSelling modelParamSelling = new ModelParamSelling();
        modelParamSelling.setActionType("confirm");
        modelParamSelling.setOrderId(orderId);
        modelParamSelling.setPosition(position);
        modelParamSelling.setRefNum(referenceNumber.getText().toString());
        modelParamSelling.setShipmentId(getAgencyId());
        modelParamSelling.setShipmentName(getAgencyName());
        modelParamSelling.setSpId(getServiceId());
        bundle.putParcelable(SellingService.MODEL_PARAM_SELLING_KEY, Parcels.wrap(modelParamSelling));
        UnifyTracking.eventConfirmShipping(getActivity());
        ((SellingDetailActivity) getActivity()).SellingAction(SellingService.CONFIRM_SHIPPING, bundle);
    }

    private boolean checkConfirmationError() {
        resetError();
        if (orderShippingList.getIsPickUp() != 1 && (referenceNumber.length() < 7 || referenceNumber.length() > 17)) {
            referenceNumber.requestFocus();
            referenceNumber.setError(getString(R.string.error_receipt_number));
            return false;
        }
        if (switchCourier.isChecked()) {
            if (spinnerAgency.getSelectedItemPosition() == 0) {
                errorSpinner.setText(getString(R.string.error_shipping_must_choose));
                errorSpinner.setVisibility(View.VISIBLE);
                return false;
            }
            if (spinnerService.getSelectedItemPosition() == 0) {
                errorSpinner.setText(getString(R.string.error_service_must_choose));
                errorSpinner.setVisibility(View.VISIBLE);
                return false;
            }
        }
        return true;
    }

    private String getAgencyId() {
        try {
            return serviceList.get(spinnerAgency.getSelectedItemPosition()).serviceId;
        } catch (Exception e) {
            return "";
        }
    }

    private String getAgencyName() {
        try {
            return serviceList.get(spinnerAgency.getSelectedItemPosition()).serviceName;
        } catch (Exception e) {
            return "";
        }
    }

    private String getServiceId() {
        try {
            return serviceList.get(spinnerAgency.getSelectedItemPosition()).packageId.get(spinnerService.getSelectedItemPosition());
        } catch (Exception e) {
            return "";
        }
    }

    private FacadeActionShopTransaction.OnConfirmMultiShippingListener onProcessShippingListener() {
        return new FacadeActionShopTransaction.OnConfirmMultiShippingListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                finishShipping(false);
            }

            @Override
            public void onFailed() {
                progressDialog.dismiss();
            }
        };
    }

    private void finishShipping(boolean isAfterSaveInstance) {
        cancelButton.setVisibility(View.GONE);
        confirmButton.setText(getActivity().getString(R.string.title_order_processed));
        confirmButton.setBackgroundResource(com.tokopedia.seller.R.drawable.button_finish_disable);
        confirmButton.setTextColor(ContextCompat.getColor(getActivity(), com.tokopedia.seller.R.color.black_twenty_five_percent));
        confirmButton.setOnClickListener(null);
        referenceNumber.setClickable(false);
        referenceNumber.setFocusable(false);
        spinnerAgency.setEnabled(false);
        spinnerService.setEnabled(false);
        spinnerAgency.setClickable(false);
        spinnerService.setClickable(false);
        scanBarcode.setClickable(false);
        switchCourier.setClickable(false);
        isConfirmDone = true;
        if (!isAfterSaveInstance) {
            getActivity().setResult(getActivity().RESULT_OK);
//            ((SellingDetailActivity) getActivity()).notifyChangeShipping();
        }

    }

    private void setSpinnerService(int pos) {
        List<String> packageName = new ArrayList<>();
        for (int i = 0; i < serviceList.get(pos).packageList.size(); i++) {
            packageName.add(serviceList.get(pos).packageList.get(i));
        }
        SimpleSpinnerAdapter servicePackageAdapter = SimpleSpinnerAdapter.createAdapter(getActivity(), packageName);
        spinnerService.setAdapter(servicePackageAdapter);
        spinnerService.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        referenceNumber.setText(CommonUtils.getBarcode(requestCode, resultCode, data));
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        CommonUtils.dumper("NISTAG : ONRESUME");
        getEditShippingForm();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isConfirmDone) {
            outState.putBoolean(DATAPROCESSORDER, true);
        }
    }

    @Override
    public void showProgress() {
        progressDialog.showDialog();
    }


    @Override
    public int getFragmentId() {
        return 0;
    }

    @Override
    public void ariseRetry(int type, Object... data) {
        switch (type) {
            case SellingService.CONFIRM_SHIPPING:
            case SellingService.CANCEL_SHIPPING:
                progressDialog.dismiss();
                break;
        }
    }

    @Override
    public void setData(final int type, Bundle data) {
        switch (type) {
            case SellingService.CONFIRM_SHIPPING:
            case SellingService.CANCEL_SHIPPING:
                Data result = Parcels.unwrap(data.getParcelable(SellingService.MODEL_CONFIRM_SHIPPING_KEY));
                progressDialog.dismiss();
                if (result.getIsSuccess() == 1) {
                    finishShipping(false);
                }else{
                    NetworkErrorHelper.showDialog(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            if (bundle != null) {
                                if(type == SellingService.CONFIRM_SHIPPING) {
                                    ((SellingDetailActivity) getActivity()).SellingAction(SellingService.CONFIRM_SHIPPING, bundle);
                                }else{
                                    ((SellingDetailActivity) getActivity()).SellingAction(SellingService.CANCEL_SHIPPING, bundle);
                                }
                            }
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        switch (type) {
            case SellingService.CONFIRM_SHIPPING:
                progressDialog.dismiss();
                NetworkErrorHelper.showDialog(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        if (bundle != null) {
                            ((SellingDetailActivity) getActivity()).SellingAction(SellingService.CONFIRM_SHIPPING, bundle);
                        }
                    }
                });
                break;
            case SellingService.CANCEL_SHIPPING:
                progressDialog.dismiss();
                NetworkErrorHelper.showDialog(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        if (bundle != null) {
                            ((SellingDetailActivity) getActivity()).SellingAction(SellingService.CANCEL_SHIPPING, bundle);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String error = (String) data[0];

        switch (type) {
            case SellingService.CONFIRM_SHIPPING:
            case SellingService.CANCEL_SHIPPING:
                progressDialog.dismiss();
                showSnackBarError(error);
                break;
        }
    }

    public void showSnackBarError(String error) {
        final Snackbar snackbar = SnackbarManager.make(getActivity(),
                error,
                Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    private void resetError() {
        errorSpinner.setVisibility(View.GONE);
    }

    private void showDialogError(Context context, String error) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        @SuppressLint("InflateParams")
        View promptsView = li.inflate(R.layout.error_network_dialog, null);
        TextView msg = (TextView) promptsView.findViewById(R.id.msg);
        msg.setText(error);
        dialog.setView(promptsView);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FragmentShopShippingDetailV2PermissionsDispatcher.onRequestPermissionsResult(
                FragmentShopShippingDetailV2.this, requestCode, grantResults);
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
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.CAMERA);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.CAMERA);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onPermissionDenied(getActivity(), listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onNeverAskAgain(getActivity(), listPermission);
    }

    private ProductPass getProductDataToPass(int position) {
        return ProductPass.Builder.aProductPass()
                .setProductPrice(orderShippingList.getOrderProducts().get(position).getProductPrice())
                .setProductId(orderShippingList.getOrderProducts().get(position).getProductId())
                .setProductName(orderShippingList.getOrderProducts().get(position).getProductName())
                .setProductImage(orderShippingList.getOrderProducts().get(position).getProductPicture())
                .build();
    }

}
