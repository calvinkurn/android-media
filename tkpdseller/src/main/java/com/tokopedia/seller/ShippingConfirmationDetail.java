package com.tokopedia.seller;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ListViewHelper;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.purchase.model.response.txlist.OrderHistory;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.customadapter.ListViewOrderStatus;
import com.tokopedia.seller.customadapter.ListViewShopOrderDetail;
import com.tokopedia.seller.selling.model.orderShipping.OrderCustomer;
import com.tokopedia.seller.selling.model.orderShipping.OrderDestination;
import com.tokopedia.seller.selling.model.orderShipping.OrderDetail;
import com.tokopedia.seller.selling.model.orderShipping.OrderPayment;
import com.tokopedia.seller.selling.model.orderShipping.OrderProduct;
import com.tokopedia.seller.selling.model.orderShipping.OrderShipment;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.seller.selling.model.orderShipping.OrderShop;
import com.tokopedia.seller.selling.model.shopconfirmationdetail.ShippingConfirmDetModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

public class
ShippingConfirmationDetail extends TActivity {

    public static final String ORDER = "order";
    public static final String PERMISSION = "permission";
    public static final String USER_ID = "user_id";
    public static final String INVOICE_URI = "invoice_uri";
    public static final String INVOICE_PDF = "invoice_pdf";
    public static final String PRODUCT_URI = "product_uri";
    public static final String PRODUCT_ID = "product_id";
    public static final String INVOICE_URI1 = "invoice_uri";
    public static final String INVOICE_PDF1 = "invoice_pdf";
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public static Intent createInstance(Context context, OrderShippingList orderData, String permission, String userId, String pdfUri, String pdf) {
        Intent intent = new Intent(context, ShippingConfirmationDetail.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ORDER, Parcels.wrap(orderData));
        bundle.putString(PERMISSION, permission);
        bundle.putString(USER_ID, userId);
        bundle.putString(INVOICE_URI, pdfUri);
        bundle.putString(INVOICE_PDF, pdf);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    private static final String PHONE_TOKOPEDIA = "021-53691015";

    TextView PaymentMethod;
    TextView Invoice;
    TextView BuyerName;
    ListView ProductListView;
    ListView OrderStatus;
    TextView Deadline;
    TextView ShippingCost;
    TextView AdditionalCost;
    TextView Destination;
    //	@BindView(R2.id.last_status)
//	TextView LastStatus;
    TextView DestinationDetail;
    TextView Quantity;
    TextView GrandTotal;
    TextView ErrorMessage;
    TextView ConfirmButton;
    TextView CancelButton;
    TextView SenderName;
    TextView SenderPhone;
    View SenderForm;
    View viewDefaultDestination;
    View viewPickupLocationCourier;
    TextView pickupLocationDetail;
    TextView deliveryLocationDetail;

    ListViewOrderStatus OrderAdapter;
    ListViewShopOrderDetail ProductAdapter;
    private TkpdProgressDialog mProgressDialog;
    private BroadcastReceiver onComplete;
    private Dialog dialog;

    ShippingConfirmDetModel shippingConfirmDetModel;
    ArrayList<ShippingConfirmDetModel.Data> dataProducts = new ArrayList<>();
    ArrayList<ShippingConfirmDetModel.DataHistory> dataHistories = new ArrayList<>();

    OrderShippingList orderData;
    String invoice_uri;
    String invoice_pdf;
    String UserID;

    private String OrderId;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_TX_S_CONFIRM_SHIPPING;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_shipping_confirmation_detail);
        initView();
        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);

        ConfirmButton.setVisibility(View.GONE);
        CancelButton.setVisibility(View.GONE);

        initModel(Integer.toString(Integer.MIN_VALUE));

        ProductAdapter = new ListViewShopOrderDetail(ShippingConfirmationDetail.this, dataProducts);
        ProductListView.setAdapter(ProductAdapter);
        ProductListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                startActivity(
                        ProductDetailRouter.createInstanceProductDetailInfoActivity(
                                ShippingConfirmationDetail.this,
                                getProductDataToPass(dataProducts.get(position))
                        )
                );
            }
        });

        OrderAdapter = new ListViewOrderStatus(ShippingConfirmationDetail.this, dataHistories);
        OrderStatus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        OrderStatus.setAdapter(OrderAdapter);

        orderData = Parcels.unwrap(getIntent().getExtras().getParcelable(ORDER));
        UserID = getIntent().getExtras().getString(USER_ID);
        invoice_uri = getIntent().getExtras().getString(INVOICE_URI1);
        invoice_pdf = getIntent().getExtras().getString(INVOICE_PDF1);

        setDataToViewV4();


    }

    private void initView(){
        PaymentMethod = (TextView) findViewById(R.id.payment_method);
        Invoice = (TextView) findViewById(R.id.invoice_text);
        BuyerName = (TextView) findViewById(R.id.buyer_name);
        ProductListView = (ListView) findViewById(R.id.product_list);
        OrderStatus = (ListView) findViewById(R.id.order_status);
        Deadline = (TextView) findViewById(R.id.deadline);
        ShippingCost = (TextView) findViewById(R.id.shipping_cost);
        AdditionalCost = (TextView) findViewById(R.id.additional_cost);
        Destination = (TextView) findViewById(R.id.destination);
        DestinationDetail = (TextView) findViewById(R.id.destination_detail);
        Quantity = (TextView) findViewById(R.id.quantity);
        GrandTotal = (TextView) findViewById(R.id.grand_total);
        ErrorMessage = (TextView) findViewById(R.id.error_message);
        ConfirmButton = (TextView) findViewById(R.id.confirm_button);
        CancelButton = (TextView) findViewById(R.id.cancel_button);
        SenderName = (TextView) findViewById(R.id.sender_name);
        SenderPhone = (TextView) findViewById(R.id.sender_phone);
        SenderForm = findViewById(R.id.sender_form);
        viewDefaultDestination = findViewById(R.id.layout_destination_default);
        viewPickupLocationCourier = findViewById(R.id.layout_pickup_instant_shipping_courier);
        pickupLocationDetail = (TextView) findViewById(R.id.pickup_detail_location);
        deliveryLocationDetail = (TextView) findViewById(R.id.destination_detail_location);

        Invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invoiceClick();
            }
        });
        BuyerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBuyerClick();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    private void setDataToViewV4() {

        OrderPayment orderPayment = orderData.getOrderPayment();
        PaymentMethod.setText(MethodChecker.fromHtml(getString(R.string.title_payment_method) + " : <b>" + orderPayment.getPaymentGatewayName() + "</b>"));
        Deadline.setText(orderPayment.getPaymentShippingDueDate());

        OrderCustomer orderCustomer = orderData.getOrderCustomer();
        BuyerName.setText(orderCustomer.getCustomerName());

        OrderDetail orderDetail = orderData.getOrderDetail();
        Invoice.setText(orderDetail.getDetailInvoice());
        if (CommonUtils.checkNullForZeroJson(orderDetail.getDetailDropshipName())
                &&
                CommonUtils.checkNullForZeroJson(orderDetail.getDetailDropshipTelp())) {
            SenderName.setText(orderDetail.getDetailDropshipName());
            SenderPhone.setText(orderDetail.getDetailDropshipTelp());
            SenderForm.setVisibility(View.VISIBLE);
        } else {
            SenderForm.setVisibility(View.GONE);
        }
        AdditionalCost.setText(orderDetail.getDetailTotalAddFeeIdr());
        ShippingCost.setText(orderDetail.getDetailShippingPrice());
        Quantity.setText(orderDetail.getDetailQuantity() + " item (" + orderDetail.getDetailTotalWeight() + " kg)");
        GrandTotal.setText(orderDetail.getDetailOpenAmountIdr());
        OrderId = orderDetail.getDetailOrderId() + "";

        OrderDestination orderDestination = orderData.getOrderDestination();
        String phoneTokopedia = "";
        String receiverPhone = orderDestination.getReceiverPhone();
        if (receiverPhone.equals(PHONE_TOKOPEDIA)) {
            phoneTokopedia = getString(R.string.title_phone_tokopedia) + " : " + receiverPhone;
        } else {
            phoneTokopedia = getString(R.string.title_phone) + " : " + receiverPhone;
        }

        String destinationDetail = MethodChecker.fromHtml(orderDestination.getReceiverName() + "<br>" + orderDestination.getAddressStreet().replace("<br/>", "\n").replace("<br>", "\n")
                + "<br>" + orderDestination.getAddressDistrict() + " " + orderDestination.getAddressCity() + ", " + orderDestination.getAddressPostal()
                + "<br>" + orderDestination.getAddressProvince() + "<br>" + phoneTokopedia).toString();
        destinationDetail = destinationDetail.replaceAll("&#39;", "'");
        DestinationDetail.setText(destinationDetail);
        deliveryLocationDetail.setText(destinationDetail);

        OrderShipment orderShipment = orderData.getOrderShipment();
        Destination.setText(orderShipment.getShipmentName() + " - " + orderShipment.getShipmentProduct());
        String shippingID = orderShipment.getShipmentId();
        if (orderData.getIsPickUp() == 1) {
            viewDefaultDestination.setVisibility(View.GONE);
            viewPickupLocationCourier.setVisibility(View.VISIBLE);
        } else {
            viewDefaultDestination.setVisibility(View.VISIBLE);
            viewPickupLocationCourier.setVisibility(View.GONE);
        }

        OrderShop orderShop = orderData.getOrderShop();
        String pickupAddress = MethodChecker.fromHtml(orderShop.getAddressStreet())
                + "\n" + MethodChecker.fromHtml(orderShop.getAddressCity()).toString() + ", " + MethodChecker.fromHtml(orderShop.getAddressPostal())
                + "\n" + orderShop.getAddressProvince()
                + "\n" + getString(R.string.title_phone) + ":" + orderShop.getShipperPhone();
        pickupLocationDetail.setText(pickupAddress);

        List<OrderProduct> orderProductList = orderData.getOrderProducts();

        initModel(OrderId);

        for (OrderProduct orderProduct : orderProductList) {
            ShippingConfirmDetModel.Data data = new ShippingConfirmDetModel.Data();
            data.ProductId = orderProduct.getOrderDetailId() + "";
            data.ImageUrlList = orderProduct.getProductPicture();
            data.NameList = orderProduct.getProductName();
            data.PriceList = orderProduct.getProductPrice();
            data.ProductUrlList = orderProduct.getProductUrl();
            data.ProductIdList = orderProduct.getProductId() + "";
            data.TtlOrderList = orderProduct.getProductQuantity() + "";
            data.TtlPriceList = orderProduct.getOrderSubtotalPriceIdr();
            data.MessageList = orderProduct.getProductNotes();

            dataProducts.add(data);
        }

        ProductAdapter.notifyDataSetChanged();

        ListViewHelper.getListViewSize(ProductListView);

//		OrderLast orderLast = orderData.getOrderLast();
//		if(CommonUtils.checkNullForZeroJson(orderLast.getLastSellerStatus()) ){
//			LastStatus.setText(getString(R.string.title_last_status) + "\n" + orderLast.getLastSellerStatus());
//		}

        List<OrderHistory> orderHistories = orderData.getOrderHistory();
        for (OrderHistory orderHistory : orderHistories) {
            ShippingConfirmDetModel.DataHistory dataHistory = new ShippingConfirmDetModel.DataHistory();
            dataHistory.ActorList = orderHistory.getHistoryActionBy();
            dataHistory.DateList = orderHistory.getHistoryStatusDate();
            dataHistory.StateList = orderHistory.getHistoryBuyerStatus().replace("<br>", "\n").replace("<br/>", "\n");
            dataHistory.CommentList = orderHistory.getHistoryComments();
            dataHistories.add(dataHistory);
        }
        OrderAdapter.notifyDataSetChanged();

        ListViewHelper.getListViewSize(OrderStatus);
    }

    public void initModel(String orderId) {
        shippingConfirmDetModel = new ShippingConfirmDetModel();
        shippingConfirmDetModel.OrderId = OrderId;
    }


//	private void setDataToView() {
//		try {
//			JSONObject order = new JSONObject(getIntent().getExtras().getString("order"));
//			JSONObject payment = new JSONObject(order.getString("payment"));
//			JSONObject customer = new JSONObject(order.getString("customer"));
//			JSONObject orderdata = new JSONObject(order.getString("order"));
//			JSONObject destination = new JSONObject(order.getString("dest"));
//			JSONObject shipping = new JSONObject(order.getString("shipping"));
//			JSONObject shop = new JSONObject(order.getString("shop"));
//			PaymentMethod.setText(MethodChecker.fromHtml(getString(R.string.title_payment_method) + " : <b>"+ payment.getString("pg_name")+"</b>"));
//			Invoice.setText(orderdata.getString("invoice"));
//			BuyerName.setText(customer.getString("cust_name"));
//			if (!orderdata.isNull("dropship_name")) {
//				SenderName.setText(orderdata.getString("dropship_name"));
//				SenderPhone.setText(orderdata.getString("dropship_telp"));
//				SenderForm.setVisibility(View.VISIBLE);
//			} else {
//				SenderForm.setVisibility(View.GONE);
//			}
//			BuyerName.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					Intent intent = new Intent(ShippingConfirmationDetail.this, People.class);
//					Bundle bundle = new Bundle();
//					bundle.putString(USER_ID, UserID);
//					intent.putExtras(bundle);
//					startActivity(intent);
//				}
//			});
//			AdditionalCost.setText(orderdata.getString("ttl_add_fee_idr"));
//			ShippingCost.setText(orderdata.getString("shipping_price"));
//			Quantity.setText(orderdata.getString("quantity") + " item (" + orderdata.getString("total_weight") + " kg)");
//			GrandTotal.setText(orderdata.getString("open_amt_idr"));
//			Destination.setText(shipping.getString("shipping_name") + " - " + shipping.getString("shipping_product"));
//			String phoneTokopedia;
//			if(destination.getString("phone").equals(PHONE_TOKOPEDIA)) {
//				phoneTokopedia = getString(R.string.title_phone_tokopedia) + " : " +destination.getString("phone");
//			} else  {
//				phoneTokopedia = getString(R.string.title_phone) + " : " +destination.getString("phone");
//			}
//
//			String destinationDetail = MethodChecker.fromHtml(destination.getString("receiver_name") + "<br>" + destination.getString("address_name").replace("<br/>", "\n").replace("<br>", "\n")
//					+ "<br>" + destination.getString("district") + " " + destination.getString("city") + ", " + destination.getString("postal")
//					+ "<br>" + destination.getString("province") + "<br>" + phoneTokopedia).toString();
//			String shippingID = shipping.getString("shipping_id");
//			String pickupAddress = MethodChecker.fromHtml(shop.optString("addr_street", ""))
//					+ "\n" + MethodChecker.fromHtml(shop.optString("city", "")).toString() + ", " + MethodChecker.fromHtml(shop.optString("postal_code", ""))
//					+ "\n" + shop.optString("province")
//					+ "\n" + getString(R.string.title_phone) + ":" + shop.optString("phone", "");
//			pickupLocationDetail.setText(pickupAddress);
//			if (shippingID.equals("10")) {
//				viewDefaultDestination.setVisibility(View.GONE);
//				viewPickupLocationCourier.setVisibility(View.VISIBLE);
//			} else {
//				viewDefaultDestination.setVisibility(View.VISIBLE);
//				viewPickupLocationCourier.setVisibility(View.GONE);
//			}
//			DestinationDetail.setText(destinationDetail);
//			deliveryLocationDetail.setText(destinationDetail);
//			JSONArray productList = new JSONArray(order.getString("products"));
//			OrderId = orderdata.getString("order_id");
//			JSONObject product;
//			Deadline.setText(payment.getString("shipping_due_date"));
//			for(int i=0; i<productList.length();i++){
//				product = new JSONObject(productList.getString(i));
//				ProductId.add(product.getString("order_dtl_id"));
//				ImageUrlList.add(product.getString("product_pic"));
//				NameList.add(product.getString("product_name"));
//				PriceList.add(product.getString("product_price"));
//				ProductUrlList.add(product.getString("product_url"));
//				ProductIdList.add(product.getString("product_id"));
//				TtlOrderList.add(product.getString("deliver_qty"));
//				TtlPriceList.add(product.getString("subtotal_price_idr"));
//				MessageList.add(product.getString("notes"));
//			}
//			ProductAdapter.notifyDataSetChanged();
//			JSONObject Last = new JSONObject(order.getString("last"));
//			if(Last.isNull("seller_state"))
//				LastStatus.setText(getString(R.string.title_last_status) + "\n" + Last.getString("seller_state"));
//			JSONArray StatusList = new JSONArray(order.getString("order_history"));
//			JSONObject Status;
//			for(int i=0; i<StatusList.length(); i++){
//				Status = new JSONObject(StatusList.getString(i));
//				ActorList.add(Status.getString("action_by"));
//				DateList.add(Status.getString("status_date"));
//				StateList.add(Status.getString("state_buyer").replace("<br>", "\n").replace("<br/>", "\n"));
//				CommentList.add(Status.getString("comments"));
//			}
////			if(getIntent().getExtras().getString("permission").equals("0")){
////				ConfirmButton.setVisibility(View.GONE);
////				CancelButton.setVisibility(View.GONE);
////			}
//			OrderAdapter.notifyDataSetChanged();
//			ListViewHelper.getListViewSize(ProductListView);
//			ListViewHelper.getListViewSize(OrderStatus);
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//	}

    public void invoiceClick() {
        AppUtils.InvoiceDialog(ShippingConfirmationDetail.this, invoice_uri, invoice_pdf, Invoice.getText().toString());
    }

    public void onBuyerClick() {
        startActivity(((TkpdCoreRouter) this.getApplicationContext())
                .getTopProfileIntent(this,
                        UserID));
    }

    private void Loading() {
        mProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        mProgressDialog.showDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ProductPass getProductDataToPass(ShippingConfirmDetModel.Data data) {
        return ProductPass.Builder.aProductPass()
                .setProductPrice(data.PriceList)
                .setProductId(data.ProductIdList)
                .setProductName(data.NameList)
                .setProductImage(data.ImageUrlList)
                .build();
    }
}
