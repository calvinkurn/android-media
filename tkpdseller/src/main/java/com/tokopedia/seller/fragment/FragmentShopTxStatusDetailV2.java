package com.tokopedia.seller.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
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
import com.tokopedia.seller.OrderHistoryView;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.customView.OrderStatusView;
import com.tokopedia.seller.customadapter.ListViewShopTxDetailProdListV2;
import com.tokopedia.seller.facade.FacadeActionShopTransaction;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.selling.activity.TrackingActivity;
import com.tokopedia.core.selling.model.orderShipping.OrderCustomer;
import com.tokopedia.core.selling.model.orderShipping.OrderDestination;
import com.tokopedia.core.selling.model.orderShipping.OrderDetail;
import com.tokopedia.core.selling.model.orderShipping.OrderHistory;
import com.tokopedia.core.selling.model.orderShipping.OrderPayment;
import com.tokopedia.core.selling.model.orderShipping.OrderShipment;
import com.tokopedia.core.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.core.selling.model.orderShipping.OrderShop;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.RequestPermissionUtil;

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
 * Created by Tkpd_Eka on 2/24/2015.
 */
@RuntimePermissions
public class FragmentShopTxStatusDetailV2 extends Fragment {

    public FragmentShopTxStatusDetailV2() {

    }

    public static FragmentShopTxStatusDetailV2 createInstance(OrderShippingList orderData, String permission) {
        FragmentShopTxStatusDetailV2 fragment = new FragmentShopTxStatusDetailV2();
        Bundle bundle = new Bundle();
        bundle.putParcelable("order_data", Parcels.wrap(orderData));
        bundle.putString("permission", permission);
        fragment.setArguments(bundle);
        return fragment;
    }

    private static final String PHONE_TOKOPEDIA = "021-53691015";
    private View rootView;
    private ViewHolder holder;
    private OrderShippingList order;
    private TkpdProgressDialog progressDialog;
    private Model model;
    private ListViewShopTxDetailProdListV2 productAdapter;
    private FacadeActionShopTransaction facadeAction;
    private Dialog editRefDialog;
    private CompositeSubscription _subscriptions = new CompositeSubscription();

    private static class Model {
        private String orderId;
        private String userId;
        private String invoiceUrl;
        private String invoicePdf;
        private String permission;
        private java.util.List<OrderHistory> statusList;
        private String refNum;
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
        public View viewDefaultDestination;
        public View viewPickupLocationCourier;
        public TextView pickupLocationDetail;
        public TextView deliveryLocationDetail;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            initCreateView(inflater, container);
        } else
            holder = (ViewHolder) rootView.getTag();

        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);
        setViewData();
        setListener();
        if (getActivity() != null) {
            ScreenTracking.screen(this);
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxUtils.unsubscribeIfNotNull(_subscriptions);
    }

    private void initCreateView(LayoutInflater inflater, ViewGroup container) {
        holder = new ViewHolder();
        rootView = inflater.inflate(R.layout.activity_shop_shipping_status_detail, container, false);
        initView();
        setAdapter();
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
        holder.viewPickupLocationCourier = rootView.findViewById(R.id.layout_pickup_instant_shipping_courier);
        holder.pickupLocationDetail = (TextView) rootView.findViewById(R.id.pickup_detail_location);
        holder.deliveryLocationDetail = (TextView) rootView.findViewById(R.id.destination_detail_location);
    }

    private void setViewData() {
        OrderPayment payment = order.getOrderPayment();
        OrderCustomer customer = order.getOrderCustomer();
        OrderDetail orderdata = order.getOrderDetail();
        OrderDestination destination = order.getOrderDestination();
        OrderShipment shipping = order.getOrderShipment();
        OrderShop shop = order.getOrderShop();
        holder.PaymentMethod.setText(Html.fromHtml(getString(R.string.title_payment_method) + " : <b>" + payment.getPaymentGatewayName() + "</b>"));
        holder.Invoice.setText(orderdata.getDetailInvoice());
        if (CommonUtils.checkNullForZeroJson(orderdata.getDetailDropshipName())) {
            holder.SenderName.setText(Html.fromHtml(orderdata.getDetailDropshipName()));
            holder.SenderPhone.setText(orderdata.getDetailDropshipTelp());
            holder.SenderForm.setVisibility(View.VISIBLE);
        } else {
            holder.SenderForm.setVisibility(View.GONE);
        }
        holder.BuyerName.setText(Html.fromHtml(customer.getCustomerName()));
        holder.AdditionalCost.setText(orderdata.getDetailTotalAddFeeIdr());
        holder.ShippingCost.setText(orderdata.getDetailShippingPriceIdr());
        holder.Quantity.setText(orderdata.getDetailQuantity() + " item (" + orderdata.getDetailTotalWeight() + " kg)");
        holder.GrandTotal.setText(orderdata.getDetailOpenAmountIdr());
        setRefNum(model.refNum);
        holder.Destination.setText(Html.fromHtml(shipping.getShipmentName() + " - " + shipping.getShipmentProduct()));
        holder.Transaction.setText(payment.getPaymentVerifyDate());
        String phoneTokopedia;
        if (destination.getReceiverPhoneIsTokopedia() == 1) {
            phoneTokopedia = getString(R.string.title_phone_tokopedia) + " : " + destination.getReceiverPhone();
        } else {
            phoneTokopedia = getString(R.string.title_phone) + " : " + destination.getReceiverPhone();
        }
        String vDest = Html.fromHtml(destination.getReceiverName()).toString() + "\n" + Html.fromHtml(destination.getAddressStreet().replace("<br/>", "\n").replace("<br>", "\n")).toString()
                + "\n" + destination.getAddressDistrict() + " " + destination.getAddressCity() + ", " + destination.getAddressPostal()
                + "\n" + destination.getAddressProvince() + "\n" + phoneTokopedia;
        String shippingID = shipping.getShipmentId();
        String pickupAddress = Html.fromHtml(shop.getAddressStreet())
                + "\n" + Html.fromHtml(shop.getAddressCity()).toString() + ", " + Html.fromHtml(shop.getAddressPostal())
                + "\n" + shop.getAddressProvince()
                + "\n" + getString(R.string.title_phone) + ":" + shop.getShipperPhone();
        holder.pickupLocationDetail.setText(pickupAddress);
        if (order.getIsPickUp() == 1) {
            holder.viewDefaultDestination.setVisibility(View.GONE);
            holder.viewPickupLocationCourier.setVisibility(View.VISIBLE);
        } else {
            holder.viewDefaultDestination.setVisibility(View.VISIBLE);
            holder.viewPickupLocationCourier.setVisibility(View.GONE);
        }
        vDest = vDest.replaceAll("&#39;", "'");
        holder.deliveryLocationDetail.setText(Html.fromHtml(vDest));
        holder.DestinationDetail.setText(Html.fromHtml(vDest));
        holder.Track.setVisibility(View.GONE);
        holder.EditRef.setVisibility(View.GONE);
        if (validatingOrderData(orderdata) && holder.RefNumber.length() > 0) {
            holder.Track.setVisibility(View.VISIBLE);
            if (order.getIsPickUp() == 1) {
                holder.EditRef.setVisibility(View.GONE);
            } else {
                holder.EditRef.setVisibility(View.VISIBLE);
            }
        }
        if (model.permission.equals("0")) {
            holder.EditRef.setVisibility(View.GONE);
            holder.Track.setVisibility(View.GONE);
        }
        setOrderStatus();
    }

    private boolean validatingOrderData(OrderDetail orderdata) {
        return (orderdata.getDetailOrderStatus() == 500
                || orderdata.getDetailOrderStatus() == 501
                || orderdata.getDetailOrderStatus() == 520
                || orderdata.getDetailOrderStatus() == 530);
    }

    private void setOrderStatus() {
        holder.OrderStatusLayout.removeAllViews();
        for (int i = 0; (i < model.statusList.size() && i < 2); i++) {
            //holder.OrderStatusLayout.addView(OrderStatusView.createInstance(getActivity(), model.statusList.get(i)).getView());
        }
    }

    private void setAdapter() {
        productAdapter = ListViewShopTxDetailProdListV2.createInstance(getActivity(), order.getOrderProducts());
        holder.ProductListView.setAdapter(productAdapter);
        holder.ProductListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(ProductInfoActivity.createInstance(getActivity(), order.getOrderProducts().get(position).getProductId().toString()));
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
    }

    private View.OnClickListener onBuyerNameClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        PeopleInfoNoDrawerActivity.createInstance(getActivity(), model.userId)
                );
            }
        };
    }

    private View.OnClickListener onSeeAllClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(OrderHistoryView.createInstanceSeller(getActivity(),
                        model.statusList));
            }
        };
    }

    private View.OnClickListener onInvoiceClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.InvoiceDialog(getActivity(), model.invoiceUrl,
                        model.invoicePdf, holder.Invoice.getText().toString());
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
        bundle.putString("OrderID", model.orderId);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    private void createEditRefDialog() {
        editRefDialog = new Dialog(getActivity());
        editRefDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editRefDialog.setContentView(R.layout.dialog_edit_ref);
        final EditText Ref = (EditText) editRefDialog.findViewById(R.id.ref_number);
        Ref.setText(model.refNum);
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

    @NeedsPermission({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onScanBarcodeClicked() {
        startActivityForResult(CommonUtils.requestBarcodeScanner(), 0);
    }

    private boolean checkEditRef(EditText ref) {
        if (ref.getText().toString().equals(model.refNum)) {
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
        model.refNum = refNum;
        holder.ErrorMessage.setVisibility(View.GONE);
        progressDialog.showDialog();
        facadeAction.setCompositeSubscription(_subscriptions);
        facadeAction.editRefNum(refNum, onEditRefNum());
    }

    private FacadeActionShopTransaction.OnEditRefNumListener onEditRefNum() {
        return new FacadeActionShopTransaction.OnEditRefNumListener() {
            @Override
            public void onSuccess(String refNum) {
                progressDialog.dismiss();
                model.refNum = refNum;
                holder.RefNumber.setText(refNum);
                setRefNum(refNum);
            }

            @Override
            public void onFailed(String errorMsg) {
                progressDialog.dismiss();
                holder.ErrorMessage.setText(errorMsg);
                holder.ErrorMessage.setVisibility(View.VISIBLE);
            }
        };
    }

    private void setRefNum(String refNum) {
        if (refNum.length() > 0)
            holder.RefNumber.setText(Html.fromHtml(getString(R.string.title_reference_number)
                    + " : <b>" + refNum + "</b>"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentArguments();
        getOrderData();
        initVar();
    }

    private void getFragmentArguments() {
        try {
            order = Parcels.unwrap(getArguments().getParcelable("order_data"));
            model = new Model();
            model.permission = getArguments().getString("permission");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), getActivity().getString(R.string.title_verification_timeout) + "\n" + getActivity().getString(R.string.message_verification_timeout), Toast.LENGTH_LONG).show();
            Crashlytics.log(0, "NullPointerException " + getActivity().getClass().getSimpleName().toString(), e.toString());
            getActivity().finish();
        }
    }

    private void getOrderData() {
        OrderCustomer customer = order.getOrderCustomer();
        OrderDetail orderdata = order.getOrderDetail();
        model.orderId = orderdata.getDetailOrderId().toString();
        model.userId = customer.getCustomerId();
        model.invoicePdf = orderdata.getDetailPdf();
        model.invoiceUrl = orderdata.getDetailPdfUri();
        model.statusList = order.getOrderHistory();
        model.refNum = orderdata.getDetailShipRefNum();
    }

    private void initVar() {
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        facadeAction = FacadeActionShopTransaction.createInstance(getActivity(), model.orderId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            final EditText Ref = (EditText) editRefDialog.findViewById(R.id.ref_number);
            Ref.setText(CommonUtils.getBarcode(data));
        }
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
}
