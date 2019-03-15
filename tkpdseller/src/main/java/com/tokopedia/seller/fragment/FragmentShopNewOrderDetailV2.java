package com.tokopedia.seller.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ListViewHelper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.customadapter.ListViewShopTxDetailProdListV2;
import com.tokopedia.seller.selling.SellingService;
import com.tokopedia.seller.selling.model.ModelParamSelling;
import com.tokopedia.seller.selling.model.modelConfirmShipping.Data;
import com.tokopedia.seller.selling.model.orderShipping.OrderCustomer;
import com.tokopedia.seller.selling.model.orderShipping.OrderDestination;
import com.tokopedia.seller.selling.model.orderShipping.OrderDetail;
import com.tokopedia.seller.selling.model.orderShipping.OrderPayment;
import com.tokopedia.seller.selling.model.orderShipping.OrderProduct;
import com.tokopedia.seller.selling.model.orderShipping.OrderShipment;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.seller.selling.model.orderShipping.OrderShop;
import com.tokopedia.seller.selling.model.shopneworderdetail.ShopNewOrderDetailView;
import com.tokopedia.seller.selling.orderReject.ConfirmRejectOrderActivity;
import com.tokopedia.seller.selling.orderReject.adapter.ProductListAdapter;
import com.tokopedia.seller.selling.orderReject.fragment.ConstrainRejectedDialog;
import com.tokopedia.seller.selling.orderReject.model.DataResponseReject;
import com.tokopedia.seller.selling.orderReject.model.ModelRejectOrder;
import com.tokopedia.seller.selling.presenter.listener.SellingView;
import com.tokopedia.seller.selling.view.activity.SellingDetailActivity;
import com.tokopedia.seller.util.NewOrderDialogBuilder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tkpd_Eka on 2/12/2015.
 */
public class FragmentShopNewOrderDetailV2 extends Fragment implements ShopNewOrderDetailView, SellingView {

    private static final String DATAPROCESSORDER = "data_process_order";
    public static final String RECEIVER_TRIGER_REFRESH_ORDER = "receiver_triger_refresh_order";
    private Bundle bundle;
    private boolean isConfirmDone = false;
    private static final int REQ_REJECT_ORDER = 1;
    private boolean shouldBroadcast;
    private Activity activity;

    public FragmentShopNewOrderDetailV2() {

    }

    public static FragmentShopNewOrderDetailV2 createInstance(OrderShippingList orderData, String permission, int position) {
        FragmentShopNewOrderDetailV2 fragment = new FragmentShopNewOrderDetailV2();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ORDER_DATA, Parcels.wrap(orderData));
        bundle.putString(PERMISSION, permission);
        bundle.putString(INVOICE_URI, orderData.getOrderDetail().getDetailPdfUri());
        bundle.putString(INVOICE_PDF, orderData.getOrderDetail().getDetailPdf());
        bundle.putInt(POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    private static final String PHONE_TOKOPEDIA = "021-53691015";
    private ViewHolder holder;
    private TkpdProgressDialog progressDialog;
    private View rootView;
    private OrderShippingList order;
    private ListViewShopTxDetailProdListV2 adapter;
    private String permission;
    private String invoiceUri;
    private String invoicePdf;
    private String userId;
    private String orderId;
    private boolean isProcessed = false;
    private Model model;
    private int position;

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
        progressDialog.dismiss();
    }

    @Override
    public void setData(int type, Bundle data) {
        if (type == SellingService.REJECT_ORDER_WITH_REASON) {
            DataResponseReject dataResponseReject = Parcels.unwrap(data.getParcelable(DataResponseReject.MODEL_DATA_REJECT_RESPONSE_KEY));
            if (dataResponseReject.getIsSuccess() == 1) {
                onProceedComplete(false);
            }
        } else {
            Data result = Parcels.unwrap(data.getParcelable(SellingService.MODEL_CONFIRM_SHIPPING_KEY));
            if (result.getIsSuccess() == 1) {
                onProceedComplete(false);
            }
        }
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        switch (type) {
            case SellingService.PARTIAL_NEW_ORDER:
                NetworkErrorHelper.showDialog(activity, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        if (bundle != null) {
                            ((SellingDetailActivity) getActivity()).SellingAction(SellingService.PARTIAL_NEW_ORDER, bundle);
                        }
                    }
                });
                break;
            case SellingService.CONFIRM_NEW_ORDER:
                NetworkErrorHelper.showDialog(activity, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        if (bundle != null) {
                            ((SellingDetailActivity) getActivity()).SellingAction(SellingService.CONFIRM_NEW_ORDER, bundle);
                        }
                    }
                });
                break;
            case SellingService.REJECT_ORDER_WITH_REASON:
                NetworkErrorHelper.showDialog(activity, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        if (bundle != null) {
                            ((SellingDetailActivity) getActivity()).SellingAction(SellingService.REJECT_ORDER_WITH_REASON, bundle);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String error = (String) data[0];
        if (error != null) {
            onProceedFailed(error);
        }
    }

    private class ViewHolder {
        TextView Invoice;
        TextView BuyerName;
        ListView ProductListView;
        TextView AcceptButton;
        TextView RejectButton;
        TextView PartialButton;
        TextView Deadline;
        TextView ShippingCost;
        TextView AdditionalCost;
        TextView PriceInsurance;
        TextView Destination;
        TextView DestinationDetail;
        TextView Quantity;
        TextView GrandTotal;
        TextView ErrorMessage;
        TextView SenderName;
        TextView SenderPhone;
        View SenderForm;
        public View viewDefaultDestination;
        public View viewPickupLocationCourier;
        public TextView pickupLocationDetail;
        public TextView deliveryLocationDetail;
        public LinearLayout wrapperInsurance;
        View wrapperBuyerRequestCancel;
        TextView buyerRequestCancel;
        TextView dateRequestCancel;
        TextView askBuyer;
    }

    public static class Model {
        public String shippingID;
        public String shippingProduct;
        public String shippingName;
        public String pickupAddress;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        defineOrderId();
        getOrderId();
        setRetainInstance(true);
    }

    private void initVar() {
        progressDialog = new TkpdProgressDialog(activity, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (rootView == null) {
        initCreateView(inflater, container);
//        } else
//            loadViewHolder();
        if (savedInstanceState != null) {
            isConfirmDone = savedInstanceState.getBoolean(DATAPROCESSORDER, false);
            if (isConfirmDone) {
                onProceedComplete(true);
            }
        }

        setListener();

        return rootView;
    }

    private void initCreateView(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.activity_shop_order_detail, container, false);
        initVar();
        initView();
        setViewDataV4();
        setAdapter();
        rootView.setTag(holder);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isConfirmDone) {
            outState.putBoolean(DATAPROCESSORDER, true);
        }
    }

    private void defineOrderId() {
        order = Parcels.unwrap(getArguments().getParcelable(ORDER_DATA));
        permission = getArguments().getString(PERMISSION);
        invoicePdf = getArguments().getString(INVOICE_PDF);
        invoiceUri = getArguments().getString(INVOICE_URI);
        position = getArguments().getInt(POSITION);
    }

    private void getOrderId() {
        try {
            OrderDetail orderdata = order.getOrderDetail();
            orderId = orderdata.getDetailOrderId().toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(activity, activity.getString(R.string.title_verification_timeout) + "\n" + activity.getString(R.string.message_verification_timeout), Toast.LENGTH_LONG).show();
            if(!GlobalConfig.DEBUG) Crashlytics.log(0, "NullPointerException FragmentShopNewOrderDetailV2.java", e.toString());
            activity.finish();
        }
    }

    private void initView() {
        holder = new ViewHolder();
        holder.Deadline = (TextView) rootView.findViewById(R.id.deadline);
        holder.Invoice = (TextView) rootView.findViewById(R.id.invoice_text);
        holder.ShippingCost = (TextView) rootView.findViewById(R.id.shipping_cost);
        holder.AdditionalCost = (TextView) rootView.findViewById(R.id.additional_cost);
        holder.PriceInsurance = (TextView) rootView.findViewById(R.id.price_insurance);
        holder.Destination = (TextView) rootView.findViewById(R.id.destination);
        holder.DestinationDetail = (TextView) rootView.findViewById(R.id.destination_detail);
        holder.Quantity = (TextView) rootView.findViewById(R.id.quantity);
        holder.GrandTotal = (TextView) rootView.findViewById(R.id.grand_total);
        holder.PartialButton = (TextView) rootView.findViewById(R.id.partial);
        holder.BuyerName = (TextView) rootView.findViewById(R.id.buyer_name);
        holder.ErrorMessage = (TextView) rootView.findViewById(R.id.error_message);
        holder.SenderName = (TextView) rootView.findViewById(R.id.sender_name);
        holder.SenderPhone = (TextView) rootView.findViewById(R.id.sender_phone);
        holder.SenderForm = rootView.findViewById(R.id.sender_form);
        holder.AcceptButton = (TextView) rootView.findViewById(R.id.accept);
        holder.RejectButton = (TextView) rootView.findViewById(R.id.reject);
        holder.ProductListView = (ListView) rootView.findViewById(R.id.product_list);
        holder.viewDefaultDestination = rootView.findViewById(R.id.layout_destination_default);
        holder.viewPickupLocationCourier = rootView.findViewById(R.id.layout_pickup_instant_shipping_courier);
        holder.pickupLocationDetail = (TextView) rootView.findViewById(R.id.pickup_detail_location);
        holder.deliveryLocationDetail = (TextView) rootView.findViewById(R.id.destination_detail_location);
        holder.wrapperInsurance = (LinearLayout) rootView.findViewById(R.id.wrapper_insurance);
        holder.wrapperBuyerRequestCancel = rootView.findViewById(R.id.wrapper_buyer_request_cancel);
        holder.buyerRequestCancel = (TextView) rootView.findViewById(R.id.buyer_request_cancel);
        holder.dateRequestCancel = (TextView) rootView.findViewById(R.id.date_buyer_request_cancel);
        holder.askBuyer = (TextView) rootView.findViewById(R.id.ask_buyer);
    }

    private void setViewDataV4() {
        OrderPayment payment = order.getOrderPayment();
        OrderCustomer customer = order.getOrderCustomer();
        OrderDetail orderDetail = order.getOrderDetail();
        OrderDestination destination = order.getOrderDestination();
        OrderShipment shipping = order.getOrderShipment();
        OrderShop shop = order.getOrderShop();
        orderId = orderDetail.getDetailOrderId().toString();
        holder.Invoice.setText(orderDetail.getDetailInvoice());
        if (orderDetail.getDetailDropshipName() != null && !orderDetail.getDetailDropshipName().equals("")
                && !orderDetail.getDetailDropshipName().equals("0")) {
            holder.SenderName.setText(orderDetail.getDetailDropshipName());
            holder.SenderPhone.setText(orderDetail.getDetailDropshipTelp());
            holder.SenderForm.setVisibility(View.VISIBLE);
        } else {
            holder.SenderForm.setVisibility(View.GONE);
        }

        if (orderDetail.getDetailCancelRequest() != null && orderDetail.getDetailCancelRequest().getCancelRequest() == 1) {
            holder.wrapperBuyerRequestCancel.setVisibility(View.VISIBLE);
//            if(Build.VERSION.SDK_INT >= 24) {
//                holder.buyerRequestCancel.setText("\"" + Html.fromHtml(orderDetail.getDetailCancelRequest().getReason(), Html.) + "\"");
//            }else{
            holder.buyerRequestCancel.setText(String.format("\"%s\"",
                    MethodChecker.fromHtml(orderDetail.getDetailCancelRequest().getReason()))
            );
//            }
            holder.dateRequestCancel.setText(orderDetail.getDetailCancelRequest().getReasonTime());
        }

        holder.BuyerName.setText(MethodChecker.fromHtml(customer.getCustomerName()));
        userId = customer.getCustomerId();
        holder.AdditionalCost.setText(orderDetail.getDetailAdditionalFeeIdr());
        if (!orderDetail.getDetailInsurancePrice().equals("0") && orderDetail.getDetailInsurancePrice() != null
                && !orderDetail.getDetailInsurancePrice().equals("")) {
            holder.wrapperInsurance.setVisibility(View.VISIBLE);
            holder.PriceInsurance.setText(orderDetail.getDetailInsurancePriceIdr());
        }
        holder.ShippingCost.setText(orderDetail.getDetailShippingPriceIdr());
        holder.Quantity.setText(orderDetail.getDetailQuantity() + " item (" +
                orderDetail.getDetailTotalWeight() + " kg)");
        holder.GrandTotal.setText(orderDetail.getDetailOpenAmountIdr());
        String phoneTokopedia;
        if (destination.getReceiverPhoneIsTokopedia() != 0)
            phoneTokopedia = getString(R.string.title_phone_tokopedia) + " " + destination.getReceiverPhone();
        else
            phoneTokopedia = getString(R.string.title_phone) + " " + destination.getReceiverPhone();
        model = new Model();
        model.shippingID = shipping.getShipmentId();
        model.pickupAddress = MethodChecker.fromHtml(shop.getAddressStreet()
                + "<br/>" + shop.getAddressCity() + ", " + shop.getAddressPostal()
                + "<br/>" + shop.getAddressProvince()
                + "<br/>" + getString(R.string.title_phone) + ":" + shop.getShipperPhone()
        ).toString();
        model.shippingName = shipping.getShipmentName();
        model.shippingProduct = shipping.getShipmentProduct();
        holder.Destination.setText(model.shippingName + " - " + model.shippingProduct);
        String vDest = MethodChecker.fromHtml(
                destination.getReceiverName()
                        + "<br/>" + destination.getAddressStreet()
                        + "<br/>" + destination.getAddressDistrict() + " " + destination.getAddressCity()
                        + ", " + destination.getAddressPostal()
                        + "<br/>" + destination.getAddressProvince() + "<br/>" + phoneTokopedia

        ).toString();
        vDest = vDest.replaceAll("&#39;", "'");
        vDest = vDest.replaceAll("&amp;", "&");
        holder.DestinationDetail.setText(vDest);
        holder.deliveryLocationDetail.setText(vDest);
        holder.pickupLocationDetail.setText(model.pickupAddress);
        if (model.shippingID.equals(TkpdState.SHIPPING_ID.GOJEK)) {
            holder.viewDefaultDestination.setVisibility(View.GONE);
            holder.viewPickupLocationCourier.setVisibility(View.VISIBLE);
        } else {
            holder.viewDefaultDestination.setVisibility(View.VISIBLE);
            holder.viewPickupLocationCourier.setVisibility(View.GONE);
        }

        if(payment != null){
            if (payment.getPaymentProcessDayLeft() != null && payment.getPaymentProcessDayLeft() > 0 && orderDetail.getDetailPartialOrder().equals("1"))
                holder.PartialButton.setVisibility(View.VISIBLE);
            else if (payment.getPaymentProcessDayLeft() < 0)
                holder.AcceptButton.setVisibility(View.GONE);
            holder.Deadline.setText(payment.getPaymentProcessDueDate());
        }else{
            holder.Deadline.setText(orderDetail.getDetailPayDueDate());
        }

        if (permission.equals("0")) {
            holder.AcceptButton.setVisibility(View.GONE);
            holder.RejectButton.setVisibility(View.GONE);
            holder.PartialButton.setVisibility(View.GONE);
        }

        if (isConfirmDone) {
            onProceedComplete(true);
        }
    }


    @SuppressWarnings("EmptyCatchBlock")
    private void setAdapter() {
        adapter = ListViewShopTxDetailProdListV2.createInstanceV4(activity, order.getOrderProducts());
        holder.ProductListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        holder.ProductListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(getProductIntent(order.getOrderProducts().get(position).getProductId().toString()));
            }
        });
        ListViewHelper.getListViewSize(holder.ProductListView);

    }

    private Intent getProductIntent(String productId){
        if (getContext() != null) {
            return RouteManager.getIntent(getContext(),
                    UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId));
        } else {
            return null;
        }
    }

    private void loadViewHolder() {
        holder = (ViewHolder) rootView.getTag();
    }

    private void setListener() {
        if (!isProcessed) holder.AcceptButton.setOnClickListener(onAcceptClick());
        holder.RejectButton.setOnClickListener(onRejectListener());
        holder.PartialButton.setOnClickListener(onPartialListener());
        holder.Invoice.setOnClickListener(onInvoiceListener());
        holder.BuyerName.setOnClickListener(onBuyerName());
        holder.askBuyer.setOnClickListener(onAskBuyerClickListener());
    }

    private View.OnClickListener onAskBuyerClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainApplication.getAppContext() instanceof TkpdInboxRouter) {
                    Intent intent = ((TkpdInboxRouter) MainApplication.getAppContext())
                            .getAskBuyerIntent(getActivity(),
                                    order.getOrderCustomer().getCustomerId(),
                                    order.getOrderCustomer().getCustomerName(),
                                    order.getOrderDetail().getDetailInvoice(),
                                    MethodChecker.fromHtml(
                                            getString(R.string.custom_content_message_ask_seller)
                                                    .replace("XXX",
                                                            order.getOrderDetail()
                                                                    .getDetailPdfUri())).toString(),
                                    TkpdInboxRouter.TX_ASK_BUYER,
                                    order.getOrderCustomer().getCustomerImage());
                    startActivity(intent);
                }


            }
        };
    }

    private View.OnClickListener onBuyerName() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionOpenBuyer();
            }
        };
    }

    private void actionOpenBuyer() {
        if (getActivity().getApplicationContext() instanceof SellerModuleRouter) {
            startActivity(((SellerModuleRouter) getActivity().getApplicationContext())
                    .getTopProfileIntent(getActivity(), userId));
        }
    }


    private View.OnClickListener onAcceptClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAcceptDialog();
                UnifyTracking.eventAcceptOrder(v.getContext());
            }
        };
    }

    private View.OnClickListener onRejectListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRejectDialog();
                UnifyTracking.eventRejectOrder(v.getContext());
            }
        };
    }

    private View.OnClickListener onPartialListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPartialDialog();
            }
        };
    }

    private View.OnClickListener onInvoiceListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.InvoiceDialog(activity, invoiceUri, invoicePdf, holder.Invoice.getText().toString());
            }
        };
    }

    private void createAcceptDialog() {
        NewOrderDialogBuilder.createAcceptDialog(activity, new NewOrderDialogBuilder.OnAcceptListener() {

            @Override
            public void onAccept() {
                bundle = new Bundle();
                ModelParamSelling modelParamSelling = new ModelParamSelling();
                modelParamSelling.setActionType("accept");
                modelParamSelling.setOrderId(orderId);
                modelParamSelling.setPosition(position);

                bundle.putParcelable(SellingService.MODEL_PARAM_SELLING_KEY, Parcels.wrap(modelParamSelling));

                ((SellingDetailActivity) getActivity()).SellingAction(SellingService.CONFIRM_NEW_ORDER, bundle);

//                facadeAction.acceptOrder(onFacadeActionListener());
            }
        });
    }

    private void createPartialDialog() {
        NewOrderDialogBuilder.createPartialDialog(activity, getDialogModel(), new NewOrderDialogBuilder.OnPartialListener() {

            @Override
            public void onAcceptPartial(String remark, String param) {
                bundle = new Bundle();
                ModelParamSelling modelParamSelling = new ModelParamSelling();
                modelParamSelling.setActionType("partial");
                modelParamSelling.setOrderId(orderId);
                modelParamSelling.setReason(remark);
                modelParamSelling.setQtyAccept(param);
                modelParamSelling.setPosition(position);
                bundle.putParcelable(SellingService.MODEL_PARAM_SELLING_KEY, Parcels.wrap(modelParamSelling));

                ((SellingDetailActivity) getActivity()).SellingAction(SellingService.PARTIAL_NEW_ORDER, bundle);
//                progressDialog.showDialog();
//                facadeAction.partialOrder(remark, param, onFacadeActionListener());
            }
        });
    }

    private ConstrainRejectedDialog.OnConfirmReject onConfirmReject = new ConstrainRejectedDialog.OnConfirmReject() {
        @Override
        public void OnConfirmRejectOrder(String reason, ProductListAdapter.Type type) {
            bundle = new Bundle();
            ModelRejectOrder modelRejectOrder = new ModelRejectOrder();
            modelRejectOrder.setOrder_id(orderId);
            modelRejectOrder.setReason(reason);
            modelRejectOrder.setAction_type("reject");
            modelRejectOrder.setPosition(position);
            switch (type) {
                case courrier:
                    modelRejectOrder.setReason_code("7");// 7 is courier problem
                    break;
                case buyer:
                    modelRejectOrder.setReason_code("8");// 8 is buyer request problem
                    break;
            }
            bundle.putParcelable(ModelRejectOrder.MODEL_REJECT_ORDER_KEY, Parcels.wrap(modelRejectOrder));


            ((SellingDetailActivity) getActivity()).SellingAction(SellingService.REJECT_ORDER_WITH_REASON, bundle);
//            ((ShopTransactionV2) getActivity()).SellingAction(SellingService.REJECT_ORDER_WITH_REASON, bundle);
        }
    };

    private void createRejectDialog() {
        Dialog rejectDialog = NewOrderDialogBuilder.createRejectDialog(activity, new NewOrderDialogBuilder.OnSelectRejectReasonListener() {
            @Override
            public void onSelected(String reason, int pos) {
                switch (pos) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        if (isAdded()) {
                            Intent intent = new Intent(activity, ConfirmRejectOrderActivity.class);
                            intent.putExtra(ConfirmRejectOrderActivity.INDEX, pos);
                            intent.putExtra(ConfirmRejectOrderActivity.REASON, reason);
                            intent.putExtra(ConfirmRejectOrderActivity.ORDERS, Parcels.wrap(order));
                            intent.putExtra(ConfirmRejectOrderActivity.ORDER_ID, orderId);
                            startActivityForResult(intent, REQ_REJECT_ORDER);
                        }
                        break;
                    case 4:
                        ConstrainRejectedDialog dialog = ConstrainRejectedDialog.newInstance(reason,
                                ProductListAdapter.Type.courrier);
                        dialog.setOnConfirmReject(onConfirmReject);
                        dialog.show(getFragmentManager(), reason);
                        break;
                    case 5:
                        dialog = ConstrainRejectedDialog.newInstance(reason, ProductListAdapter.Type.buyer);
                        dialog.setOnConfirmReject(onConfirmReject);
                        dialog.show(getFragmentManager(), reason);
                        break;
                }
            }
        });
        rejectDialog.show();
    }

    private NewOrderDialogBuilder.NewOrderDialogModel getDialogModel() {
        NewOrderDialogBuilder.NewOrderDialogModel model = new NewOrderDialogBuilder.NewOrderDialogModel();
        model.productList = order.getOrderProducts();
        model.buyerName = holder.BuyerName.getText().toString();
        model.invoice = holder.Invoice.getText().toString();
        model.grandTotal = holder.GrandTotal.getText().toString();

        return model;
    }

    private void onProceedComplete(boolean isAfterSaveInstance) {
        isProcessed = true;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        holder.AcceptButton.setOnClickListener(null);
        holder.PartialButton.setVisibility(View.GONE);
        holder.RejectButton.setVisibility(View.GONE);
        holder.AcceptButton.setText(getString(R.string.title_order_processed));
        holder.AcceptButton.setBackgroundResource(com.tokopedia.seller.R.drawable.button_finish_disable);
        holder.AcceptButton.setTextColor(ContextCompat.getColor(activity, com.tokopedia.seller.R.color.black_twenty_five_percent));
        holder.AcceptButton.setOnClickListener(null);
        isConfirmDone = true;
        if (!isAfterSaveInstance) {
            getActivity().setResult(getActivity().RESULT_OK);
//            ((SellingDetailActivity) getActivity()).notifyChangeListNewOrder();
        }
    }

    private void onProceedFailed(String messageError) {
        if (messageError.toLowerCase().contains("pesanan tidak valid")) {
            holder.AcceptButton.setEnabled(false);
            holder.AcceptButton.setBackgroundResource(R.drawable.btn_shop);
            holder.AcceptButton.setTextColor(activity.getResources().getColorStateList(R.color.label_color));
            holder.PartialButton.setEnabled(false);
            holder.PartialButton.setBackgroundResource(R.drawable.btn_shop);
            holder.PartialButton.setTextColor(activity.getResources().getColorStateList(R.color.label_color));
            holder.RejectButton.setEnabled(false);
        }
        progressDialog.dismiss();

        showDialogError(activity, messageError);
        //CommonUtils.UniversalToast(getActivity(),"Terjadi masalah koneksi, mohon coba lagi");
        getActivity().setResult(getActivity().RESULT_OK);
//        ((SellingDetailActivity) getActivity()).notifyChangeListNewOrder();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQ_REJECT_ORDER:
                    shouldBroadcast = true;
                    onProceedComplete(false);
                    break;
            }
        }
    }

    private void showDialogError(final Context context, final String error) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        @SuppressLint("InflateParams")
        View promptsView = li.inflate(R.layout.error_network_dialog, null);
        TextView msg = (TextView) promptsView.findViewById(R.id.msg);
        Spanned textError;
        //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        //  textError = MethodChecker.fromHtml(error, Html.FROM_HTML_MODE_LEGACY);
        //} else {
        textError = MethodChecker.fromHtml(error);
        //}
        msg.setText(textError);
        dialog.setView(promptsView);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (error.toLowerCase().contains("pesanan tidak valid")) {
                    activity.onBackPressed();
                }
            }
        });
        if (!activity.isFinishing()) {
            dialog.create().show();
        }
    }

    /**
     * send broadcast to trigger refresh order list
     */
    private void sendBroadcastTrigerOrder() {
        Intent intent = new Intent(RECEIVER_TRIGER_REFRESH_ORDER);
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
    }

    @Override
    public void onResume() {
        if (activity == null) {
            activity = getActivity();
        }
        if (shouldBroadcast) {
            sendBroadcastTrigerOrder();
            shouldBroadcast = false;
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //register receiver for order at selling service
            LocalBroadcastManager.getInstance(activity).registerReceiver(mReceiverBroadcast,
                    new IntentFilter(SellingService.RECEIVER_BROADCAST_ORDER_NAME));
        } else {
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(mReceiverBroadcast);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * receiver for receive order broadcast from selling service
     */
    private BroadcastReceiver mReceiverBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && isAdded()) {
                int resultCode = intent.getIntExtra(SellingService.RESULT_CODE, -1);
                Bundle data = intent.getBundleExtra(SellingService.DATA);
                int type = data.getInt(SellingService.TYPE, SellingService.INVALID_TYPE);
                switch (resultCode) {
                    case SellingService.STATUS_RUNNING:
                        switch (type) {
                            case SellingService.REJECT_ORDER_WITH_REASON:
                            case SellingService.PARTIAL_NEW_ORDER:
                            case SellingService.CONFIRM_NEW_ORDER:
                            case SellingService.REJECT_NEW_ORDER:
                                showProgress();
                                break;
                        }
                        break;
                    case SellingService.STATUS_FINISHED:
                        switch (type) {
                            case SellingService.REJECT_ORDER_WITH_REASON:
                            case SellingService.PARTIAL_NEW_ORDER:
                            case SellingService.CONFIRM_NEW_ORDER:
                            case SellingService.REJECT_NEW_ORDER:
                                setData(type, data);
                                break;
                        }
                        break;
                    case SellingService.STATUS_ERROR:
                        switch (data.getInt(SellingService.NETWORK_ERROR_FLAG, SellingService.INVALID_NETWORK_ERROR_FLAG)) {
                            case NetworkConfig.BAD_REQUEST_NETWORK_ERROR:
                                onNetworkError(type, " BAD_REQUEST_NETWORK_ERROR !!!");
                                break;
                            case NetworkConfig.INTERNAL_SERVER_ERROR:
                                onNetworkError(type, " INTERNAL_SERVER_ERROR !!!");
                                break;
                            case NetworkConfig.FORBIDDEN_NETWORK_ERROR:
                                onNetworkError(type, " FORBIDDEN_NETWORK_ERROR !!!");
                                break;
                            case SellingService.INVALID_NETWORK_ERROR_FLAG:
                            default:
                                String messageError = data.getString(SellingService.MESSAGE_ERROR_FLAG, SellingService.INVALID_MESSAGE_ERROR);
                                if (!messageError.equals(SellingService.INVALID_MESSAGE_ERROR)) {
                                    onMessageError(type, messageError);
                                }
                        }
                        break;
                }// end of switch result code
            }
        }
    };

    private static List<ListViewShopTxDetailProdListV2.TxProdModel> getModelListV4(List<OrderProduct> productList) {
        List<ListViewShopTxDetailProdListV2.TxProdModel> list = new ArrayList<>();
        try {
            for (int i = 0; i < productList.size(); i++) {
                OrderProduct product = productList.get(i);
                ListViewShopTxDetailProdListV2.TxProdModel model = new ListViewShopTxDetailProdListV2.TxProdModel();
                model.ProductId = product.getOrderDetailId().toString();
                model.ProductUrl = product.getProductUrl();
                model.ProductIdList = product.getProductId().toString();
                model.ImageUrl = product.getProductPicture();
                model.Name = MethodChecker.fromHtml(product.getProductName()).toString();
                model.Price = product.getProductPrice();
                model.TotalOrder = product.getProductQuantity().toString();
                model.TotalPrice = product.getOrderSubtotalPriceIdr();
                model.Message = CommonUtils.checkNullForZeroJson(product.getProductNotes()) ? MethodChecker.fromHtml(product.getProductNotes()).toString() : "";
                list.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private ProductPass getProductDataToPass(int position) {
        return ProductPass.Builder.aProductPass()
                .setProductPrice(order.getOrderProducts().get(position).getProductPrice())
                .setProductId(order.getOrderProducts().get(position).getProductId())
                .setProductName(order.getOrderProducts().get(position).getProductName())
                .setProductImage(order.getOrderProducts().get(position).getProductPicture())
                .build();
    }

}
